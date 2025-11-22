package com.example.chatbot_diseo.data.model

import com.google.gson.annotations.SerializedName

data class Conversacion(
    @SerializedName("_id")
    val id: String? = null, // Puede ser null al crear uno nuevo

    val usuarioId: String,

    // Valores por defecto para que no fallen al enviar
    val fechaInicio: String = "",
    val fechaUltimaMensaje: String = "",
    val activa: Boolean = true,
    val resuelto: Boolean = false
)