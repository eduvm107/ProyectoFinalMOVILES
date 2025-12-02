package com.example.chatbot_diseo.network.api

import com.example.chatbot_diseo.network.dto.request.ActivityRequest
import com.example.chatbot_diseo.network.dto.request.ContentRequest
import com.example.chatbot_diseo.network.dto.request.ResourceRequest
import com.example.chatbot_diseo.network.dto.response.*
import retrofit2.Response
import retrofit2.http.*

/**
 * API Service para el panel de administración
 * Endpoints usando nombres en PascalCase del backend ASP.NET Core
 */
interface AdminApiService {

    // ============== MENSAJES AUTOMÁTICOS ==============

    /**
     * Obtener todos los mensajes automáticos
     * El backend devuelve: List<MensajeAutomatico>
     */
    @GET("MensajeAutomatico")
    suspend fun getContents(): Response<List<ContentResponse>>

    @GET("MensajeAutomatico/{id}")
    suspend fun getContentById(@Path("id") id: String): Response<ContentResponse>

    @POST("MensajeAutomatico")
    suspend fun createContent(@Body request: ContentRequest): Response<ContentResponse>

    @PUT("MensajeAutomatico/{id}")
    suspend fun updateContent(
        @Path("id") id: String,
        @Body request: ContentRequest
    ): Response<ContentResponse>

    @DELETE("MensajeAutomatico/{id}")
    suspend fun deleteContent(@Path("id") id: String): Response<Unit>

    // ============== ACTIVIDADES ==============

    /**
     * Obtener todas las actividades
     * El backend devuelve: List<Actividad>
     */
    @GET("Actividad")
    suspend fun getActivities(): Response<List<ActivityResponse>>

    @GET("Actividad/{id}")
    suspend fun getActivityById(@Path("id") id: String): Response<ActivityResponse>

    @POST("Actividad")
    suspend fun createActivity(@Body request: ActivityRequest): Response<ActivityResponse>

    @PUT("Actividad/{id}")
    suspend fun updateActivity(
        @Path("id") id: String,
        @Body request: ActivityRequest
    ): Response<ActivityResponse>

    @DELETE("Actividad/{id}")
    suspend fun deleteActivity(@Path("id") id: String): Response<Unit>

    // ============== DOCUMENTOS ==============

    /**
     * Obtener todos los documentos
     * El backend devuelve: List<Documento>
     */
    @GET("Documento")
    suspend fun getResources(): Response<List<ResourceResponse>>

    @GET("Documento/{id}")
    suspend fun getResourceById(@Path("id") id: String): Response<ResourceResponse>

    @POST("Documento")
    suspend fun createResource(@Body request: ResourceRequest): Response<ResourceResponse>

    @PUT("Documento/{id}")
    suspend fun updateResource(
        @Path("id") id: String,
        @Body request: ResourceRequest
    ): Response<ResourceResponse>

    @DELETE("Documento/{id}")
    suspend fun deleteResource(@Path("id") id: String): Response<Unit>

    // ============== MÉTRICAS ==============

    @GET("Metricas")
    suspend fun getMetrics(): Response<MetricsResponse>

    // ============== USUARIOS ==============

    /**
     * Obtener usuario completo por email
     * El backend devuelve: UsuarioCompleto
     */
    @GET("api/usuario/email/{email}")
    suspend fun getUsuarioByEmail(@Path("email") email: String): Response<UsuarioCompleto>
}
