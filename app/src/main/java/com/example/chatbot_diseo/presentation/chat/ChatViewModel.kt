package com.example.chatbot_diseo.presentation.chat

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chatbot_diseo.data.api.TokenHolder
import com.example.chatbot_diseo.data.repository.ChatbotRepository
import kotlinx.coroutines.launch

class ChatViewModel : ViewModel() {

    private val chatbotRepository = ChatbotRepository()

    val mensajes = mutableStateListOf<Mensaje>()
    val isLoading = mutableStateOf(false)
    val error = mutableStateOf<String?>(null)

    // ID del usuario actual - se obtiene de TokenHolder después del login
    val usuarioId: String?
        get() = TokenHolder.usuarioId

    // ID de la conversación actual
    var conversacionId: String? = null

    // Preguntas sugeridas del chatbot
    val sugerencias = listOf(
        "¿Qué es el onboarding? 🧭",
        "¿Dónde veo mis documentos? 📄",
        "¿Qué actividades debo completar? 📌",
        "¿Cómo contacto a mi supervisor? 🧑‍💼",
        "¿Qué puedo hacer en esta aplicación? 🤖"
    )

    // Respuestas fijas a preguntas sugeridas (versión corta)
    val respuestasPredefinidas = mapOf(

        "¿Qué es el onboarding? 🧭" to Mensaje(
            texto = "El onboarding es el proceso donde te integras a TCS. Aquí completas tus documentos, actividades y recibes la información inicial necesaria para tu ingreso ✨.",
            esUsuario = false
        ),

        "¿Dónde veo mis documentos? 📄" to Mensaje(
            texto = "Tus documentos cargados están en la sección Documentos. Ahí podrás revisar lo que ya enviaste y lo que aún te falta 👍.\n\nPresiona aquí para ir directo:",
            esUsuario = false,
            textoAccion = "Ir a Documentos",
            accion = null,
            actionRoute = "recursos"
        ),

        "¿Qué actividades debo completar? 📌" to Mensaje(
            texto = "Tus actividades del onboarding aparecen en la sección Actividades. Ahí ves tus tareas pendientes, completadas y su detalle 📍.\n\nIngresa aquí:",
            esUsuario = false,
            textoAccion = "Ver Actividades",
            accion = null,
            actionRoute = "calendario"
        ),

        "¿Cómo contacto a mi supervisor? 🧑‍💼" to Mensaje(
            texto = "Puedes ver a tu supervisor en tu perfil. Encontrarás su nombre, área y correo institucional para contactarlo fácilmente 💼.\n\nToca aquí para ir:",
            esUsuario = false,
            textoAccion = "Ver Supervisor",
            accion = null,
            actionRoute = "perfil"
        ),

        "¿Qué puedo hacer en esta aplicación? 🤖" to Mensaje(
            texto = "Aquí puedes revisar tus documentos, ver tus actividades, consultar tu información personal y usar el asistente para resolver dudas del onboarding 🙌.",
            esUsuario = false
        )
    )

    init {
        mensajes.add(Mensaje("¡Hola! 👋 Soy tu asistente virtual. ¿En qué puedo ayudarte hoy?", false))
    }

    var navegarADocumentos: (() -> Unit)? = null
    var navegarAActividades: (() -> Unit)? = null
    var navegarAPerfil: (() -> Unit)? = null

    /**
     * Enviar mensaje al chatbot con IA
     */
    fun enviarMensaje(texto: String) {
        if (texto.isBlank()) return

        val textoTrim = texto.trim()

        // Agregar mensaje del usuario
        mensajes.add(Mensaje(textoTrim, true))

        // Respuesta predefinida sin llamar al backend
        // Buscamos coincidencias de forma más tolerante: exacta (ignore case) o parcial
        val matched = respuestasPredefinidas.entries.firstOrNull { (key, _) ->
            val keyTrim = key.trim()
            keyTrim.equals(textoTrim, ignoreCase = true) ||
                    keyTrim.contains(textoTrim, ignoreCase = true) ||
                    textoTrim.contains(keyTrim, ignoreCase = true)
        }
        matched?.value?.let { respuesta ->
            // Si la respuesta tiene actionRoute, adjuntamos la accion correspondiente como fallback
            val respuestaConAccion = when (respuesta.actionRoute) {
                "recursos" -> respuesta.copy(accion = { navegarADocumentos?.invoke() })
                "calendario", "actividades" -> respuesta.copy(accion = { navegarAActividades?.invoke() })
                "perfil" -> respuesta.copy(accion = { navegarAPerfil?.invoke() })
                else -> respuesta
            }
            // Añadimos el Mensaje predefinido tal cual (mantiene textoAccion y actionRoute)
            mensajes.add(respuestaConAccion)
            return
        }

        // Verificar si hay usuario logueado
        val userId = usuarioId
        if (userId.isNullOrBlank()) {
            mensajes.add(Mensaje("Por favor inicia sesión para usar el chatbot.", false))
            return
        }

        isLoading.value = true
        error.value = null

        // Agregar mensaje de carga
        mensajes.add(Mensaje("⏳ Analizando tu pregunta con IA... (esto toma 15-60 segundos)", false))

        viewModelScope.launch {
            try {
                val result = chatbotRepository.enviarPregunta(userId, textoTrim)

                result.onSuccess { response ->
                    // Remover mensaje de carga
                    if (mensajes.isNotEmpty() && mensajes.last().texto.startsWith("⏳")) {
                        mensajes.removeAt(mensajes.size - 1)
                    }

                    // Guardar el ID de la conversación
                    response.conversacionId?.let { conversacionId = it }
                    // Agregar respuesta del bot
                    mensajes.add(Mensaje(response.respuesta, false))
                }.onFailure { e ->
                    // Remover mensaje de carga
                    if (mensajes.isNotEmpty() && mensajes.last().texto.startsWith("⏳")) {
                        mensajes.removeAt(mensajes.size - 1)
                    }

                    error.value = e.message
                    val errorMsg = when {
                        e.message?.contains("timeout", ignoreCase = true) == true ->
                            "⏱️ La respuesta tardó demasiado. El servidor está muy ocupado. Por favor, intenta de nuevo."
                        e.message?.contains("401") == true ->
                            "🔐 Error de autenticación. Por favor, inicia sesión nuevamente."
                        e.message?.contains("404") == true ->
                            "❌ Servicio no disponible. Verifica la conexión del servidor."
                        else -> "Lo siento, ocurrió un error: ${e.message}"
                    }
                    mensajes.add(Mensaje(errorMsg, false))
                }
            } catch (e: Exception) {
                // Remover mensaje de carga
                if (mensajes.isNotEmpty() && mensajes.last().texto.startsWith("⏳")) {
                    mensajes.removeAt(mensajes.size - 1)
                }

                error.value = e.message
                mensajes.add(Mensaje("Error de conexión. Por favor intenta de nuevo.", false))
            } finally {
                isLoading.value = false
            }
        }
    }

    /**
     * Registrar satisfacción con la respuesta (1-5)
     */
    fun registrarSatisfaccion(satisfaccion: Int) {
        val convId = conversacionId ?: return

        viewModelScope.launch {
            try {
                chatbotRepository.registrarSatisfaccion(convId, satisfaccion)
            } catch (e: Exception) {
                // Error silencioso
            }
        }
    }

    /**
     * Limpiar el chat e iniciar nueva conversación
     */
    fun limpiarChat() {
        mensajes.clear()
        conversacionId = null
        mensajes.add(Mensaje("¡Nuevo chat iniciado! 👋 ¿En qué puedo ayudarte ahora?", false))
    }

    /**
     * Verificar estado del servicio de chatbot
     */
    fun verificarEstadoChatbot() {
        viewModelScope.launch {
            try {
                chatbotRepository.healthCheck()
            } catch (e: Exception) {
                // Error silencioso
            }
        }
    }
}