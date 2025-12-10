//recien agregado
package com.example.chatbot_diseo.data.model

// Corregimos la importación del modelo Mensaje (antes referenciaba presentation.chat)
import com.google.gson.annotations.SerializedName
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException


data class Conversacion(
    // Aceptamos que el ID pueda venir como "_id" o "id" y mapearlo correctamente preferiendo "id" como nombre de campo
    @SerializedName("id", alternate = ["_id"])
    val id: String,

    @SerializedName("usuarioId")
    val usuarioId: String,

    // Campo opcional que puede enviar el backend
    @SerializedName("titulo")
    val tituloBackend: String? = null,

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
) {
    // Propiedad calculada para el título mostrado en la UI
    val tituloMostrado: String
        get() {
            // Si el backend envía un título válido, usarlo
            tituloBackend?.let {
                if (it.isNotBlank()) return it
            }

            // Si hay mensajes, usar el contenido del primer mensaje
            if (mensajes.isNotEmpty()) {
                val primerContenido = mensajes.first().contenido.ifBlank { "" }
                if (primerContenido.isNotBlank()) {
                    return if (primerContenido.length > 40) primerContenido.take(40) + "..." else primerContenido
                }
            }

            // Fallback: usar fecha formateada
            return "Conversación - ${formatearFechaSimple(fechaInicio)}"
        }

    // Función helper para crear una copia con favorito actualizado
    fun conFavorito(nuevoEstado: Boolean): Conversacion {
        return this.copy(favorito = nuevoEstado)
    }
}

// Utilidad para formatear la fecha de inicio en un formato simple legible.
// Acepta ISO-8601 u otras cadenas y devuelve una representación corta (ej: 10 dic 2025).
fun formatearFechaSimple(fechaInicio: String): String {
    if (fechaInicio.isBlank()) return "sin fecha"

    // Intentar parsear como OffsetDateTime (ISO-8601) y formatear
    return try {
        val odt = OffsetDateTime.parse(fechaInicio)
        val formatter = DateTimeFormatter.ofPattern("d MMM yyyy")
        odt.format(formatter)
    } catch (_: DateTimeParseException) {
        // Si falla, intentar cortar la parte de la fecha (primer 10 caracteres)
        return if (fechaInicio.length >= 10) {
            try {
                // intentar parsear yyyy-MM-dd
                val sub = fechaInicio.substring(0, 10)
                val parsed = OffsetDateTime.parse(sub + "T00:00:00Z")
                parsed.format(DateTimeFormatter.ofPattern("d MMM yyyy"))
            } catch (_: Exception) {
                // Dejar la cadena original truncada
                fechaInicio.take(16)
            }
        } else {
            fechaInicio
        }
    }
}
