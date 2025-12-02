package com.example.chatbot_diseo.data.repository

import android.util.Log
import com.example.chatbot_diseo.data.model.FAQ
import com.example.chatbot_diseo.data.model.FAQRequest
import com.example.chatbot_diseo.data.remote.apiChatBot.RetrofitInstance

/**
 * Repositorio para operaciones de FAQs
 */
class FAQRepository {

    private val api = RetrofitInstance.faqApi
    private val TAG = "FAQRepository"

    /**
     * Obtener todas las FAQs
     */
    suspend fun getAllFAQs(): Result<List<FAQ>> {
        return try {
            val response = api.getAllFAQs()
            // Log para depuración
            try {
                Log.d(TAG, "getAllFAQs() HTTP ${response.code()} - ${response.message()}")
                val body = response.body()
                Log.d(TAG, "getAllFAQs() body present: ${body != null} size=${body?.size ?: 0}")
            } catch (ignored: Exception) {
                Log.d(TAG, "getAllFAQs() debug log failed: ${ignored.message}")
            }

            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                // Intentar leer error body si existe para más contexto
                val errMsg = try {
                    response.errorBody()?.string()
                } catch (e: Exception) {
                    null
                }
                val message = "Error: ${response.code()} - ${response.message()}${if (errMsg != null) " - $errMsg" else ""}"
                Log.e(TAG, message)
                Result.failure(Exception(message))
            }
        } catch (e: Exception) {
            Log.e(TAG, "getAllFAQs() exception: ${e.message}", e)
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
