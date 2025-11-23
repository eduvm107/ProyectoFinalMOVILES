package com.example.chatbot_diseo.data.model

import com.google.gson.annotations.SerializedName

// Modelo de Configuración según el backend
data class Configuracion(
    @SerializedName("_id")
    val id: String? = null,
    val nombre: String,
    val valor: String? = null,
    val tipo: String, // chatbot, notificaciones, seguridad, general
    val descripcion: String? = null,
    val configuracion: Map<String, Any>? = null, // BSON dinámico
    val activo: Boolean = true,
    val modificadoPor: String? = null,
    val fechaCreacion: String? = null,
    val fechaModificacion: String? = null
)

// Request para crear/actualizar configuración
data class ConfiguracionRequest(
    val nombre: String,
    val valor: String? = null,
    val tipo: String,
    val descripcion: String? = null,
    val configuracion: Map<String, Any>? = null,
    val activo: Boolean = true
)
