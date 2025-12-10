package com.example.chatbot_diseo.data.repository

import com.example.chatbot_diseo.data.model.*
import com.example.chatbot_diseo.data.remote.apiChatBot.RetrofitInstance

/**
 * Repositorio para operaciones del Chatbot con IA
 */
class ChatbotRepository {

    private val chatbotApi = RetrofitInstance.chatbotApi
    private val chatOrquestadorApi = RetrofitInstance.chatOrquestadorApi

    /**
     * Enviar una pregunta al chatbot y obtener respuesta generada por IA (Ollama)
     * @param usuarioId ID del usuario que hace la pregunta
     * @param pregunta Texto de la pregunta
     * @param conversacionId ID de la conversación existente (opcional, para continuar un chat)
     */
    suspend fun enviarPregunta(
        usuarioId: String,
        pregunta: String,
        conversacionId: String? = null
    ): Result<ChatbotAskResponse> {
        return try {
            // ✅ Llamar al orquestador con conversacionId (siempre, sea null o no)
            val response = chatOrquestadorApi.preguntar(
                pregunta = pregunta,
                usuarioId = usuarioId,
                conversacionId = conversacionId  // Se pasa siempre (puede ser null)
            )

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
            val response = chatbotApi.getHistorial(usuarioId)
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
            val response = chatbotApi.registrarSatisfaccion(conversacionId, request)
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
            val response = chatbotApi.getEstadisticas()
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
            val response = chatbotApi.healthCheck()
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
