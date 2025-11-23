package com.example.chatbot_diseo.data.repository

import com.example.chatbot_diseo.data.model.*
import com.example.chatbot_diseo.data.remote.apiChatBot.RetrofitInstance

/**
 * Repositorio para operaciones del Chatbot con IA
 */
class ChatbotRepository {

    private val api = RetrofitInstance.chatbotApi

    /**
     * Enviar una pregunta al chatbot y obtener respuesta generada por IA (Ollama)
     */
    suspend fun enviarPregunta(usuarioId: String, pregunta: String): Result<ChatbotAskResponse> {
        return try {
            val request = ChatbotAskRequest(usuarioId, pregunta)
            val response = api.ask(request)
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
     * Obtener historial de conversaciones del chatbot para un usuario
     */
    suspend fun getHistorial(usuarioId: String): Result<ChatbotHistorialResponse> {
        return try {
            val response = api.getHistorial(usuarioId)
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
     * Registrar la satisfacción del usuario con una respuesta (1-5)
     */
    suspend fun registrarSatisfaccion(conversacionId: String, satisfaccion: Int): Result<SatisfaccionResponse> {
        return try {
            val request = SatisfaccionRequest(satisfaccion)
            val response = api.registrarSatisfaccion(conversacionId, request)
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
     * Obtener estadísticas del chatbot
     */
    suspend fun getEstadisticas(): Result<ChatbotEstadisticasResponse> {
        return try {
            val response = api.getEstadisticas()
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
     * Verificar el estado del servicio de chatbot (Ollama)
     */
    suspend fun healthCheck(): Result<ChatbotHealthResponse> {
        return try {
            val response = api.healthCheck()
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
