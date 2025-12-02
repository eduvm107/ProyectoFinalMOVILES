package com.example.chatbot_diseo.network.dto.request

import com.google.gson.annotations.SerializedName

/**
 * Request para crear/actualizar un documento
 * Incluye TODOS los campos requeridos por el backend
 */
data class ResourceRequest(
    @SerializedName("titulo")
    val titulo: String,

    @SerializedName("descripcion")
    val descripcion: String,

    @SerializedName("url")
    val url: String,

    @SerializedName("tipo")
    val tipo: String,

    @SerializedName("categoria")
    val categoria: String,

    @SerializedName("subcategoria")
    val subcategoria: String = "",

    @SerializedName("tags")
    val tags: List<String> = emptyList(),

    @SerializedName("icono")
    val icono: String = "📄",

    @SerializedName("tamaño")
    val tamaño: String? = null,

    @SerializedName("idioma")
    val idioma: String = "Español",

    @SerializedName("version")
    val version: String = "1.0",

    @SerializedName("publico")
    val publico: String = "Nuevos empleados",

    @SerializedName("obligatorio")
    val obligatorio: Boolean = false,

    @SerializedName("autor")
    val autor: String = "Administrador",

    @SerializedName("valoracion")
    val valoracion: Int = 0,

    @SerializedName("favorito")
    val favorito: Boolean = false,

    // ⭐ Opcionales pero recomendados para consistencia
    @SerializedName("fechaPublicacion")
    val fechaPublicacion: String? = null,

    @SerializedName("fechaActualizacion")
    val fechaActualizacion: String? = null,

    @SerializedName("descargas")
    val descargas: Int? = null,

    @SerializedName("accesos")
    val accesos: Int? = null


)
