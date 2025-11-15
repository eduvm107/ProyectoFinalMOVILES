package com.example.chatbot_diseo.presentation.historial

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import java.text.SimpleDateFormat
import java.util.*

class HistorialViewModel : ViewModel() {

    var historial by mutableStateOf(listOf<HistorialItem>())
        private set

    fun agregarRegistro(texto: String) {
        val fecha = obtenerFechaActual()
        val nuevo = HistorialItem(
            id = historial.size + 1,
            mensaje = texto,
            fecha = fecha
        )
        historial = historial + nuevo
    }

    private fun obtenerFechaActual(): String {
        val formato = SimpleDateFormat("dd MMM yyyy - HH:mm", Locale.getDefault())
        return formato.format(Date())
    }

    fun limpiarHistorial() {
        historial = emptyList()
    }
}
