package com.example.chatbot_diseo.data.remote

import com.example.chatbot_diseo.data.model.*
import retrofit2.Response
import retrofit2.http.*

/**
 * Servicio API para el Chatbot con IA (Ollama)
 * Endpoints: /api/Chatbot
 */
interface ChatbotApiService {

    /**
     * Enviar una pregunta al chatbot y obtener respuesta generada por IA
     * POST /api/Chatbot/ask
     */
    @POST("Chatbot/ask")
    suspend fun ask(
        @Body request: ChatbotAskRequest
    ): Response<ChatbotAskResponse>

    /**
     * Obtener historial de conversaciones de un usuario
     * GET /api/Chatbot/historial/{usuarioId}
     */
    @GET("Chatbot/historial/{usuarioId}")
    suspend fun getHistorial(
        @Path("usuarioId") usuarioId: String
    ): Response<ChatbotHistorialResponse>

    /**
     * Registrar satisfacción del usuario con una respuesta
     * PUT /api/Chatbot/satisfaccion/{conversacionId}
     */
    @PUT("Chatbot/satisfaccion/{conversacionId}")
    suspend fun registrarSatisfaccion(
        @Path("conversacionId") conversacionId: String,
        @Body request: SatisfaccionRequest
    ): Response<SatisfaccionResponse>

    /**
     * Obtener estadísticas del chatbot
     * GET /api/Chatbot/estadisticas
     */
    @GET("Chatbot/estadisticas")
    suspend fun getEstadisticas(): Response<ChatbotEstadisticasResponse>

    /**
     * Verificar el estado del servicio Ollama y del chatbot
     * GET /api/Chatbot/health
     */
    @GET("Chatbot/health")
    suspend fun healthCheck(): Response<ChatbotHealthResponse>
}
