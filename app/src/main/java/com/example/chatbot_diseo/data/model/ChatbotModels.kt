package com.example.chatbot_diseo.data.model

import com.google.gson.annotations.SerializedName

// Request para enviar pregunta al chatbot
data class ChatbotAskRequest(
    val usuarioId: String,
    val pregunta: String
)

// Response del chatbot
data class ChatbotAskResponse(
    val respuesta: String,
    val contextoUtilizado: Boolean? = null,
    val fechaRespuesta: String? = null,
    val conversacionId: String? = null
)

// Response del historial del chatbot
data class ChatbotHistorialResponse(
    val total: Int,
    val conversaciones: List<ConversacionCompleta>
)

// Conversación completa con mensajes
data class ConversacionCompleta(
    @SerializedName("_id")
    val id: String? = null,
    val usuarioId: String,
    val mensajes: List<MensajeChat>? = null,
    val fechaInicio: String? = null,
    val fechaUltimaMensaje: String? = null,
    val activa: Boolean = true,
    val satisfaccion: Int? = null,
    val resuelto: Boolean = false,
    val favorito: Boolean? = null // Nuevo campo: favorito (opcional) para indicar si la conversación está marcada como favorita
)

// Mensaje dentro de una conversación
data class MensajeChat(
    val tipo: String, // "usuario" o "bot"
    val contenido: String,
    val timestamp: String? = null,
    val faqRelacionada: String? = null
)

// Request para registrar satisfacción
data class SatisfaccionRequest(
    val satisfaccion: Int // 1-5
)

// Response de satisfacción
data class SatisfaccionResponse(
    val mensaje: String
)

// Response de estadísticas del chatbot
data class ChatbotEstadisticasResponse(
    val totalConversaciones: Int,
    val promedioSatisfaccion: Double? = null,
    val totalFAQs: Int,
    val estadoOllama: OllamaStatus? = null
)

// Estado del servicio Ollama
data class OllamaStatus(
    val modelo: String,
    val disponible: Boolean
)

// Response de health check
data class ChatbotHealthResponse(
    val estado: String,
    val ollama: OllamaStatus? = null,
    val timestamp: String? = null
)
