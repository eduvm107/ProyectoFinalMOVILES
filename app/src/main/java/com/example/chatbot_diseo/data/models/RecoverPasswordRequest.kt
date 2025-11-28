package com.example.chatbot_diseo.data.models

import com.google.gson.annotations.SerializedName

data class RecoverPasswordRequest(
    @SerializedName("email")
    val email: String
)
