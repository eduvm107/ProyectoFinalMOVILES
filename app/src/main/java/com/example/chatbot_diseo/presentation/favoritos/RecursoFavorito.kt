package com.example.chatbot_diseo.data.model

data class RecursoFavorito(
    val id: String,
    val titulo: String,
    val descripcion: String,
    val tipo: String // Ej: "Manual", "Curso", "Beneficio"
)