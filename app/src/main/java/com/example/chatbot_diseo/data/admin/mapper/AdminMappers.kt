package com.example.chatbot_diseo.data.admin.mapper

import com.example.chatbot_diseo.data.admin.ActivityItem
import com.example.chatbot_diseo.data.admin.ContentItem
import com.example.chatbot_diseo.data.admin.ResourceItem
import com.example.chatbot_diseo.network.dto.response.ActivityResponse
import com.example.chatbot_diseo.network.dto.response.ContentResponse
import com.example.chatbot_diseo.network.dto.response.ResourceResponse

/**
 * Mappers para convertir DTOs de red (backend en español) a modelos de dominio
 */

// Content Mappers (MensajeAutomatico -> ContentItem)
fun ContentResponse.toContentItem(): ContentItem {
    return ContentItem(
        id = id ?: "",
        title = titulo,
        type = tipo,
        description = contenido
    )
}

fun List<ContentResponse>.toContentItems(): List<ContentItem> {
    return this.map { it.toContentItem() }
}

// Activity Mappers (Actividad -> ActivityItem)
fun ActivityResponse.toActivityItem(): ActivityItem {
    return ActivityItem(
        id = id ?: "",
        title = titulo,
        date = "Día $dia - $horaInicio",
        modality = modalidad
    )
}

fun List<ActivityResponse>.toActivityItems(): List<ActivityItem> {
    return this.map { it.toActivityItem() }
}

// Resource Mappers (Documento -> ResourceItem)
fun ResourceResponse.toResourceItem(): ResourceItem {
    return ResourceItem(
        id = id ?: "",
        title = titulo,
        category = categoria,
        url = url
    )
}

fun List<ResourceResponse>.toResourceItems(): List<ResourceItem> {
    return this.map { it.toResourceItem() }
}
