package com.example.chatbot_diseo.presentation.chat

import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chatbot_diseo.data.api.TokenHolder
import com.example.chatbot_diseo.data.remote.apiChatBot.RetrofitInstance
import com.example.chatbot_diseo.data.repository.ChatbotRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class ChatViewModel : ViewModel() {

    companion object {
        private const val TAG = "ChatViewModel"
    }

    private val chatbotRepository = ChatbotRepository()

    val mensajes = mutableStateListOf<Mensaje>()
    val isLoading = mutableStateOf(false)
    val error = mutableStateOf<String?>(null)

    // Controlar visibilidad de FAQs / sugerencias
    val mostrarSugerencias = mutableStateOf(true)

    // ID del usuario actual - se obtiene de TokenHolder después del login
    val usuarioId: String?
        get() = TokenHolder.usuarioId

    // ID de la conversación actual
    var conversacionId: String? = null

    // Preguntas sugeridas del chatbot (visibles en UI, se pueden ir removiendo)
    val sugerencias = mutableStateListOf(
        "¿Qué es el onboarding?",
        "¿Dónde veo mis documentos?",
        "¿Qué actividades debo completar?",
        "¿Cómo contacto a mi supervisor?",
        "¿Qué puedo hacer en esta aplicación?"
    )

    // Respuestas fijas a preguntas sugeridas (versión corta)
    private val respuestasPredefinidas = mapOf(

        "¿Qué es el onboarding?" to Mensaje(
            texto = "El onboarding es el proceso donde te integras a TCS. Aquí completas tus documentos, actividades y recibes la información inicial necesaria para tu ingreso.",
            esUsuario = false
        ),

        "¿Dónde veo mis documentos?" to Mensaje(
            texto = "Tus documentos cargados están en la sección Documentos. Ahí podrás revisar lo que ya enviaste y lo que aún te falta.\n\nPresiona aquí para ir directo:",
            esUsuario = false,
            textoAccion = "Ir a Documentos",
            accion = null,
            actionRoute = "recursos"
        ),

        "¿Qué actividades debo completar?" to Mensaje(
            texto = "Tus actividades del onboarding aparecen en la sección Actividades. Ahí ves tus tareas pendientes, completadas y su detalle.\n\nIngresa aquí:",
            esUsuario = false,
            textoAccion = "Ver Actividades",
            accion = null,
            actionRoute = "calendario"
        ),

        "¿Cómo contacto a mi supervisor?" to Mensaje(
            texto = "Puedes ver a tu supervisor en tu perfil. Encontrarás su nombre, área y correo institucional para contactarlo fácilmente.\n\nToca aquí para ir:",
            esUsuario = false,
            textoAccion = "Ver Supervisor",
            accion = null,
            actionRoute = "perfil"
        ),

        "¿Qué puedo hacer en esta aplicación?" to Mensaje(
            texto = "Aquí puedes revisar tus documentos, ver tus actividades, consultar tu información personal y usar el asistente para resolver dudas del onboarding.",
            esUsuario = false
        )
    )

    init {
        mensajes.add(Mensaje("¡Hola! Soy tu asistente virtual. ¿En qué puedo ayudarte hoy?", false))
    }

    var navegarADocumentos: (() -> Unit)? = null
    var navegarAActividades: (() -> Unit)? = null
    var navegarAPerfil: (() -> Unit)? = null

    /**
     * Enviar mensaje al chatbot con IA
     *
     * @param texto               Texto de la consulta
     * @param ocultarSugerencias  Si es true, las FAQs se ocultan (solo para input escrito)
     */
    fun enviarMensaje(texto: String, ocultarSugerencias: Boolean = false) {
        if (texto.isBlank()) return

        val textoTrim = texto.trim()

        // Agregar mensaje del usuario
        mensajes.add(Mensaje(textoTrim, true))

        // Ocultar sugerencias solo cuando viene de input escrito
        if (ocultarSugerencias) {
            mostrarSugerencias.value = false
        }

        // Respuesta predefinida sin llamar al backend
        // Buscamos coincidencias de forma más tolerante: exacta (ignore case) o parcial
        val matchedEntry = respuestasPredefinidas.entries.firstOrNull { (key, _) ->
            val keyTrim = key.trim()
            keyTrim.equals(textoTrim, ignoreCase = true) ||
                    keyTrim.contains(textoTrim, ignoreCase = true) ||
                    textoTrim.contains(keyTrim, ignoreCase = true)
        }

        if (matchedEntry != null) {
            val (key, baseRespuesta) = matchedEntry

            // Si la respuesta tiene actionRoute, adjuntamos la acción correspondiente como fallback
            val respuestaConAccion = when (baseRespuesta.actionRoute) {
                "recursos" -> baseRespuesta.copy(accion = { navegarADocumentos?.invoke() })
                "calendario", "actividades" -> baseRespuesta.copy(accion = { navegarAActividades?.invoke() })
                "perfil" -> baseRespuesta.copy(accion = { navegarAPerfil?.invoke() })
                else -> baseRespuesta
            }

            // Desde FAQ (ocultarSugerencias = false): quitar solo esa pregunta de la lista visible
            if (!ocultarSugerencias) {
                val index = sugerencias.indexOfFirst { it.equals(key, ignoreCase = true) }
                if (index >= 0) {
                    sugerencias.removeAt(index)
                }
            }

            // Simular "escribiendo..." siempre por 1s
            isLoading.value = true
            error.value = null
            mensajes.add(Mensaje(TYPING_MESSAGE_TEXT, false))

            viewModelScope.launch {
                delay(1000L)
                if (mensajes.isNotEmpty() && mensajes.last().texto == TYPING_MESSAGE_TEXT) {
                    mensajes.removeAt(mensajes.size - 1)
                }
                mensajes.add(respuestaConAccion)
                isLoading.value = false
            }

            return
        }

        // Verificar si hay usuario logueado para llamadas al backend
        val userId = usuarioId
        if (userId.isNullOrBlank()) {
            mensajes.add(Mensaje("Por favor inicia sesión para usar el chatbot.", false))
            return
        }

        isLoading.value = true
        error.value = null

        // Agregar mensaje de "escribiendo..." del bot
        mensajes.add(Mensaje(TYPING_MESSAGE_TEXT, false))

        viewModelScope.launch {
            val startTime = System.currentTimeMillis()
            try {
                val result = chatbotRepository.enviarPregunta(userId, textoTrim)

                // Garantizar mínimo 1 segundo de "escribiendo..."
                val elapsed = System.currentTimeMillis() - startTime
                if (elapsed < 1000L) {
                    delay(1000L - elapsed)
                }

                result
                    .onSuccess { response ->
                        if (mensajes.isNotEmpty() && mensajes.last().texto == TYPING_MESSAGE_TEXT) {
                            mensajes.removeAt(mensajes.size - 1)
                        }

                        // Guardar el ID de la conversación
                        response.conversacionId?.let { conversacionId = it }
                        // Agregar respuesta del bot
                        mensajes.add(Mensaje(response.respuesta, false))
                    }
                    .onFailure { e ->
                        if (mensajes.isNotEmpty() && mensajes.last().texto == TYPING_MESSAGE_TEXT) {
                            mensajes.removeAt(mensajes.size - 1)
                        }

                        error.value = e.message
                        val errorMsg = when {
                            e.message?.contains("timeout", ignoreCase = true) == true ->
                                "La respuesta tardó demasiado. El servidor está muy ocupado. Por favor, intenta de nuevo."
                            e.message?.contains("401") == true ->
                                "Error de autenticación. Por favor, inicia sesión nuevamente."
                            e.message?.contains("404") == true ->
                                "Servicio no disponible. Verifica la conexión del servidor."
                            else -> "Lo siento, ocurrió un error: ${e.message}"
                        }
                        mensajes.add(Mensaje(errorMsg, false))
                    }
            } catch (e: Exception) {
                Log.e(TAG, "enviarMensaje excepción", e)
                val elapsed = System.currentTimeMillis() - startTime
                if (elapsed < 1000L) {
                    delay(1000L - elapsed)
                }

                if (mensajes.isNotEmpty() && mensajes.last().texto == TYPING_MESSAGE_TEXT) {
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
    @Suppress("unused")
    fun registrarSatisfaccion(satisfaccion: Int) {
        val convId = conversacionId ?: return

        viewModelScope.launch {
            try {
                chatbotRepository.registrarSatisfaccion(convId, satisfaccion)
            } catch (e: Exception) {
                Log.e(TAG, "registrarSatisfaccion excepción", e)
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
        mensajes.add(Mensaje("¡Nuevo chat iniciado! ¿En qué puedo ayudarte ahora?", false))
        mostrarSugerencias.value = true
        // Restaurar todas las sugerencias iniciales si se han ido removiendo
        sugerencias.clear()
        sugerencias.addAll(
            listOf(
                "¿Qué es el onboarding?",
                "¿Dónde veo mis documentos?",
                "¿Qué actividades debo completar?",
                "¿Cómo contacto a mi supervisor?",
                "¿Qué puedo hacer en esta aplicación?"
            )
        )
    }

    /**
     * Verificar estado del servicio de chatbot
     */
    @Suppress("unused")
    fun verificarEstadoChatbot() {
        viewModelScope.launch {
            try {
                chatbotRepository.healthCheck()
            } catch (e: Exception) {
                Log.e(TAG, "verificarEstadoChatbot excepción", e)
                // Error silencioso
            }
        }
    }

    /**
     * Cargar una conversacion completa (historial) por su ID desde la API y poblar los mensajes
     */
    @Suppress("unused")
    fun cargarConversacionPorId(id: String) {
        viewModelScope.launch {
            isLoading.value = true
            try {
                val response = RetrofitInstance.conversacionApi.getConversacionById(id)
                if (response.isSuccessful) {
                    val conversacionCompleta = response.body()
                    if (conversacionCompleta != null) {
                        // Limpiar mensajes actuales y agregar los de la conversacion
                        mensajes.clear()

                        // Manejar caso nullable y mapear MensajeChat -> Mensaje (estructura UI)
                        val listaMensajes = conversacionCompleta.mensajes ?: emptyList()
                        listaMensajes.forEach { m ->
                            val esUsuario = m.tipo.equals("usuario", ignoreCase = true)
                            mensajes.add(Mensaje(m.contenido ?: "", esUsuario))
                        }

                        conversacionId = id
                        mostrarSugerencias.value = false
                    } else {
                        error.value = "Conversación vacía"
                    }
                } else {
                    error.value = "Error al cargar conversación: ${response.code()}"
                }
            } catch (e: Exception) {
                Log.e(TAG, "cargarConversacionPorId excepción", e)
                error.value = e.message
            } finally {
                isLoading.value = false
            }
        }
    }
}
