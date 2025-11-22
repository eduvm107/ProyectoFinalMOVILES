package com.example.chatbot_diseo.data.repository

import com.example.chatbot_diseo.data.api.ApiService
import com.example.chatbot_diseo.data.models.LoginRequest
import com.example.chatbot_diseo.data.models.LoginResponse
import retrofit2.Response

class AuthRepository(private val api: ApiService) {
    suspend fun login(email: String, password: String): Response<LoginResponse> {
        val req = LoginRequest(email = email, password = password)
        return api.login(req)
    }
}

