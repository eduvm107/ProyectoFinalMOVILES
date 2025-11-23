package com.example.chatbot_diseo.data.remote.model

import com.google.gson.annotations.SerializedName

/**
 * Modelo de Documento según el backend
 */
data class Documento(
    @SerializedName("_id")
    val id: String? = null,
    val titulo: String,
    val descripcion: String? = null,
    val url: String,
    val categoria: String? = null,
    val subcategoria: String? = null,
    val tipo: String? = null, // PDF, Formulario Web, Portal Web, etc.
    val tags: List<String>? = null,
    val icono: String? = null, // Emoji icon
    @SerializedName("tamaño")
    val tamano: String? = null,
    val idioma: String? = null,
    val version: String? = null,
    val publico: String? = null, // Target audience
    val obligatorio: Boolean = false,
    val autor: String? = null,
    val descargas: Int? = null,
    val accesos: Int? = null,
    val valoracion: Double? = null,
    val fechaCreacion: String? = null,
    val fechaModificacion: String? = null
)
