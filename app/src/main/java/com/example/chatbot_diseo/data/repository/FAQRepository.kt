package com.example.chatbot_diseo.data.repository

import com.example.chatbot_diseo.data.model.FAQ
import com.example.chatbot_diseo.data.model.FAQRequest
import com.example.chatbot_diseo.data.remote.apiChatBot.RetrofitInstance

/**
 * Repositorio para operaciones de FAQs
 */
class FAQRepository {

    private val api = RetrofitInstance.faqApi

    /**
     * Obtener todas las FAQs
     */
    suspend fun getAllFAQs(): Result<List<FAQ>> {
        return try {
            val response = api.getAllFAQs()
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Error: ${response.code()} - ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Obtener FAQ por ID
     */
    suspend fun getFAQById(id: String): Result<FAQ> {
        return try {
            val response = api.getFAQById(id)
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Error: ${response.code()} - ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Crear una nueva FAQ
     */
    suspend fun createFAQ(faq: FAQRequest): Result<FAQ> {
        return try {
            val response = api.createFAQ(faq)
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Error: ${response.code()} - ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Actualizar una FAQ existente
     */
    suspend fun updateFAQ(id: String, faq: FAQRequest): Result<Unit> {
        return try {
            val response = api.updateFAQ(id, faq)
            if (response.isSuccessful) {
                Result.success(Unit)
            } else {
                Result.failure(Exception("Error: ${response.code()} - ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Eliminar una FAQ
     */
    suspend fun deleteFAQ(id: String): Result<Unit> {
        return try {
            val response = api.deleteFAQ(id)
            if (response.isSuccessful) {
                Result.success(Unit)
            } else {
                Result.failure(Exception("Error: ${response.code()} - ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Buscar FAQs por texto
     */
    suspend fun searchFAQs(query: String): Result<List<FAQ>> {
        return try {
            val response = api.searchFAQs(query)
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Error: ${response.code()} - ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
