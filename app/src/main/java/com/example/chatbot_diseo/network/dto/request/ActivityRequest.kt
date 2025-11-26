package com.example.chatbot_diseo.network.dto.request

import com.google.gson.annotations.SerializedName

/**
 * Request para crear/actualizar una actividad
 */
data class ActivityRequest(
    @SerializedName("titulo")
    val titulo: String,

    @SerializedName("descripcion")
    val descripcion: String,

    @SerializedName("dia")
    val dia: Int,

    @SerializedName("duracionHoras")
    val duracionHoras: Double,

    @SerializedName("horaInicio")
    val horaInicio: String,

    @SerializedName("horaFin")
    val horaFin: String,

    @SerializedName("lugar")
    val lugar: String,

    @SerializedName("modalidad")
    val modalidad: String,

    @SerializedName("tipo")
    val tipo: String,

    @SerializedName("categoria")
    val categoria: String = "",

    @SerializedName("responsable")
    val responsable: String,

    @SerializedName("emailResponsable")
    val emailResponsable: String? = null,

    @SerializedName("capacidadMaxima")
    val capacidadMaxima: Int = 50,

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
    val estado: String = "activo"
)
