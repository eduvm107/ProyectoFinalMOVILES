package com.example.chatbot_diseo.data.model

import com.google.gson.annotations.SerializedName

/**
 * Modelo unificado para recursos favoritos
 * Puede representar: documentos, actividades o chats
 * Soporta tanto _id (MongoDB directo) como id (ASP.NET Core)
 */
data class RecursoFavorito(
    @SerializedName(value = "id", alternate = ["_id"])
    val id: String,

    @SerializedName("tipo")
    val tipo: String, // "documento", "actividad", "chat"

    @SerializedName("titulo")
    val titulo: String,

    @SerializedName("descripcion")
    val descripcion: String? = null,

    @SerializedName("url")
    val url: String? = null,

    @SerializedName("categoria")
    val categoria: String? = null,

    @SerializedName("subcategoria")
    val subcategoria: String? = null,

    @SerializedName("tags")
    val tags: List<String>? = null,

    @SerializedName("icono")
    val icono: String? = null,

    @SerializedName("fecha")
    val fecha: String? = null,

    @SerializedName("fechaInicio")
    val fechaInicio: String? = null,

    @SerializedName("fechaFin")
    val fechaFin: String? = null,

    // Campos adicionales según el tipo
    @SerializedName("ubicacion")
    val ubicacion: String? = null,

    @SerializedName("modalidad")
    val modalidad: String? = null
)