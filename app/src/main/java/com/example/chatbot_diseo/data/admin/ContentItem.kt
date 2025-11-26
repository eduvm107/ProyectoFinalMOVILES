package com.example.chatbot_diseo.data.admin

data class ContentItem(
    val id: String,
    val title: String,
    val type: String,
    val description: String,
    val apiId: String? = null // ID del backend MongoDB
)