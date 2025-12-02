package com.example.chatbot_diseo.data.remote.model

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

    data class Actividad(
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
}
