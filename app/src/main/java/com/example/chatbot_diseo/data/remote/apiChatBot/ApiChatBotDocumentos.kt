package com.example.chatbot_diseo.data.remote.apiChatBot

import com.example.chatbot_diseo.data.remote.model.Documento
import retrofit2.http.*

/**
 * Servicio API para gestión de Documentos
 * Endpoints: /api/Documento
 */
interface ApiChatBotDocumentos {

    /**
     * Obtener todos los documentos
     * GET /api/Documento
     */
    @GET("Documento")
    suspend fun getAllDocumentos(): List<Documento>

    /**
     * Obtener documento por ID
     * GET /api/Documento/{id}
     */
    @GET("Documento/{id}")
    suspend fun getDocumentoById(
        @Path("id") id: String
    ): Documento

    /**
     * Crear un nuevo documento
     * POST /api/Documento
     */
    @POST("Documento")
    suspend fun createDocumento(
        @Body documento: Documento
    ): Documento

    /**
     * Actualizar un documento existente
     * PUT /api/Documento/{id}
     */
    @PUT("Documento/{id}")
    suspend fun updateDocumento(
        @Path("id") id: String,
        @Body documento: Documento
    )

    /**
     * Eliminar un documento
     * DELETE /api/Documento/{id}
     */
    @DELETE("Documento/{id}")
    suspend fun deleteDocumento(
        @Path("id") id: String
    )

    /**
     * Obtener documentos por categoría
     * GET /api/Documento/categoria/{categoria}
     */
    @GET("Documento/categoria/{categoria}")
    suspend fun getByCategoria(
        @Path("categoria") categoria: String
    ): List<Documento>

    /**
     * Obtener documentos por tipo
     * GET /api/Documento/tipo/{tipo}
     */
    @GET("Documento/tipo/{tipo}")
    suspend fun getByTipo(
        @Path("tipo") tipo: String
    ): List<Documento>

    /**
     * Buscar documentos por tag
     * GET /api/Documento/tag/{tag}
     */
    @GET("Documento/tag/{tag}")
    suspend fun searchByTag(
        @Path("tag") tag: String
    ): List<Documento>

    /**
     * Obtener documentos personalizados para un usuario
     * GET /api/Documento/personalizado/{usuarioId}
     * Este endpoint debería devolver la lista de documentos con el campo isFavorite inyectado
     */
    @GET("Documento/personalizado/{usuarioId}")
    suspend fun getDocumentosPersonalizados(
        @Path("usuarioId") usuarioId: String
    ): List<Documento>
}
