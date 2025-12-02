package com.example.chatbot_diseo.data.remote.apiChatBot

import com.example.chatbot_diseo.data.remote.model.Actividad
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

typealias ActividadRemota = Actividad.Actividad_Calendario

/**
 * Servicio API para gestión de Actividades
 * Endpoints: /api/Actividad
 */
interface ApiActividadService {

    /**
     * Obtener todas las actividades
     * GET /api/Actividad
     */
    @GET("Actividad")
    suspend fun getAllActividades(): List<ActividadRemota>

    /**
     * Obtener actividad por ID
     * GET /api/Actividad/{id}
     */
    @GET("Actividad/{id}")
    suspend fun getActividadById(
        @Path("id") id: String
    ): ActividadRemota

    /**
     * Crear una nueva actividad
     * POST /api/Actividad
     */
    @POST("Actividad")
    suspend fun createActividad(
        @Body actividad: ActividadRemota
    ): ActividadRemota

    /**
     * Actualizar una actividad existente
     * PUT /api/Actividad/{id}
     */
    @PUT("Actividad/{id}")
    suspend fun updateActividad(
        @Path("id") id: String,
        @Body actividad: ActividadRemota
    )

    /**
     * Eliminar una actividad
     * DELETE /api/Actividad/{id}
     */
    @DELETE("Actividad/{id}")
    suspend fun deleteActividad(
        @Path("id") id: String
    )

    /**
     * Obtener actividades por día
     * GET /api/Actividad/dia/{dia}
     */
    @GET("Actividad/dia/{dia}")
    suspend fun getByDia(
        @Path("dia") dia: Int
    ): List<ActividadRemota>

    /**
     * Obtener actividades por tipo
     * GET /api/Actividad/tipo/{tipo}
     */
    @GET("Actividad/tipo/{tipo}")
    suspend fun getByTipo(
        @Path("tipo") tipo: String
    ): List<ActividadRemota>

    /**
     * Obtener actividades obligatorias
     * GET /api/Actividad/obligatorias
     */
    @GET("Actividad/obligatorias")
    suspend fun getObligatorias(): List<ActividadRemota>

    /**
     * Obtener actividades asignadas a un usuario
     * GET /api/Actividad/usuario/{usuarioId}
     */
    @GET("Actividad/usuario/{usuarioId}")
    suspend fun getByUsuario(
        @Path("usuarioId") usuarioId: String
    ): List<ActividadRemota>
}
