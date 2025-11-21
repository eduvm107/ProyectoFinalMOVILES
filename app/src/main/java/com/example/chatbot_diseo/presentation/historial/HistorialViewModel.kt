package com.example.chatbot_diseo.presentation.historial

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chatbot_diseo.data.model.Conversacion
import com.example.chatbot_diseo.data.repository.HistorialRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class HistorialViewModel : ViewModel() {

    private val repository = HistorialRepository()

    private val _chats = MutableStateFlow<List<Conversacion>>(emptyList())
    val chats: StateFlow<List<Conversacion>> = _chats

    init {
        cargarDatos()
    }

    fun cargarDatos() {
        viewModelScope.launch {
            // Obtenemos y ordenamos por fecha (el más nuevo arriba)
            // Nota: Si te sale error en 'sortedByDescending', asegúrate de que 'fechaInicio' existe en tu modelo
            val lista = repository.obtenerMisConversaciones().sortedByDescending { it.fechaInicio }
            _chats.value = lista
        }
    }

    fun crearNuevoChat() {
        viewModelScope.launch {
            // CORRECCIÓN AQUÍ 👇
            // El repositorio ahora devuelve 'Conversacion?' (puede ser null)
            // Asegúrate de que en tu Repository la función se llame 'crearNuevoChat'
            val nuevoChat = repository.crearNuevoChat()

            // Verificamos si NO es nulo (es decir, si se creó bien)
            if (nuevoChat != null) {
                cargarDatos() // Recargamos la lista para ver el nuevo chat
            }
        }
    }
}