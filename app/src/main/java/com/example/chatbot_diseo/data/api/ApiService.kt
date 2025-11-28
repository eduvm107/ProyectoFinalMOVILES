package com.example.chatbot_diseo.data.api

import com.example.chatbot_diseo.data.models.LoginRequest
import com.example.chatbot_diseo.data.models.LoginResponse
import com.example.chatbot_diseo.data.models.RecoverPasswordRequest
import com.example.chatbot_diseo.data.models.RecoverPasswordResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {
    @POST("Auth/login")
    suspend fun login(@Body request: LoginRequest): Response<LoginResponse>

    @POST("usuario/recuperar")
    suspend fun recoverPassword(@Body request: RecoverPasswordRequest): Response<RecoverPasswordResponse>
}
