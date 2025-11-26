package com.example.chatbot_diseo.data.admin

import com.example.chatbot_diseo.data.admin.datasource.AdminRemoteDataSource
import com.example.chatbot_diseo.data.admin.mapper.toActivityItems
import com.example.chatbot_diseo.data.admin.mapper.toContentItems
import com.example.chatbot_diseo.data.admin.mapper.toResourceItems
import com.example.chatbot_diseo.data.common.Result

/**
 * Repository para el panel de administración
 * Coordina el acceso a datos remotos desde el backend ASP.NET Core
 */
class AdminRepository {

    private val remoteDataSource = AdminRemoteDataSource()

    // ============== CONTENIDOS ==============

    suspend fun getContents(): Result<List<ContentItem>> {
        return when (val result = remoteDataSource.getContents()) {
            is Result.Success -> {
                // Convertir cada ContentResponse a ContentItem
                val items = result.data.map { response ->
                    ContentItem(
                        id = response.id ?: "",
                        title = response.titulo,
                        type = response.tipo,
                        description = response.contenido
                    )
                }
                Result.Success(items)
            }
            is Result.Error -> result
            is Result.Loading -> result
        }
    }

    suspend fun addContent(title: String, type: String, description: String): Result<ContentItem> {
        return when (val result = remoteDataSource.createContent(title, type, description)) {
            is Result.Success -> Result.Success(
                ContentItem(
                    id = result.data.id ?: "",
                    title = result.data.titulo,
                    type = result.data.tipo,
                    description = result.data.contenido
                )
            )
            is Result.Error -> result
            is Result.Loading -> result
        }
    }

    suspend fun updateContent(id: String, title: String, type: String, description: String): Result<ContentItem> {
        return when (val result = remoteDataSource.updateContent(id, title, type, description)) {
            is Result.Success -> Result.Success(
                ContentItem(
                    id = result.data.id ?: id,
                    title = result.data.titulo,
                    type = result.data.tipo,
                    description = result.data.contenido
                )
            )
            is Result.Error -> result
            is Result.Loading -> result
        }
    }

    suspend fun deleteContent(id: String): Result<Boolean> {
        return remoteDataSource.deleteContent(id)
    }

    // ============== ACTIVIDADES ==============

    suspend fun getActivities(): Result<List<ActivityItem>> {
        return when (val result = remoteDataSource.getActivities()) {
            is Result.Success -> {
                // Convertir cada ActivityResponse a ActivityItem
                val items = result.data.map { response ->
                    ActivityItem(
                        id = response.id ?: "",
                        title = response.titulo,
                        date = "Día ${response.dia} - ${response.horaInicio}",
                        modality = response.modalidad
                    )
                }
                Result.Success(items)
            }
            is Result.Error -> result
            is Result.Loading -> result
        }
    }

    suspend fun addActivity(title: String, date: String, modality: String): Result<ActivityItem> {
        return when (val result = remoteDataSource.createActivity(title, date, modality)) {
            is Result.Success -> Result.Success(
                ActivityItem(
                    id = result.data.id ?: "",
                    title = result.data.titulo,
                    date = "Día ${result.data.dia} - ${result.data.horaInicio}",
                    modality = result.data.modalidad
                )
            )
            is Result.Error -> result
            is Result.Loading -> result
        }
    }

    suspend fun updateActivity(id: String, title: String, date: String, modality: String): Result<ActivityItem> {
        return when (val result = remoteDataSource.updateActivity(id, title, date, modality)) {
            is Result.Success -> Result.Success(
                ActivityItem(
                    id = result.data.id ?: id,
                    title = result.data.titulo,
                    date = "Día ${result.data.dia} - ${result.data.horaInicio}",
                    modality = result.data.modalidad
                )
            )
            is Result.Error -> result
            is Result.Loading -> result
        }
    }

    suspend fun deleteActivity(id: String): Result<Boolean> {
        return remoteDataSource.deleteActivity(id)
    }

    // ============== RECURSOS ==============

    suspend fun getResources(): Result<List<ResourceItem>> {
        return when (val result = remoteDataSource.getResources()) {
            is Result.Success -> {
                // Convertir cada ResourceResponse a ResourceItem
                val items = result.data.map { response ->
                    ResourceItem(
                        id = response.id ?: "",
                        title = response.titulo,
                        category = response.categoria,
                        url = response.url
                    )
                }
                Result.Success(items)
            }
            is Result.Error -> result
            is Result.Loading -> result
        }
    }

    suspend fun addResource(title: String, category: String, url: String): Result<ResourceItem> {
        return when (val result = remoteDataSource.createResource(title, category, url)) {
            is Result.Success -> Result.Success(
                ResourceItem(
                    id = result.data.id ?: "",
                    title = result.data.titulo,
                    category = result.data.categoria,
                    url = result.data.url
                )
            )
            is Result.Error -> result
            is Result.Loading -> result
        }
    }

    suspend fun updateResource(id: String, title: String, category: String, url: String): Result<ResourceItem> {
        return when (val result = remoteDataSource.updateResource(id, title, category, url)) {
            is Result.Success -> Result.Success(
                ResourceItem(
                    id = result.data.id ?: id,
                    title = result.data.titulo,
                    category = result.data.categoria,
                    url = result.data.url
                )
            )
            is Result.Error -> result
            is Result.Loading -> result
        }
    }

    suspend fun deleteResource(id: String): Result<Boolean> {
        return remoteDataSource.deleteResource(id)
    }

    // ============== MÉTRICAS ==============

    suspend fun getMetrics(): Result<AdminStats> {
        return when (val result = remoteDataSource.getMetrics()) {
            is Result.Success -> Result.Success(
                AdminStats(
                    totalContents = result.data.totalContents,
                    totalActivities = result.data.totalActivities,
                    totalResources = result.data.totalResources,
                    completionRate = result.data.completionRate,
                    averageSatisfaction = result.data.averageSatisfaction,
                    averageTimeDays = result.data.averageTimeDays
                )
            )
            is Result.Error -> result
            is Result.Loading -> result
        }
    }
}

/**
 * Data class para métricas del dashboard
 */
data class AdminStats(
    val totalContents: Int,
    val totalActivities: Int,
    val totalResources: Int,
    val completionRate: Int,
    val averageSatisfaction: Double,
    val averageTimeDays: Int
)
