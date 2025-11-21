//esto es para historial

package com.example.chatbot_diseo.data.remote

import com.example.chatbot_diseo.data.model.Conversacion
import com.example.chatbot_diseo.data.model.MensajeRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ConversacionApiService {

    // 1. OBTENER HISTORIAL
    // Tu C# dice: [HttpGet("usuario/{usuarioId}")]
    // Por eso usamos @Path y la ruta 'api/Conversacion' (singular)
    @GET("api/Conversacion/usuario/{usuarioId}")
    suspend fun obtenerHistorial(
        @Path("usuarioId") usuarioId: String
    ): Response<List<Conversacion>>

    // 2. CREAR NUEVO CHAT
    // Tu C# dice: [HttpPost] y espera un [FromBody] Conversacion
    // Por eso usamos @Body
    @POST("api/Conversacion")
    suspend fun crearConversacion(
        @Body conversacion: Conversacion
    ): Response<Conversacion>

    // ... tus otros métodos GET y POST ...

    // 3. ENVIAR MENSAJE A UN CHAT ESPECÍFICO
    // Coincide con tu C#: [HttpPost("{id}/mensajes")]
    @POST("api/Conversacion/{id}/mensajes")
    suspend fun enviarMensaje(
        @Path("id") idConversacion: String,
        @Body mensaje: MensajeRequest
    ): Response<Any> // Usamos Any porque no nos importa mucho qué devuelve, solo si es 200 OK
}