//paraaaaa histoirallllllllllllllllllllllllll

package com.example.chatbot_diseo.data.repository

import android.util.Log
import com.example.chatbot_diseo.data.model.Conversacion
import com.example.chatbot_diseo.data.remote.RetrofitClient

class HistorialRepository {

    // El usuario de prueba que tienes en Mongo
    private val miUsuarioId = "test-user-001"

    // 1. Obtener la lista
    suspend fun obtenerMisConversaciones(): List<Conversacion> {
        return try {
            Log.d("CHAT_API", "Pidiendo historial para: $miUsuarioId")
            val response = RetrofitClient.conversacionApi.obtenerHistorial(miUsuarioId)

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

    // 2. Crear nuevo chat (Lapicito)
    suspend fun crearNuevoChat(): Conversacion? {
        return try {
            // Creamos un objeto simple solo con el ID del usuario
            // El backend pondrá la fecha y el ID automáticamente
            val nuevoChat = Conversacion(usuarioId = miUsuarioId)

            val response = RetrofitClient.conversacionApi.crearConversacion(nuevoChat)

            if (response.isSuccessful) {
                Log.d("CHAT_API", "¡Chat creado!")
                response.body()
            } else {
                Log.e("CHAT_API", "Error al crear: ${response.code()}")
                null
            }
        } catch (e: Exception) {
            Log.e("CHAT_API", "Error conexión al crear: ${e.message}")
            null
        }


    }
}