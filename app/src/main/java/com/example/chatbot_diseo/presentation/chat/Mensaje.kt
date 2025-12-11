package com.example.chatbot_diseo.presentation.chat

import androidx.compose.runtime.Immutable

// Texto especial para representar el estado "escribiendo..." del bot
// ⚠️ IMPORTANTE: Este valor debe coincidir con el usado en ChatViewModel
const val TYPING_MESSAGE_TEXT = "escribiendo..."

@Immutable
data class Mensaje(
    val texto: String,
    val esUsuario: Boolean,
    val textoAccion: String? = null,       // Texto del botón (opcional)
    val accion: (() -> Unit)? = null,      // Acción al presionar el botón (opcional)
    val actionRoute: String? = null        // Ruta de navegación (opcional, preferida sobre `accion` desde UI)
)
