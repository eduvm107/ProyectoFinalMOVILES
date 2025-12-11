package com.example.chatbot_diseo.data.remote

import com.example.chatbot_diseo.data.model.FAQ
import com.example.chatbot_diseo.data.remote.apiChatBot.RetrofitInstance
import com.example.chatbot_diseo.presentation.ayuda.AyudaRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Implementación real del repositorio de Ayuda usando la API de FAQs.
 * Limita la lista a 5 ítems tal como solicita el requerimiento.
 */
class ApiAyudaRepository : AyudaRepository {
    override suspend fun getFaqs(): List<FAQ> = withContext(Dispatchers.IO) {
        val response = RetrofitInstance.faqApi.getAllFAQs()
        if (response.isSuccessful) {
            val body = response.body() ?: emptyList()
            return@withContext body.take(5)
        } else {
            throw Exception("Error al obtener FAQs: ${response.code()} ${response.message()}")
        }
    }
}

