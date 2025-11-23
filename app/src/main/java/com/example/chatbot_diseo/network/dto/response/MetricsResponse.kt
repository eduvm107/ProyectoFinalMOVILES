package com.example.chatbot_diseo.network.dto.response

import com.google.gson.annotations.SerializedName

/**
 * DTO para métricas del panel admin
 */
data class MetricsResponse(
    @SerializedName("totalContents")
    val totalContents: Int,

    @SerializedName("totalActivities")
    val totalActivities: Int,

    @SerializedName("totalResources")
    val totalResources: Int,

    @SerializedName("completionRate")
    val completionRate: Int,

    @SerializedName("averageSatisfaction")
    val averageSatisfaction: Double,

    @SerializedName("averageTimeDays")
    val averageTimeDays: Int,

    @SerializedName("activeUsers")
    val activeUsers: Int? = 0,

    @SerializedName("totalInteractions")
    val totalInteractions: Int? = 0
)

