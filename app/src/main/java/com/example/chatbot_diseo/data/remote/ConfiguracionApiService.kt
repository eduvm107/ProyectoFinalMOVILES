package com.example.chatbot_diseo.data.remote

import com.example.chatbot_diseo.data.model.Configuracion
import com.example.chatbot_diseo.data.model.ConfiguracionRequest
import retrofit2.Response
import retrofit2.http.*

/**
 * Servicio API para gestión de Configuraciones
 * Endpoints: /api/Configuracion
 */
interface ConfiguracionApiService {

    /**
     * Obtener todas las configuraciones
     * GET /api/Configuracion
     */
    @GET("Configuracion")
    suspend fun getAllConfiguraciones(): Response<List<Configuracion>>

    /**
     * Obtener configuración por ID
     * GET /api/Configuracion/{id}
     */
    @GET("Configuracion/{id}")
    suspend fun getConfiguracionById(
        @Path("id") id: String
    ): Response<Configuracion>

    /**
     * Crear una nueva configuración
     * POST /api/Configuracion
     */
    @POST("Configuracion")
    suspend fun createConfiguracion(
        @Body configuracion: ConfiguracionRequest
    ): Response<Configuracion>

    /**
     * Actualizar una configuración existente
     * PUT /api/Configuracion/{id}
     */
    @PUT("Configuracion/{id}")
    suspend fun updateConfiguracion(
        @Path("id") id: String,
        @Body configuracion: ConfiguracionRequest
    ): Response<Unit>

    /**
     * Eliminar una configuración
     * DELETE /api/Configuracion/{id}
     */
    @DELETE("Configuracion/{id}")
    suspend fun deleteConfiguracion(
        @Path("id") id: String
    ): Response<Unit>

    /**
     * Obtener configuraciones por tipo
     * GET /api/Configuracion/tipo/{tipo}
     */
    @GET("Configuracion/tipo/{tipo}")
    suspend fun getConfiguracionesByTipo(
        @Path("tipo") tipo: String
    ): Response<List<Configuracion>>

    /**
     * Obtener todas las configuraciones activas
     * GET /api/Configuracion/activas
     */
    @GET("Configuracion/activas")
    suspend fun getConfiguracionesActivas(): Response<List<Configuracion>>

    /**
     * Buscar configuración por nombre
     * GET /api/Configuracion/nombre/{nombre}
     */
    @GET("Configuracion/nombre/{nombre}")
    suspend fun getConfiguracionByNombre(
        @Path("nombre") nombre: String
    ): Response<Configuracion>
}
