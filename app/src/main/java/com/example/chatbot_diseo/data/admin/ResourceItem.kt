package com.example.chatbot_diseo.data.admin

data class ResourceItem(
    val id: Int,
    val title: String,
    val category: String,
    val url: String,
    val apiId: String? = null // ID del backend MongoDB
)