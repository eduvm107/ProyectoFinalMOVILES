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

    val sugerencias = listOf(
        "Ver mi calendario 📅",
        "Revisar documentos 📄",
        "Consultar cursos 🎓",
        "Hablar con RRHH 💬"
    )

    init {
        mensajes.add(Mensaje("¡Hola! 👋 Soy tu asistente virtual. ¿En qué puedo ayudarte hoy?", false))
    }

    /**
     * Enviar mensaje al chatbot con IA
     */
    fun enviarMensaje(texto: String) {
        if (texto.isBlank()) return

        // Agregar mensaje del usuario
        mensajes.add(Mensaje(texto, true))

        // Verificar si hay usuario logueado
        val userId = usuarioId
        if (userId.isNullOrBlank()) {
            mensajes.add(Mensaje("Por favor inicia sesión para usar el chatbot.", false))
            return
        }

        isLoading.value = true
        error.value = null

        // Agregar mensaje de carga
        mensajes.add(Mensaje("⏳ Procesando tu pregunta... esto puede tardar unos momentos...", false))

        viewModelScope.launch {
            try {
                val result = chatbotRepository.enviarPregunta(userId, texto)

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
