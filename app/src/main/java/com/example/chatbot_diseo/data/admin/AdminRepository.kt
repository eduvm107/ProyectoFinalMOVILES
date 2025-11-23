package com.example.chatbot_diseo.data.admin

import android.util.Log
import com.example.chatbot_diseo.data.model.FAQRequest
import com.example.chatbot_diseo.data.remote.apiChatBot.RetrofitInstance

/**
 * Repositorio para el panel de administración
 * Conecta con el backend real para gestionar FAQs, Documentos y Actividades
 */
class AdminRepository {

    private val faqApi = RetrofitInstance.faqApi
    private val documentosApi = RetrofitInstance.documentosApi
    private val actividadesApi = RetrofitInstance.actividadesApi
    private val chatbotApi = RetrofitInstance.chatbotApi

    // Datos locales para compatibilidad con el ViewModel actual
    val contents = mutableListOf<ContentItem>()
    val activities = mutableListOf<ActivityItem>()
    val resources = mutableListOf<ResourceItem>()

    private var contentIdCounter = 0
    private var activityIdCounter = 0
    private var resourceIdCounter = 0

    // ---------------- CONTENIDOS (FAQs) ----------------
    suspend fun loadContentsFromApi(): List<ContentItem> {
        return try {
            val response = faqApi.getAllFAQs()
            if (response.isSuccessful) {
                val faqs = response.body() ?: emptyList()
                contents.clear()
                faqs.forEachIndexed { index, faq ->
                    contents.add(ContentItem(
                        id = index + 1,
                        title = faq.pregunta,
                        type = faq.categoria,
                        description = faq.respuesta,
                        apiId = faq.id
                    ))
                }
                contentIdCounter = contents.size
                contents.toList()
            } else {
                Log.e("AdminRepo", "Error cargando FAQs: ${response.code()}")
                emptyList()
            }
        } catch (e: Exception) {
            Log.e("AdminRepo", "Error: ${e.message}")
            emptyList()
        }
    }

    suspend fun addContentToApi(title: String, type: String, desc: String): Boolean {
        return try {
            val request = FAQRequest(pregunta = title, respuesta = desc, categoria = type)
            val response = faqApi.createFAQ(request)
            if (response.isSuccessful) {
                loadContentsFromApi()
                true
            } else false
        } catch (e: Exception) {
            Log.e("AdminRepo", "Error creando FAQ: ${e.message}")
            false
        }
    }

    suspend fun deleteContentFromApi(apiId: String): Boolean {
        return try {
            val response = faqApi.deleteFAQ(apiId)
            response.isSuccessful
        } catch (e: Exception) {
            Log.e("AdminRepo", "Error eliminando FAQ: ${e.message}")
            false
        }
    }

    // Métodos locales para compatibilidad
    fun addContent(title: String, type: String, desc: String) {
        contents.add(ContentItem(++contentIdCounter, title, type, desc))
    }

    fun updateContent(id: Int, title: String, type: String, desc: String) {
        val index = contents.indexOfFirst { it.id == id }
        if (index != -1) {
            val apiId = contents[index].apiId
            contents[index] = ContentItem(id, title, type, desc, apiId)
        }
    }

    fun deleteContent(id: Int) {
        contents.removeAll { it.id == id }
    }

    // ---------------- ACTIVIDADES ----------------
    suspend fun loadActivitiesFromApi(): List<ActivityItem> {
        return try {
            val remotas = actividadesApi.getAllActividades()
            activities.clear()
            remotas.forEachIndexed { index, act ->
                activities.add(ActivityItem(
                    id = index + 1,
                    title = act.titulo,
                    date = act.fechaDeActividad.substringBefore("T"),
                    modality = act.modalidad,
                    apiId = act.id
                ))
            }
            activityIdCounter = activities.size
            activities.toList()
        } catch (e: Exception) {
            Log.e("AdminRepo", "Error cargando actividades: ${e.message}")
            emptyList()
        }
    }

    fun addActivity(title: String, date: String, modality: String) {
        activities.add(ActivityItem(++activityIdCounter, title, date, modality))
    }

    fun updateActivity(id: Int, title: String, date: String, modality: String) {
        val index = activities.indexOfFirst { it.id == id }
        if (index != -1) {
            val apiId = activities[index].apiId
            activities[index] = ActivityItem(id, title, date, modality, apiId)
        }
    }

    fun deleteActivity(id: Int) {
        activities.removeAll { it.id == id }
    }

    // ---------------- RECURSOS (Documentos) ----------------
    suspend fun loadResourcesFromApi(): List<ResourceItem> {
        return try {
            val docs = documentosApi.getAllDocumentos()
            resources.clear()
            docs.forEachIndexed { index, doc ->
                resources.add(ResourceItem(
                    id = index + 1,
                    title = doc.titulo,
                    category = doc.categoria ?: doc.tipo ?: "General",
                    url = doc.url,
                    apiId = doc.id
                ))
            }
            resourceIdCounter = resources.size
            resources.toList()
        } catch (e: Exception) {
            Log.e("AdminRepo", "Error cargando documentos: ${e.message}")
            emptyList()
        }
    }

    fun addResource(title: String, category: String, url: String) {
        resources.add(ResourceItem(++resourceIdCounter, title, category, url))
    }

    fun updateResource(id: Int, title: String, category: String, url: String) {
        val index = resources.indexOfFirst { it.id == id }
        if (index != -1) {
            val apiId = resources[index].apiId
            resources[index] = ResourceItem(id, title, category, url, apiId)
        }
    }

    fun deleteResource(id: Int) {
        resources.removeAll { it.id == id }
    }

    // ---------------- MÉTRICAS ----------------
    fun totalContents() = contents.size
    fun totalActivities() = activities.size
    fun totalResources() = resources.size

    suspend fun getEstadisticasFromApi(): AdminStats {
        return try {
            val response = chatbotApi.getEstadisticas()
            if (response.isSuccessful && response.body() != null) {
                val stats = response.body()!!
                AdminStats(
                    totalConversaciones = stats.totalConversaciones,
                    promedioSatisfaccion = stats.promedioSatisfaccion ?: 0.0,
                    totalFAQs = stats.totalFAQs
                )
            } else {
                AdminStats()
            }
        } catch (e: Exception) {
            Log.e("AdminRepo", "Error obteniendo estadísticas: ${e.message}")
            AdminStats()
        }
    }

    fun completionRate(): Int = 87
    fun averageSatisfaction(): Double = 4.5
    fun averageTimeDays(): Int = 14
}

// Clase para estadísticas del admin
data class AdminStats(
    val totalConversaciones: Int = 0,
    val promedioSatisfaccion: Double = 0.0,
    val totalFAQs: Int = 0
)
