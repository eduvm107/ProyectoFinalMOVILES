package com.example.chatbot_diseo.data.repository

import android.util.Log
import com.example.chatbot_diseo.data.model.Conversacion
import com.example.chatbot_diseo.data.model.Mensaje
import com.example.chatbot_diseo.data.remote.apiChatBot.RetrofitInstance

// Código Corregido para HistorialRepository.kt
class HistorialRepository {

    private val api = RetrofitInstance.conversacionApi

    /**
     * Obtener todas las conversaciones del usuario actual
     */
    suspend fun obtenerMisConversaciones(usuarioId: String): List<Conversacion> { // 👈 Acepta el ID
        return try {
            Log.d("HISTORIAL_API", "Pidiendo historial para: $usuarioId")
            val response = api.obtenerHistorial(usuarioId) // Usa el ID recibido
            if (response.isSuccessful) {
                response.body() ?: emptyList()
            } else {
                Log.e("HISTORIAL_API", "Error al obtener historial: code=${response.code()} message=${response.message()}")
                emptyList()
            }
        } catch (e: Exception) {
            Log.e("HISTORIAL_API", "Excepción al obtener historial", e)
            emptyList()
        }
    }

    /**
     * Crear una nueva conversación
     */
    suspend fun crearNuevoChat(usuarioId: String): Conversacion? { // 👈 Acepta el ID
        return try {
            // Construimos un objeto mínimo compatible con el modelo Conversacion
            val nuevoChat = Conversacion(
                id = "", // backend suele generar el id
                usuarioId = usuarioId,
                mensajes = emptyList<Mensaje>(),
                fechaInicio = System.currentTimeMillis().toString(),
                activa = true,
                favorito = false
            )

            val response = api.crearConversacion(nuevoChat)
            if (response.isSuccessful) {
                response.body()
            } else {
                Log.e("HISTORIAL_API", "Error creando conversacion: code=${response.code()} message=${response.message()}")
                null
            }
        } catch (e: Exception) {
            Log.e("HISTORIAL_API", "Excepción al crear conversacion", e)
            null
        }
    }
}