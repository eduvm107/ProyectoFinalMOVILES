package com.example.chatbot_diseo.network.dto.response

import com.google.gson.annotations.SerializedName

/**
 * Response del toggle de favoritos
 */
data class FavoritoResponse(
    @SerializedName("success")
    val success: Boolean,

    @SerializedName("message")
    val message: String? = null,

    @SerializedName("esFavorito")
    val esFavorito: Boolean = false // true si se agregó, false si se eliminó
)

