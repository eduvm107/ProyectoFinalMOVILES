package com.example.chatbot_diseo.data.remote

import com.example.chatbot_diseo.data.model.MensajeAutomatico
import retrofit2.Response
import retrofit2.http.*

/**
 * Servicio API para gestión de Mensajes Automáticos
 * Endpoints: /api/MensajeAutomatico
 */
interface MensajeApiService {

    /**
     * Obtener todos los mensajes automáticos
     * GET /api/MensajeAutomatico
     */
    @GET("MensajeAutomatico")
    suspend fun getMensajes(): Response<List<MensajeAutomatico>>

    /**
     * Obtener mensaje automático por ID
     * GET /api/MensajeAutomatico/{id}
     */
    @GET("MensajeAutomatico/{id}")
    suspend fun getMensajeById(
        @Path("id") id: String
    ): Response<MensajeAutomatico>

    /**
     * Crear un nuevo mensaje automático
     * POST /api/MensajeAutomatico
     */
    @POST("MensajeAutomatico")
    suspend fun createMensaje(
        @Body mensaje: MensajeAutomatico
    ): Response<MensajeAutomatico>

    /**
     * Actualizar un mensaje automático existente
     * PUT /api/MensajeAutomatico/{id}
     */
    @PUT("MensajeAutomatico/{id}")
    suspend fun updateMensaje(
        @Path("id") id: String,
        @Body mensaje: MensajeAutomatico
    ): Response<Unit>

    /**
     * Eliminar un mensaje automático
     * DELETE /api/MensajeAutomatico/{id}
     */
    @DELETE("MensajeAutomatico/{id}")
    suspend fun deleteMensaje(
        @Path("id") id: String
    ): Response<Unit>

    /**
     * Obtener mensajes automáticos por tipo
     * GET /api/MensajeAutomatico/tipo/{tipo}
     */
    @GET("MensajeAutomatico/tipo/{tipo}")
    suspend fun getMensajesByTipo(
        @Path("tipo") tipo: String
    ): Response<List<MensajeAutomatico>>

    /**
     * Obtener mensajes automáticos activos
     * GET /api/MensajeAutomatico/activos
     */
    @GET("MensajeAutomatico/activos")
    suspend fun getMensajesActivos(): Response<List<MensajeAutomatico>>
}