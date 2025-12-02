package com.example.chatbot_diseo.network.api

import com.example.chatbot_diseo.network.dto.request.ActivityRequest
import com.example.chatbot_diseo.network.dto.request.ContentRequest
import com.example.chatbot_diseo.network.dto.request.ResourceRequest
import com.example.chatbot_diseo.network.dto.response.*
import retrofit2.Response
import retrofit2.http.*

/**
 * API Service para el panel de administración
 * Endpoints usando nombres en español del backend ASP.NET Core
 */
interface AdminApiService {

    // ============== MENSAJES AUTOMÁTICOS ==============

    /**
     * Obtener todos los mensajes automáticos
     * El backend devuelve: List<MensajeAutomatico>
     */
    @GET("mensajeautomatico")
    suspend fun getContents(): Response<List<ContentResponse>>

    @GET("mensajeautomatico/{id}")
    suspend fun getContentById(@Path("id") id: String): Response<ContentResponse>

    @POST("mensajeautomatico")
    suspend fun createContent(@Body request: ContentRequest): Response<ContentResponse>

    @PUT("mensajeautomatico/{id}")
    suspend fun updateContent(
        @Path("id") id: String,
        @Body request: ContentRequest
    ): Response<ContentResponse>

    @DELETE("mensajeautomatico/{id}")

    suspend fun deleteContent(@Path("id") id: String): Response<Unit>

    // ============== ACTIVIDADES ==============

    /**
     * Obtener todas las actividades
     * El backend devuelve: List<Actividad>
     */
    @GET("actividad")
    suspend fun getActivities(): Response<List<ActivityResponse>>

    @GET("actividad/{id}")
    suspend fun getActivityById(@Path("id") id: String): Response<ActivityResponse>

    @POST("actividad")
    suspend fun createActivity(@Body request: ActivityRequest): Response<ActivityResponse>

    @PUT("actividad/{id}")
    suspend fun updateActivity(
        @Path("id") id: String,
        @Body request: ActivityRequest
    ): Response<ActivityResponse>

    @DELETE("actividad/{id}")
    suspend fun deleteActivity(@Path("id") id: String): Response<Unit>

    // ============== DOCUMENTOS ==============

    /**
     * Obtener todos los documentos
     * El backend devuelve: List<Documento>
     */
    @GET("documento")
    suspend fun getResources(): Response<List<ResourceResponse>>

    @GET("documento/{id}")
    suspend fun getResourceById(@Path("id") id: String): Response<ResourceResponse>

    @POST("documento")
    suspend fun createResource(@Body request: ResourceRequest): Response<ResourceResponse>

    @PUT("documento/{id}")
    suspend fun updateResource(
        @Path("id") id: String,
        @Body request: ResourceRequest
    ): Response<ResourceResponse>

    @DELETE("documento/{id}")
    suspend fun deleteResource(@Path("id") id: String): Response<Unit>

    // ============== MÉTRICAS ==============

    @GET("metricas")
    suspend fun getMetrics(): Response<MetricsResponse>

    // ============== USUARIOS ==============

    /**
     * Obtener usuario completo por email
     * El backend devuelve: UsuarioCompleto
     */
    @GET("api/usuario/email/{email}")
    suspend fun getUsuarioByEmail(@Path("email") email: String): Response<UsuarioCompleto>
}
