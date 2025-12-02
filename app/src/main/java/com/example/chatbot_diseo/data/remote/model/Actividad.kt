package com.example.chatbot_diseo.data.remote.model

import com.google.gson.annotations.SerializedName

class Actividad {

    // Modelo ligero para UI de calendario
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




    // Modelo completo que antes se llamaba Actividad, ahora renombrado
    // para poder reutilizar el nombre Actividad en otro lugar.
    data class Actividad_Calendario(
        val id: String,
        val titulo: String,
        val descripcion: String,
        val dia: Int,
        val duracionHoras: Double,
        val horaInicio: String,
        val horaFin: String,
        val lugar: String,
        val modalidad: String,
        val tipo: String,
        val categoria: String,
        val responsable: String,
        val emailResponsable: String,
        val capacidadMaxima: Int,
        val obligatorio: Boolean,
        val materialesNecesarios: List<String>,
        val materialesProporcionados: List<String>,
        val preparacionPrevia: String,
        val actividadesSiguientes: List<String>,
        val estado: String,
        val fechaCreacion: String,
        val fechaDeActividad: String,
        val UsuarioId: String,
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
        val fechaDeActividad: String = "",

        @SerializedName("UsuarioId")
        val UsuarioId: String = ""
    )

}
