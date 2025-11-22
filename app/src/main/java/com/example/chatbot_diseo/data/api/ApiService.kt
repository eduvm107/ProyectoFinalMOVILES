package com.example.chatbot_diseo.data.api

import com.example.chatbot_diseo.data.models.LoginRequest
import com.example.chatbot_diseo.data.models.LoginResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {
    @POST("api/Auth/login")
    suspend fun login(@Body request: LoginRequest): Response<LoginResponse>
}

