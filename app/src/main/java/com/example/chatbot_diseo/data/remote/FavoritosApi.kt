package com.example.chatbot_diseo.data.remote

import com.example.chatbot_diseo.data.model.RecursoFavorito
import com.example.chatbot_diseo.network.dto.request.FavoritoRequest
import com.example.chatbot_diseo.network.dto.response.FavoritoResponse
import retrofit2.Response
import retrofit2.http.*

/**
 * API Service para manejo de favoritos
 */
interface FavoritosApi {

    /**
     * Obtener lista de favoritos del usuario
     * GET /api/Usuario/{usuarioId}/favoritos
     */
    @GET("Usuario/{usuarioId}/favoritos")
    suspend fun getMisFavoritos(
        @Path("usuarioId") usuarioId: String
    ): Response<List<RecursoFavorito>>

    /**
     * Toggle favorito (agregar o eliminar)
     * POST /api/Usuario/{usuarioId}/favoritos
     */
    @POST("Usuario/{usuarioId}/favoritos")
    suspend fun toggleFavorito(
        @Path("usuarioId") usuarioId: String,
        @Body request: FavoritoRequest
    ): Response<FavoritoResponse>
}
