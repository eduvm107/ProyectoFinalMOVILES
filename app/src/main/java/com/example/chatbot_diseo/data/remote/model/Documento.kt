package com.example.chatbot_diseo.data.remote.model

data class Documento(
    val id: String?,
    val titulo: String,
    val url: String,
    val descripcion: String?,
    val tipo: String?,
    val tags: List<String>?
)
