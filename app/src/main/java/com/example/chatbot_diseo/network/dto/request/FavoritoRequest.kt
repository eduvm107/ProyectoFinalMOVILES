package com.example.chatbot_diseo.network.dto.request

import com.google.gson.annotations.SerializedName

/**
 * Request para agregar/eliminar favoritos
 * POST /api/Usuario/{usuarioId}/favoritos
 */
data class FavoritoRequest(
    @SerializedName("tipoRecurso")
    val tipoRecurso: String, // "documento", "actividad", etc.

    @SerializedName("recursoId")
    val recursoId: String // ID del recurso en MongoDB
)

