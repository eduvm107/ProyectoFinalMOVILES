package com.example.chatbot_diseo.data.admin

import com.example.chatbot_diseo.data.admin.datasource.AdminRemoteDataSource
import com.example.chatbot_diseo.data.common.Result
import com.example.chatbot_diseo.network.dto.request.ActivityRequest
import com.example.chatbot_diseo.network.dto.response.ActivityResponse
import com.example.chatbot_diseo.network.dto.response.ContentResponse

/**
 * Repositorio para el panel de administración
 * Coordina el acceso a datos remotos y locales
 */
class AdminRepository {

    private val remoteDataSource = AdminRemoteDataSource()

    // ============== CONTENIDOS ==============

    suspend fun getContents(): Result<List<ContentResponse>> {
        return remoteDataSource.getContents()
    }

    suspend fun getContentById(id: String): Result<ContentResponse> {
        return remoteDataSource.getContentById(id)
    }

    suspend fun addContent(
        titulo: String,
        contenido: String,
        tipo: String,
        diaGatillo: Int,
        prioridad: String,
        canal: List<String>,
        activo: Boolean,
        segmento: String,
        horaEnvio: String
    ): Result<ContentResponse> {
        return remoteDataSource.createContent(
            titulo, contenido, tipo, diaGatillo, prioridad, canal, activo, segmento, horaEnvio
        )
    }

    suspend fun updateContent(
        id: String,
        titulo: String,
        contenido: String,
        tipo: String,
        diaGatillo: Int,
        prioridad: String,
        canal: List<String>,
        activo: Boolean,
        segmento: String,
        horaEnvio: String
    ): Result<ContentResponse> {
        return remoteDataSource.updateContent(
            id, titulo, contenido, tipo, diaGatillo, prioridad, canal, activo, segmento, horaEnvio
        )
    }

    suspend fun deleteContent(id: String): Result<Boolean> {
        return remoteDataSource.deleteContent(id)
    }

    // ============== ACTIVIDADES ==============

    suspend fun getActivities(): Result<List<ActivityItem>> {
        return when (val result = remoteDataSource.getActivities()) {
            is Result.Success -> {
                val activities = result.data.map { response ->
                    ActivityItem(
                        id = response.id ?: "",
                        title = response.titulo,
                        date = "Día ${response.dia} - ${response.horaInicio}",
                        modality = response.modalidad
                    )
                }
                Result.Success(activities)
            }
            is Result.Error -> Result.Error(result.message)
            is Result.Loading -> Result.Loading
        }
    }

    suspend fun getActivityById(id: String): Result<ActivityResponse> {
        return remoteDataSource.getActivityById(id)
    }

    suspend fun addActivity(title: String, date: String, modality: String): Result<ActivityResponse> {
        return remoteDataSource.createActivity(title, date, modality)
    }

    suspend fun updateActivity(id: String, title: String, date: String, modality: String): Result<ActivityResponse> {
        return remoteDataSource.updateActivity(id, title, date, modality)
    }

    suspend fun updateActivityComplete(id: String, activityRequest: ActivityRequest): Result<ActivityResponse> {
        return remoteDataSource.updateActivityComplete(id, activityRequest)
    }

    suspend fun deleteActivity(id: String): Result<Boolean> {
        return remoteDataSource.deleteActivity(id)
    }

    // ============== RECURSOS ==============

    suspend fun getResources(): Result<List<ResourceItem>> {
        return when (val result = remoteDataSource.getResources()) {
            is Result.Success -> {
                val resources = result.data.map { response ->
                    ResourceItem(
                        id = response.id ?: "",
                        title = response.titulo,
                        category = response.categoria,
                        url = response.url
                    )
                }
                Result.Success(resources)
            }
            is Result.Error -> Result.Error(result.message)
            is Result.Loading -> Result.Loading
        }
    }

    suspend fun addResource(title: String, category: String, url: String): Result<com.example.chatbot_diseo.network.dto.response.ResourceResponse> {
        return remoteDataSource.createResource(title, category, url)
    }

    suspend fun updateResource(id: String, title: String, category: String, url: String): Result<com.example.chatbot_diseo.network.dto.response.ResourceResponse> {
        return remoteDataSource.updateResource(id, title, category, url)
    }

    suspend fun deleteResource(id: String): Result<Boolean> {
        return remoteDataSource.deleteResource(id)
    }

    // ============== MÉTRICAS ==============

    fun getMetrics(): Result<AdminStats> {
        // Por ahora retornamos un error para que use el fallback local
        return Result.Error("Métricas no implementadas aún")
    }
}

/**
 * Data class para estadísticas del panel de administración
 */
data class AdminStats(
    val totalContents: Int = 0,
    val totalActivities: Int = 0,
    val totalResources: Int = 0,
    val completionRate: Int = 0,
    val averageSatisfaction: Double = 0.0,
    val averageTimeDays: Int = 0
)
