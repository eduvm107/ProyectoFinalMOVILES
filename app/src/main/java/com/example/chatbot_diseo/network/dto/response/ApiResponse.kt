package com.example.chatbot_diseo.network.dto.response

import com.google.gson.annotations.SerializedName

/**
 * Respuesta genérica de la API
 */
data class ApiResponse<T>(
    @SerializedName("success")
    val success: Boolean,

    @SerializedName("message")
    val message: String? = null,

    @SerializedName("data")
    val data: T? = null,

    @SerializedName("error")
    val error: String? = null
)

/**
 * Respuesta para operaciones de eliminación
 */
data class DeleteResponse(
    @SerializedName("success")
    val success: Boolean,

    @SerializedName("message")
    val message: String,

    @SerializedName("deletedId")
    val deletedId: String? = null
)

