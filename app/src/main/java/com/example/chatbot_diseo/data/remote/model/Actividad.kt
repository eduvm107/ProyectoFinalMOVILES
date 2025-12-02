package com.example.chatbot_diseo.data.remote.model

import com.google.gson.annotations.SerializedName

class Actividad {

    data class ActividadUI(
        val id: String,
        val titulo: String,
        val fechaCorta: String,     // "10 NOV"
        val estado: String,         // "Pendiente"
        val horaInicio: String,     // "09:00 AM"
        val lugar: String           // "Auditorio Principal"
    )

    data class Notificacion(
        val titulo: String,
        val fechaDeActividad: String
    )

    data class Actividad(
        @SerializedName("_id")
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
        val estado: String = "",

        @SerializedName("fechaCreacion")
        val fechaCreacion: String = "",

        @SerializedName("fecha_de_actividad")
        val fechaDeActividad: String = ""
    )
}
