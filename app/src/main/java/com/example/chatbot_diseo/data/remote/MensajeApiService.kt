package com.example.chatbot_diseo.data.remote

import com.example.chatbot_diseo.data.model.MensajeAutomatico
import retrofit2.Response
import retrofit2.http.GET

interface MensajeApiService {
    // Devuelve la respuesta completa (con código de estado)
    @GET("api/MensajeAutomatico")
    suspend fun getMensajes(): Response<List<MensajeAutomatico>>
}