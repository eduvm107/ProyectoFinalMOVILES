package com.example.chatbot_diseo.network.dto.response

import com.google.gson.annotations.SerializedName

/**
 * Modelo completo de usuario según el backend ASP.NET Core
 */
data class UsuarioCompleto(
    @SerializedName("id")
    val id: String,

    @SerializedName("nombre")
    val nombre: String,

    @SerializedName("apellidos")
    val apellidos: String,

    @SerializedName("nombreCompleto")
    val nombreCompleto: String,

    @SerializedName("email")
    val email: String,

    @SerializedName("telefono")
    val telefono: String?,

    @SerializedName("dni")
    val dni: String?,

    @SerializedName("fechaNacimiento")
    val fechaNacimiento: String?,

    @SerializedName("edad")
    val edad: Int?,

    @SerializedName("genero")
    val genero: String?,

    @SerializedName("estadoCivil")
    val estadoCivil: String?,

    @SerializedName("direccion")
    val direccion: Direccion?,

    @SerializedName("area")
    val area: String?,

    @SerializedName("departamento")
    val departamento: String?,

    @SerializedName("puesto")
    val puesto: String?,

    @SerializedName("nivel")
    val nivel: String?,

    @SerializedName("tipoContrato")
    val tipoContrato: String?,

    @SerializedName("fechaIngreso")
    val fechaIngreso: String?,

    @SerializedName("diasDesdeIngreso")
    val diasDesdeIngreso: Int?,

    @SerializedName("supervisor")
    val supervisor: Supervisor?,

    @SerializedName("estadoOnboarding")
    val estadoOnboarding: String?,

    @SerializedName("progresoOnboarding")
    val progresoOnboarding: Double?,

    @SerializedName("actividadesCompletadas")
    val actividadesCompletadas: Int?,

    @SerializedName("actividadesPendientes")
    val actividadesPendientes: Int?,

    @SerializedName("documentosEntregados")
    val documentosEntregados: Int?,

    @SerializedName("documentosPendientes")
    val documentosPendientes: Int?,

    @SerializedName("cursosAsignados")
    val cursosAsignados: List<String>?,

    @SerializedName("cursosCompletados")
    val cursosCompletados: List<String>?,

    @SerializedName("certificaciones")
    val certificaciones: List<String>?,

    @SerializedName("favoritosChat")
    val favoritosChat: List<String>?,

    @SerializedName("preferencias")
    val preferencias: Preferencias?,

    @SerializedName("estadisticas")
    val estadisticas: Estadisticas?,

    @SerializedName("activo")
    val activo: Boolean,

    @SerializedName("verificado")
    val verificado: Boolean,

    @SerializedName("primerLogin")
    val primerLogin: String?,

    @SerializedName("ultimoLogin")
    val ultimoLogin: String?,

    @SerializedName("fechaCreacion")
    val fechaCreacion: String,

    @SerializedName("fechaActualizacion")
    val fechaActualizacion: String?,

    @SerializedName("creadoPor")
    val creadoPor: String?,

    @SerializedName("rol")
    val rol: String
)

/**
 * Modelo de dirección del usuario
 */
data class Direccion(
    @SerializedName("calle")
    val calle: String?,

    @SerializedName("distrito")
    val distrito: String?,

    @SerializedName("ciudad")
    val ciudad: String?,

    @SerializedName("pais")
    val pais: String?,

    @SerializedName("codigoPostal")
    val codigoPostal: String?
)

/**
 * Modelo de supervisor del usuario
 */
data class Supervisor(
    @SerializedName("nombre")
    val nombre: String?,

    @SerializedName("email")
    val email: String?,

    @SerializedName("telefono")
    val telefono: String?,

    @SerializedName("puesto")
    val puesto: String?
)

/**
 * Modelo de preferencias del usuario
 */
data class Preferencias(
    @SerializedName("idioma")
    val idioma: String?,

    @SerializedName("tema")
    val tema: String?,

    @SerializedName("notificaciones")
    val notificaciones: Boolean?,

    @SerializedName("notificacionesEmail")
    val notificacionesEmail: Boolean?,

    @SerializedName("notificacionesPush")
    val notificacionesPush: Boolean?,

    @SerializedName("frecuenciaRecordatorios")
    val frecuenciaRecordatorios: String?
)

/**
 * Modelo de estadísticas del usuario
 */
data class Estadisticas(
    @SerializedName("mensajesEnviados")
    val mensajesEnviados: Int?,

    @SerializedName("consultasRealizadas")
    val consultasRealizadas: Int?,

    @SerializedName("documentosConsultados")
    val documentosConsultados: Int?,

    @SerializedName("actividadesCompletadasTotal")
    val actividadesCompletadasTotal: Int?,

    @SerializedName("tiempoPromedioRespuesta")
    val tiempoPromedioRespuesta: Double?,

    @SerializedName("satisfaccionPromedio")
    val satisfaccionPromedio: Double?,

    @SerializedName("ultimaActividad")
    val ultimaActividad: String?
)

