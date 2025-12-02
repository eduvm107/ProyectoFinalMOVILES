package com.example.chatbot_diseo.data.admin.mapper

import com.example.chatbot_diseo.data.admin.ActivityItem
import com.example.chatbot_diseo.data.admin.ResourceItem
import com.example.chatbot_diseo.network.dto.response.ActivityResponse
import com.example.chatbot_diseo.network.dto.response.ResourceResponse

/**
 * Mappers para convertir DTOs del backend a modelos de UI
 */

/**
 * Convierte ActivityResponse (del backend) a ActivityItem (para UI)
 */
fun ActivityResponse.toActivityItem(): ActivityItem {
    return ActivityItem(
        id = this.id ?: "",
        title = this.titulo,
        date = "Día ${this.dia} - ${this.horaInicio}",
        modality = this.modalidad
    )
}

/**
 * Convierte ResourceResponse (del backend) a ResourceItem (para UI)
 */
fun ResourceResponse.toResourceItem(): ResourceItem {
    return ResourceItem(
        id = this.id ?: "",
        title = this.titulo,
        category = this.categoria,
        url = this.url
    )
}

