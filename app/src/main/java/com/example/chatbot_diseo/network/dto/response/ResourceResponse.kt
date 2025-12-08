package com.example.chatbot_diseo.network.dto.response

import com.google.gson.annotations.SerializedName

/**
 * DTO para Documentos. Mapeo corregido para manejar _id y Null-Safety.
 */
data class ResourceResponse(
    // 1. CORRECCIÓN CRÍTICA DEL ID
    @SerializedName("id", alternate = ["_id"])
    val id: String? = null,

    // 2. Seguridad: Agregar valores por defecto para campos obligatorios
    @SerializedName("titulo")
    val titulo: String = "", // Evita NPE si es nulo/vacío

    @SerializedName("descripcion")
    val descripcion: String = "",

    @SerializedName("url")
    val url: String = "",

    @SerializedName("tipo")
    val tipo: String = "",

    @SerializedName("categoria")
    val categoria: String = "",

    @SerializedName("subcategoria")
    val subcategoria: String = "",

    @SerializedName("tags")
    val tags: List<String> = emptyList(),

    @SerializedName("icono")
    val icono: String = "",

    @SerializedName("tamaño")
    val tamaño: String? = null, // Acepta nulo

    @SerializedName("idioma")
    val idioma: String = "",

    @SerializedName("version")
    val version: String = "",

    @SerializedName("publico")
    val publico: String = "",

    @SerializedName("obligatorio")
    val obligatorio: Boolean = false,

    @SerializedName("fechaPublicacion")
    val fechaPublicacion: String? = null,

    @SerializedName("fechaActualizacion")
    val fechaActualizacion: String? = null,

    @SerializedName("autor")
    val autor: String = "",

    @SerializedName("descargas")
    val descargas: Int? = null,

    @SerializedName("accesos")
    val accesos: Int? = null,

    @SerializedName("valoracion")
    val valoracion: Int = 0,

    // ⭐ CAMPO ESENCIAL DEL CORAZÓN (Asumiendo que viene en el GET, si no, se queda en false)
    @SerializedName("favorito")
    val favorito: Boolean = false
)

// Las clases ResourceListResponse y FavoritoRequest no necesitan cambios si el mapeo anterior funciona.