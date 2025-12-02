package com.example.chatbot_diseo.data.model

import com.google.gson.annotations.SerializedName

// Modelo de FAQ según el backend
data class FAQ(
    @SerializedName("_id")
    val id: String? = null,
    val pregunta: String,
    val respuesta: String,
    // categoria puede venir ausente desde el backend; dar valor por defecto para evitar errores de deserialización
    val categoria: String = "",
    val subcategoria: String? = null,
    val palabrasClave: List<String>? = null,
    val prioridad: String? = null,
    val activa: Boolean = true,
    val vecesUsada: Int? = null,
    val rating: Double? = null,
    val respuestaLarga: String? = null,
    val documentosRelacionados: List<String>? = null,
    val actividadesRelacionadas: List<String>? = null,
    val fechaCreacion: String? = null,
    val fechaModificacion: String? = null
)

// Request para crear/actualizar FAQ
data class FAQRequest(
    val pregunta: String,
    val respuesta: String,
    val categoria: String,
    val subcategoria: String? = null,
    val palabrasClave: List<String>? = null,
    val prioridad: String? = null,
    val activa: Boolean = true,
    val respuestaLarga: String? = null,
    val documentosRelacionados: List<String>? = null,
    val actividadesRelacionadas: List<String>? = null
)
