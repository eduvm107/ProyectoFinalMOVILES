package com.example.chatbot_diseo.data.repository

import android.util.Log
import com.example.chatbot_diseo.data.model.Conversacion
import com.example.chatbot_diseo.data.remote.apiChatBot.RetrofitInstance

/**
 * Repositorio para gestión del historial de conversaciones
 */
class HistorialRepository {

    private val api = RetrofitInstance.conversacionApi

    // ID del usuario actual (se puede cambiar dinámicamente)
    var usuarioId: String = "test-user-001"

    /**
     * Obtener todas las conversaciones del usuario actual
     */
    suspend fun obtenerMisConversaciones(): List<Conversacion> {
        return try {
            Log.d("HISTORIAL_API", "Pidiendo historial para: $usuarioId")
            val response = api.obtenerHistorial(usuarioId)

            if (response.isSuccessful) {
                val lista = response.body() ?: emptyList()
                Log.d("CHAT_API", "¡Éxito! Recibidos ${lista.size} chats")
                lista
            } else {
                Log.e("CHAT_API", "Error Servidor: ${response.code()} ${response.message()}")
                emptyList()
            }
        } catch (e: Exception) {
            Log.e("CHAT_API", "Error Conexión: ${e.message}")
            emptyList()
        }
    }

    /**
     * Crear una nueva conversación
     */
    suspend fun crearNuevoChat(): Conversacion? {
        return try {
            val nuevoChat = Conversacion(usuarioId = usuarioId)
            val response = api.crearConversacion(nuevoChat)

            if (response.isSuccessful) {
                Log.d("HISTORIAL_API", "¡Chat creado!")
                response.body()
            } else {
                Log.e("HISTORIAL_API", "Error al crear: ${response.code()}")
                null
            }
        } catch (e: Exception) {
            Log.e("HISTORIAL_API", "Error conexión al crear: ${e.message}")
            null
        }
    }
}