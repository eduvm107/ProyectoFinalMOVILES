//este es para el chatttttttttttttttt
package com.example.chatbot_diseo.data.repository

import com.example.chatbot_diseo.data.model.MensajeAutomatico
import com.example.chatbot_diseo.data.remote.RetrofitClient

class MensajesRepository {

    suspend fun obtenerNotificaciones(): List<MensajeAutomatico> {
        return try {
            val respuesta = RetrofitClient.api.getMensajes()
            if (respuesta.isSuccessful) {
                respuesta.body() ?: emptyList()
            } else {
                emptyList()
            }
        } catch (e: Exception) {
            emptyList()
        }
    }
}