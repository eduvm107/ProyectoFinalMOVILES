package com.example.chatbot_diseo.data.remote

import com.example.chatbot_diseo.data.model.UsuarioCompleto
import com.example.chatbot_diseo.data.model.UsuarioRequest
import com.example.chatbot_diseo.data.model.RecursoFavorito
import com.example.chatbot_diseo.data.model.FavoritoRequest
import retrofit2.Response
import retrofit2.http.*

/**
 * Servicio API para gestión de Usuarios
 * Endpoints: /api/Usuario
 */
interface UsuarioApiService {

    /**
     * Obtener todos los usuarios
     * GET /api/Usuario
     */
    @GET("Usuario")
    suspend fun getAllUsuarios(): Response<List<UsuarioCompleto>>

    /**
     * Obtener usuario por ID
     * GET /api/Usuario/{id}
     */
    @GET("Usuario/{id}")
    suspend fun getUsuarioById(
        @Path("id") id: String
    ): Response<UsuarioCompleto>

    /**
     * Crear un nuevo usuario
     * POST /api/Usuario
     */
    @POST("Usuario")
    suspend fun createUsuario(
        @Body usuario: UsuarioRequest
    ): Response<UsuarioCompleto>

    /**
     * Actualizar un usuario existente
     * PUT /api/Usuario/{id}
     */
    @PUT("Usuario/{id}")
    suspend fun updateUsuario(
        @Path("id") id: String,
        @Body usuario: UsuarioRequest
    ): Response<Unit>

    /**
     * Eliminar un usuario
     * DELETE /api/Usuario/{id}
     */
    @DELETE("Usuario/{id}")
    suspend fun deleteUsuario(
        @Path("id") id: String
    ): Response<Unit>

    /**
     * Buscar usuario por email
     * GET /api/Usuario/email/{email}
     */
    @GET("Usuario/email/{email}")
    suspend fun getUsuarioByEmail(
        @Path("email") email: String
    ): Response<UsuarioCompleto>

    /**
     * Buscar usuario por DNI
     * GET /api/Usuario/dni/{dni}
     */
    @GET("Usuario/dni/{dni}")
    suspend fun getUsuarioByDni(
        @Path("dni") dni: String
    ): Response<UsuarioCompleto>

    /**
     * Obtener usuarios por estado de onboarding
     * GET /api/Usuario/onboarding/{estado}
     */
    @GET("Usuario/onboarding/{estado}")
    suspend fun getUsuariosByOnboarding(
        @Path("estado") estado: String
    ): Response<List<UsuarioCompleto>>

    /**
     * Obtener todos los usuarios activos
     * GET /api/Usuario/activos
     */
    @GET("Usuario/activos")
    suspend fun getUsuariosActivos(): Response<List<UsuarioCompleto>>

    /**
     * Obtener usuarios por departamento
     * GET /api/Usuario/departamento/{departamento}
     */
    @GET("Usuario/departamento/{departamento}")
    suspend fun getUsuariosByDepartamento(
        @Path("departamento") departamento: String
    ): Response<List<UsuarioCompleto>>

    // --- Nuevos endpoints para Favoritos ---

    /**
     * Obtener los favoritos de un usuario
     * GET /api/Usuario/{id}/favoritos
     */
    @GET("Usuario/{id}/favoritos")
    suspend fun getFavoritosByUsuario(@Path("id") id: String): Response<List<RecursoFavorito>>

    /**
     * Toggle (marcar/desmarcar) favorito para un usuario
     * POST /api/Usuario/{id}/favoritos
     */
    @POST("Usuario/{id}/favoritos")
    suspend fun toggleFavorito(
        @Path("id") id: String,
        @Body request: FavoritoRequest
    ): Response<Unit>
}
