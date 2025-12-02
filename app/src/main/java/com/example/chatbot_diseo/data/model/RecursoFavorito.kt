package com.example.chatbot_diseo.data.model

// Modelo unificado para recursos marcados como favoritos por el usuario
data class RecursoFavorito(
    val id: String,
    val tipo: String, // documento, actividad, chat, etc.
    val titulo: String,
    val descripcion: String?,
    val url: String?,
    val fechaRelevante: String?, // usar String por simplicidad (ISO-8601)
    val isFavorite: Boolean = true
)
