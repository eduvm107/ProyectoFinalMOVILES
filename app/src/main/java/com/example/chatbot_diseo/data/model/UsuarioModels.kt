package com.example.chatbot_diseo.data.model

import com.google.gson.annotations.SerializedName
import com.example.chatbot_diseo.network.dto.response.Supervisor

// Modelo completo de Usuario según el backend
data class UsuarioCompleto(
    @SerializedName("_id")
    val id: String? = null,
    val nombre: String? = null,
    val apellidos: String? = null,
    val nombreCompleto: String? = null,
    val email: String,
    @SerializedName("contraseña")
    val contrasena: String? = null,
    val telefono: String? = null,
    val dni: String? = null,
    val departamento: String? = null,
    val puesto: String? = null,
    val estadoOnboarding: String? = null,
    val progresoOnboarding: Int? = null,
    val actividadesCompletadas: List<String>? = null,
    val actividadesPendientes: List<String>? = null,
    val documentosEntregados: List<String>? = null,
    val documentosPendientes: List<String>? = null,
    val cursosAsignados: List<String>? = null,
    val cursosCompletados: List<String>? = null,
    val primerLogin: String? = null,
    val ultimoLogin: String? = null,
    val rol: String? = null,
    val activo: Boolean? = null,
    val verificado: Boolean? = null,
    val direccion: Direccion? = null,
    val supervisor: Supervisor? = null,
    val preferencias: Preferencias? = null,
    val estadisticas: EstadisticasUsuario? = null
)

// Dirección del usuario
data class Direccion(
    val calle: String? = null,
    val ciudad: String? = null,
    val codigoPostal: String? = null,
    val pais: String? = null
)

// Preferencias del usuario
data class Preferencias(
    val idioma: String? = null,
    val notificaciones: Boolean? = null,
    val temaOscuro: Boolean? = null
)

// Estadísticas del usuario
data class EstadisticasUsuario(
    val totalMensajes: Int? = null,
    val totalConversaciones: Int? = null,
    val tiempoEnPlataforma: Int? = null
)

// Request para crear/actualizar usuario
data class UsuarioRequest(
    val nombre: String,
    val apellidos: String? = null,
    val nombreCompleto: String? = null,
    val email: String,
    @SerializedName("contraseña")
    val contrasena: String? = null,
    val telefono: String? = null,
    val dni: String? = null,
    val departamento: String? = null,
    val puesto: String? = null,
    val activo: Boolean = true
)
