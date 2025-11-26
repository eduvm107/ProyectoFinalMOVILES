package com.example.chatbot_diseo.network.dto.request

import com.google.gson.annotations.SerializedName

/**
 * Request para crear/actualizar un mensaje automático
 */
data class ContentRequest(
    @SerializedName("titulo")
    val titulo: String,

    @SerializedName("contenido")
    val contenido: String,

    @SerializedName("tipo")
    val tipo: String,

    @SerializedName("diaGatillo")
    val diaGatillo: Int? = null,

    @SerializedName("prioridad")
    val prioridad: String = "media",

    @SerializedName("canal")
    val canal: List<String> = listOf("chatbot"),

    @SerializedName("activo")
    val activo: Boolean = true,

    @SerializedName("segmento")
    val segmento: String = "todos",

    @SerializedName("horaEnvio")
    val horaEnvio: String = "09:00",

    @SerializedName("condicion")
    val condicion: String? = null,

    @SerializedName("creadoPor")
    val creadoPor: String = "admin"
)
