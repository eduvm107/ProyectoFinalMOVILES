package com.example.chatbot_diseo.data.remote

import com.example.chatbot_diseo.data.model.ChatbotAskResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Servicio API para el Chat Orquestador
 * Endpoint: /api/ChatOrquestador/preguntar
 */
interface ChatOrquestadorApiService {

    /**
     * Enviar una pregunta al orquestador de chat
     * GET /api/ChatOrquestador/preguntar?pregunta=...&usuarioId=...
     */
    @GET("ChatOrquestador/preguntar")
    suspend fun preguntar(
        @Query("pregunta") pregunta: String,
        @Query("usuarioId") usuarioId: String
    ): Response<ChatbotAskResponse>
}

