package com.example.chatbot_diseo.data.remote

import com.example.chatbot_diseo.data.model.Conversacion
import com.example.chatbot_diseo.data.model.ConversacionCompleta
import com.example.chatbot_diseo.data.model.MensajeRequest
import retrofit2.Response
import retrofit2.http.*

/**
 * Servicio API para gestión de Conversaciones
 * Endpoints: /api/Conversacion
 */
interface ConversacionApiService {

    /**
     * Obtener todas las conversaciones
     * GET /api/Conversacion
     */
    @GET("api/Conversacion")
    suspend fun getAllConversaciones(): Response<List<ConversacionCompleta>>

    /**
     * Obtener conversación por ID
     * GET /api/Conversacion/{id}
     */
    @GET("api/Conversacion/{id}")
    suspend fun getConversacionById(
        @Path("id") id: String
    ): Response<ConversacionCompleta>

    /**
     * Obtener historial de conversaciones de un usuario
     * GET /api/Conversacion/usuario/{usuarioId}
     */
    @GET("api/Conversacion/usuario/{usuarioId}")
    suspend fun obtenerHistorial(
        @Path("usuarioId") usuarioId: String
    ): Response<List<Conversacion>>

    /**
     * Crear una nueva conversación
     * POST /api/Conversacion
     */
    @POST("api/Conversacion")
    suspend fun crearConversacion(
        @Body conversacion: Conversacion
    ): Response<Conversacion>

    /**
     * Actualizar una conversación existente
     * PUT /api/Conversacion/{id}
     */
    @PUT("api/Conversacion/{id}")
    suspend fun updateConversacion(
        @Path("id") id: String,
        @Body conversacion: ConversacionCompleta
    ): Response<Unit>

    /**
     * Eliminar una conversación
     * DELETE /api/Conversacion/{id}
     */
    @DELETE("api/Conversacion/{id}")
    suspend fun deleteConversacion(
        @Path("id") id: String
    ): Response<Unit>

    /**
     * Obtener conversaciones activas
     * GET /api/Conversacion/activas
     */
    @GET("api/Conversacion/activas")
    suspend fun getConversacionesActivas(): Response<List<ConversacionCompleta>>

    /**
     * Obtener conversaciones resueltas
     * GET /api/Conversacion/resueltas
     */
    @GET("api/Conversacion/resueltas")
    suspend fun getConversacionesResueltas(): Response<List<ConversacionCompleta>>

    /**
     * Enviar mensaje a una conversación específica
     * POST /api/Conversacion/{id}/mensajes
     */
    @POST("api/Conversacion/{id}/mensajes")
    suspend fun enviarMensaje(
        @Path("id") idConversacion: String,
        @Body mensaje: MensajeRequest
    ): Response<Any>
}