package com.example.chatbot_diseo.data.repository

import android.util.Log
import com.example.chatbot_diseo.data.model.MensajeAutomatico
import com.example.chatbot_diseo.data.remote.apiChatBot.RetrofitInstance

/**
 * Repositorio para gestión de mensajes automáticos/notificaciones
 */
class MensajesRepository {

    private val api = RetrofitInstance.mensajeApi

    /**
     * Obtener todas las notificaciones/mensajes automáticos
     */
    suspend fun obtenerNotificaciones(): List<MensajeAutomatico> {
        return try {
            Log.d("MENSAJES_API", "Obteniendo mensajes automáticos...")
            val respuesta = api.getMensajes()
            if (respuesta.isSuccessful) {
                val lista = respuesta.body() ?: emptyList()
                Log.d("MENSAJES_API", "Recibidos ${lista.size} mensajes")
                lista
            } else {
                Log.e("MENSAJES_API", "Error: ${respuesta.code()}")
                emptyList()
            }
        } catch (e: Exception) {
            Log.e("MENSAJES_API", "Error conexión: ${e.message}")
            emptyList()
        }
    }

    /**
     * Obtener mensajes automáticos activos
     */
    suspend fun obtenerMensajesActivos(): List<MensajeAutomatico> {
        return try {
            val respuesta = api.getMensajesActivos()
            if (respuesta.isSuccessful) {
                respuesta.body() ?: emptyList()
            } else {
                emptyList()
            }
        } catch (e: Exception) {
            Log.e("MENSAJES_API", "Error: ${e.message}")
            emptyList()
        }
    }

    /**
     * Obtener mensajes por tipo
     */
    suspend fun obtenerMensajesPorTipo(tipo: String): List<MensajeAutomatico> {
        return try {
            val respuesta = api.getMensajesByTipo(tipo)
            if (respuesta.isSuccessful) {
                respuesta.body() ?: emptyList()
            } else {
                emptyList()
            }
        } catch (e: Exception) {
            Log.e("MENSAJES_API", "Error: ${e.message}")
            emptyList()
        }
    }
}