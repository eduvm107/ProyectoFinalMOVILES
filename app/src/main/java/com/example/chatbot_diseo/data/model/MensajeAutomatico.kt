package com.example.chatbot_diseo.data.model

import com.google.gson.annotations.SerializedName

data class MensajeAutomatico(
    @SerializedName("_id")
    val id: String,

    val titulo: String,
    val contenido: String,
    val tipo: String,
    val diaGatillo: Int,
    val prioridad: String,
    val canal: List<String>,
    val activo: Boolean,
    val segmento: String,
    val horaEnvio: String,


    val condicion: String? = null,

    val fechaCreacion: String,
    val creadoPor: String
)