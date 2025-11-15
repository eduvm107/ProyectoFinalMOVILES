package com.example.chatbot_diseo.presentation.chat

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel

class ChatViewModel : ViewModel() {

    val mensajes = mutableStateListOf<Mensaje>()

    val sugerencias = listOf(
        "Ver mi calendario 📅",
        "Revisar documentos 📄",
        "Consultar cursos 🎓",
        "Hablar con RRHH 💬"
    )

    init {
        mensajes.add(Mensaje("¡Nuevo chat iniciado! 👋 ¿En qué puedo ayudarte ahora?", false))
    }

    fun enviarMensaje(texto: String) {
        if (texto.isBlank()) return

        mensajes.add(Mensaje(texto, true))

        mensajes.add(
            Mensaje("Estoy procesando tu solicitud sobre \"$texto\"…", false)
        )
    }

    fun limpiarChat() {
        mensajes.clear()
        mensajes.add(Mensaje("¡Nuevo chat iniciado! 👋 ¿En qué puedo ayudarte ahora?", false))
    }
}
