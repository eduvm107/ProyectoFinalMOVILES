package com.example.chatbot_diseo.data.models

// Modelo que representa el usuario devuelto por la API
data class Usuario(
    val id: String?,
    val email: String?,
    val nombreCompleto: String?,
    val nombre: String?,
    val departamento: String?,
    val puesto: String?,
    val activo: Boolean? = null,
    val verificado: Boolean? = null,
    val estadoOnboarding: String? = null,
    val progresoOnboarding: Int? = null,
    val rol: String? = null
)

// Respuesta del login según el ejemplo proporcionado
data class LoginResponse(
    val message: String? = null,
    val token: String? = null,
    val usuario: Usuario? = null
)
