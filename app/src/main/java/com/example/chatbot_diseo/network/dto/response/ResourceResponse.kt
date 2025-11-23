package com.example.chatbot_diseo.network.dto.response

import com.google.gson.annotations.SerializedName

/**
 * DTO para Documentos desde MongoDB
 * Mapea exactamente el modelo Documento del backend ASP.NET Core
 */
data class ResourceResponse(
    @SerializedName("id")
    val id: String? = null,

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
    val icono: String = "",

    @SerializedName("tamaño")
    val tamaño: String? = null,

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
    val valoracion: Int = 0
)

/**
 * Respuesta de lista de recursos/documentos
 */
data class ResourceListResponse(
    @SerializedName("data")
    val data: List<ResourceResponse>? = null,

    @SerializedName("items")
    val items: List<ResourceResponse>? = null,

    @SerializedName("documentos")
    val documentos: List<ResourceResponse>? = null,

    @SerializedName("total")
    val total: Int? = null,

    @SerializedName("message")
    val message: String? = null
) {
    fun getList(): List<ResourceResponse> {
        return data ?: items ?: documentos ?: emptyList()
    }
}
