package com.example.chatbot_diseo.presentation.favoritos

// Clase auxiliar para UI local de Favoritos (no reemplaza al modelo de datos en data.model)
data class FavoritoUi(
    val id: String,
    val titulo: String,
    val descripcion: String,
    val tipo: String // Ej: "Manual", "Curso", "Beneficio"
)