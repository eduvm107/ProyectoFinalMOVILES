package com.example.chatbot_diseo.data.remote.apiChatBot

import com.example.chatbot_diseo.data.remote.model.Documento
import retrofit2.http.*

interface ApiChatBotDocumentos {

    // GET: Obtener todos
    @GET("Documento")
    suspend fun getAllDocumentos(): List<Documento>

    // GET: Obtener por ID
    @GET("Documento/{id}")
    suspend fun getDocumentoById(
        @Path("id") id: String
    ): Documento

    // POST: Crear documento
    @POST("Documento")
    suspend fun createDocumento(
        @Body documento: Documento
    ): Documento

    // PUT: Actualizar
    @PUT("Documento/{id}")
    suspend fun updateDocumento(
        @Path("id") id: String,
        @Body documento: Documento
    )

    // DELETE: Eliminar
    @DELETE("Documento/{id}")
    suspend fun deleteDocumento(
        @Path("id") id: String
    )

    // GET: Buscar por categoría
    @GET("Documento/categoria/{categoria}")
    suspend fun getByCategoria(
        @Path("categoria") categoria: String
    ): List<Documento>

    // GET: Buscar por tipo
    @GET("Documento/tipo/{tipo}")
    suspend fun getByTipo(
        @Path("tipo") tipo: String
    ): List<Documento>

    // GET: Buscar por tag
    @GET("Documento/tag/{tag}")
    suspend fun searchByTag(
        @Path("tag") tag: String
    ): List<Documento>
}
