//recien agregado
package com.example.chatbot_diseo.data.model

// Corregimos la importación del modelo Mensaje (antes referenciaba presentation.chat)
import com.google.gson.annotations.SerializedName
import com.example.chatbot_diseo.data.model.Mensaje


data class Conversacion(
    // Aceptamos que el ID pueda venir como "_id" o "id" y lo dejamos nullable por seguridad
    @SerializedName(value = "_id", alternate = ["id"])
    val id: String? = null,

    @SerializedName("usuarioId")
    val usuarioId: String,

    // El Array de Mensajes completo
    @SerializedName("mensajes")
    val mensajes: List<Mensaje> = emptyList(),

    // Campos para mostrar en la lista del historial
    @SerializedName("fechaInicio")
    val fechaInicio: String = "",

    @SerializedName("activa")
    val activa: Boolean = false,

    @SerializedName("favorito")
    val favorito: Boolean = false
    // Puedes ignorar o incluir los otros campos (fechaUltimaMensaje, resuelto)
)