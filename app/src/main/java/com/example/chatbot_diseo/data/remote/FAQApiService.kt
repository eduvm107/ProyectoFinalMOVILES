package com.example.chatbot_diseo.data.remote

import com.example.chatbot_diseo.data.model.FAQ
import com.example.chatbot_diseo.data.model.FAQRequest
import retrofit2.Response
import retrofit2.http.*

/**
 * Servicio API para gestión de FAQs (Preguntas Frecuentes)
 * Endpoints: /api/FAQ
 */
interface FAQApiService {

    /**
     * Obtener todas las FAQs
     * GET /api/FAQ
     */
    @GET("FAQ")
    suspend fun getAllFAQs(): Response<List<FAQ>>

    /**
     * Obtener FAQ por ID
     * GET /api/FAQ/{id}
     */
    @GET("FAQ/{id}")
    suspend fun getFAQById(
        @Path("id") id: String
    ): Response<FAQ>

    /**
     * Crear una nueva FAQ
     * POST /api/FAQ
     */
    @POST("FAQ")
    suspend fun createFAQ(
        @Body faq: FAQRequest
    ): Response<FAQ>

    /**
     * Actualizar una FAQ existente
     * PUT /api/FAQ/{id}
     */
    @PUT("FAQ/{id}")
    suspend fun updateFAQ(
        @Path("id") id: String,
        @Body faq: FAQRequest
    ): Response<Unit>

    /**
     * Eliminar una FAQ
     * DELETE /api/FAQ/{id}
     */
    @DELETE("FAQ/{id}")
    suspend fun deleteFAQ(
        @Path("id") id: String
    ): Response<Unit>

    /**
     * Buscar FAQs por texto
     * GET /api/FAQ/search?query={text}
     */
    @GET("FAQ/search")
    suspend fun searchFAQs(
        @Query("query") query: String
    ): Response<List<FAQ>>
}
