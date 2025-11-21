package com.example.chatbot_diseo.presentation.favoritos

import androidx.lifecycle.ViewModel
import com.example.chatbot_diseo.data.model.RecursoFavorito
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class FavoritosViewModel : ViewModel() {

    // Lista de favoritos (Simulada en memoria)
    private val _favoritos = MutableStateFlow<List<RecursoFavorito>>(emptyList())
    val favoritos: StateFlow<List<RecursoFavorito>> = _favoritos

    init {
        cargarFavoritos()
    }

    private fun cargarFavoritos() {
        // DATOS DE PRUEBA (Para que tu pantalla se vea bonita de una vez)
        _favoritos.value = listOf(
            RecursoFavorito("1", "Manual de Bienvenida", "Todo lo que necesitas saber", "Manual"),
            RecursoFavorito("2", "Descuento en Gimnasio", "Código: TCSFIT2025", "Beneficio"),
            RecursoFavorito("3", "Guía de Java Avanzado", "PDF para capacitación", "Curso")
        )
    }

    // Función para eliminar de favoritos (Corazón roto)
    fun eliminarFavorito(id: String) {
        val listaActual = _favoritos.value.toMutableList()
        listaActual.removeAll { it.id == id }
        _favoritos.value = listaActual
    }
}