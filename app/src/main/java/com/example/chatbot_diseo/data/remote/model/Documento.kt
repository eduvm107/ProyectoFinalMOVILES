package com.example.chatbot_diseo.data.remote.model

import com.google.gson.annotations.SerializedName

/**
 * Modelo de Documento según el backend MongoDB
 * Soporta tanto _id (MongoDB directo) como id (ASP.NET Core)
 */
data class Documento(
    @SerializedName(value = "id", alternate = ["_id"])
    val id: String? = null,

    @SerializedName("titulo")
    val titulo: String,

    @SerializedName("descripcion")
    val descripcion: String? = null,

    @SerializedName("url")
    val url: String,

    @SerializedName("categoria")
    val categoria: String? = null,

    @SerializedName("subcategoria")
    val subcategoria: String? = null,

    @SerializedName("tipo")
    val tipo: String? = null, // PDF, Formulario Web, Portal Web, etc.

    @SerializedName("tags")
    val tags: List<String>? = null,

    @SerializedName("icono")
    val icono: String? = null, // Emoji icon

    @SerializedName("idioma")
    val idioma: String? = null,

    @SerializedName("version")
    val version: String? = null,

    @SerializedName("publico")
    val publico: String? = null, // Target audience

    @SerializedName("obligatorio")
    val obligatorio: Boolean = false,

    @SerializedName("autor")
    val autor: String? = null,

    @SerializedName("valoracion")
    val valoracion: Int? = null,

    @SerializedName("fechaPublicacion")
    val fechaPublicacion: String? = null,

    @SerializedName("fechaActualizacion")
    val fechaActualizacion: String? = null,

    // ⭐ Campo favorito que viene directamente de MongoDB
    @SerializedName("favorito")
    val favorito: Boolean = false
)
