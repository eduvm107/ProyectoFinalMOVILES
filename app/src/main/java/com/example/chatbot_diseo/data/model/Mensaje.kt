//recien creado
package com.example.chatbot_diseo.data.model

import com.google.gson.annotations.SerializedName


data class Mensaje(
    @SerializedName("tipo")
    val tipo: String, // "usuario" o "bot"
    @SerializedName("contenido")
    val contenido: String,
    @SerializedName("timestamp")
    val timestamp: String, // Usaremos String por ahora para la fecha/hora

    // Campos adicionales que vienen desde la API y pueden ser nulos
    @SerializedName("intencionDetectada")
    val intencionDetectada: String? = null,

    @SerializedName("fuentesUsadas")
    val fuentesUsadas: List<String>? = null
)