package com.example.chatbot_diseo.presentation.chat

import android.util.Log
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

    // ID del usuario actual (se obtiene del login)
    var usuarioId: String? = null

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

        viewModelScope.launch {
            try {
                Log.d("ChatViewModel", "Enviando pregunta al chatbot: $texto")

                val result = chatbotRepository.enviarPregunta(userId, texto)

                result.onSuccess { response ->
                    Log.d("ChatViewModel", "Respuesta del chatbot: ${response.respuesta}")
                    // Guardar el ID de la conversación
                    response.conversacionId?.let { conversacionId = it }
                    // Agregar respuesta del bot
                    mensajes.add(Mensaje(response.respuesta, false))
                }.onFailure { e ->
                    Log.e("ChatViewModel", "Error del chatbot", e)
                    error.value = e.message
                    mensajes.add(Mensaje("Lo siento, ocurrió un error: ${e.message}", false))
                }
            } catch (e: Exception) {
                Log.e("ChatViewModel", "Exception en enviarMensaje", e)
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
                Log.d("ChatViewModel", "Satisfacción registrada: $satisfaccion")
            } catch (e: Exception) {
                Log.e("ChatViewModel", "Error registrando satisfacción", e)
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
                val result = chatbotRepository.healthCheck()
                result.onSuccess { health ->
                    Log.d("ChatViewModel", "Estado chatbot: ${health.estado}, Ollama: ${health.ollama?.disponible}")
                }.onFailure { e ->
                    Log.e("ChatViewModel", "Error verificando estado", e)
                }
            } catch (e: Exception) {
                Log.e("ChatViewModel", "Exception en healthCheck", e)
            }
        }
    }
}
