package com.example.chatbot_diseo.data.model

data class MensajeRequest(
    val contenido: String,
    val tipo: String = "usuario" // Por defecto somos nosotros
)