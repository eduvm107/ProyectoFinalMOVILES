package com.example.chatbot_diseo.network.dto.response

import com.google.gson.annotations.SerializedName

/**
 * DTO para Mensajes Automáticos desde MongoDB
 * Mapea exactamente el modelo MensajeAutomatico del backend ASP.NET Core
 */
data class ContentResponse(
    @SerializedName("id")
    val id: String? = null,

    @SerializedName("titulo")
    val titulo: String,

    @SerializedName("contenido")
    val contenido: String,

    @SerializedName("tipo")
    val tipo: String,

    @SerializedName("diaGatillo")
    val diaGatillo: Int? = null,

    @SerializedName("prioridad")
    val prioridad: String = "",

    @SerializedName("canal")
    val canal: List<String> = emptyList(),

    @SerializedName("activo")
    val activo: Boolean = true,

    @SerializedName("segmento")
    val segmento: String = "",

    @SerializedName("horaEnvio")
    val horaEnvio: String = "",

    @SerializedName("condicion")
    val condicion: String? = null,

    @SerializedName("fechaCreacion")
    val fechaCreacion: String? = null,

    @SerializedName("creadoPor")
    val creadoPor: String = ""
)

/**
 * Respuesta de lista de mensajes automáticos
 */
data class ContentListResponse(
    @SerializedName("data")
    val data: List<ContentResponse>? = null,
    
    @SerializedName("items")
    val items: List<ContentResponse>? = null,
    
    @SerializedName("mensajes")
    val mensajes: List<ContentResponse>? = null,

    @SerializedName("total")
    val total: Int? = null,
    
    @SerializedName("message")
    val message: String? = null
) {
    fun getList(): List<ContentResponse> {
        return data ?: items ?: mensajes ?: emptyList()
    }
}
