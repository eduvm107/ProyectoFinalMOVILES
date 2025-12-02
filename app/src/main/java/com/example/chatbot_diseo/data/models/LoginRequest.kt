package com.example.chatbot_diseo.data.models

data class LoginRequest(
    val email: String,
    val password: String
)

data class ChangePasswordRequest(
    val email: String,
    val currentPassword: String,
    val newPassword: String
)

data class ChangePasswordResponse(
    val message: String
)

