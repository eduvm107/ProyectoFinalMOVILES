package com.example.chatbot_diseo.network.dto.response

import com.google.gson.annotations.SerializedName

/**
 * DTO para Actividades desde MongoDB
 * Mapea EXACTAMENTE el modelo Actividad del backend ASP.NET Core
 * IMPORTANTE: Los nombres deben coincidir con los [BsonElement] del backend
 */
data class ActivityResponse(
    @SerializedName("id")
    val id: String? = null,

    @SerializedName("titulo")
    val titulo: String = "",

    @SerializedName("descripcion")
    val descripcion: String = "",

    @SerializedName("dia")
    val dia: Int = 0,

    @SerializedName("duracionHoras")
    val duracionHoras: Double = 0.0,

    @SerializedName("horaInicio")
    val horaInicio: String = "",

    @SerializedName("horaFin")
    val horaFin: String = "",

    @SerializedName("lugar")
    val lugar: String = "",

    @SerializedName("modalidad")
    val modalidad: String = "",

    @SerializedName("tipo")
    val tipo: String = "",

    @SerializedName("categoria")
    val categoria: String = "",

    @SerializedName("responsable")
    val responsable: String = "",

    @SerializedName("emailResponsable")
    val emailResponsable: String? = null,

    @SerializedName("capacidadMaxima")
    val capacidadMaxima: Int = 0,

    @SerializedName("obligatorio")
    val obligatorio: Boolean = false,

    @SerializedName("materialesNecesarios")
    val materialesNecesarios: List<String> = emptyList(),

    @SerializedName("materialesProporcionados")
    val materialesProporcionados: List<String> = emptyList(),

    @SerializedName("preparacionPrevia")
    val preparacionPrevia: String? = null,

    @SerializedName("actividadesSiguientes")
    val actividadesSiguientes: List<String> = emptyList(),

    @SerializedName("estado")
    val estado: String = "activo",

    @SerializedName("fechaCreacion")
    val fechaCreacion: String? = null,

    @SerializedName("fecha_de_actividad")
    val fechaDeActividad: String? = null,

    @SerializedName("usuarioID")
    val usuarioID: String? = null,

    // Campo legacy para documentos antiguos con usuarioId (minúscula)
    @SerializedName("usuarioId")
    val usuarioId: String? = null,

    @SerializedName("favorito")
    val favorito: Boolean = false
)

/**
 * Respuesta de lista de actividades
 * El backend devuelve directamente una List<Actividad>, no un objeto wrapper
 */
data class ActivityListResponse(
    val actividades: List<ActivityResponse>
) {
    companion object {
        // Helper para convertir respuesta directa del backend
        fun fromList(list: List<ActivityResponse>): ActivityListResponse {
            return ActivityListResponse(list)
        }
    }

    fun getList(): List<ActivityResponse> {
        return actividades
    }
}
