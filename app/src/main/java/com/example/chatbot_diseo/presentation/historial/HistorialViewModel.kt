package com.example.chatbot_diseo.presentation.historial

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chatbot_diseo.data.model.Conversacion // Asegúrate de la ruta correcta
import com.example.chatbot_diseo.data.repository.HistorialRepository // Asegúrate de la ruta correcta
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import android.util.Log

// 💡 Nota: Asegúrate de tener una forma de inyectar o pasar el HistorialRepository.
class HistorialViewModel(
    private val repository: HistorialRepository,
    private val usuarioId: String
) : ViewModel() {

    // Lista de conversaciones mostrada en el RecyclerView / Compose
    private val _conversaciones = MutableStateFlow<List<Conversacion>>(emptyList())
    val conversaciones: StateFlow<List<Conversacion>> = _conversaciones

    // Alias para mantener compatibilidad con el uso en UI (ej. viewModel.chats)
    val chats: StateFlow<List<Conversacion>>
        get() = conversaciones

    // Indicador de si se está cargando la información (para mostrar un spinner)
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    // Propiedad para enviar mensajes de error o éxito a la UI (ej. Toast)
    private val _uiEvent = MutableStateFlow<String?>(null)
    val uiEvent: StateFlow<String?> = _uiEvent

    init {
        cargarHistorial()
    }

    /**
     * Carga el historial de conversaciones del usuario desde el repositorio.
     */
    fun cargarHistorial() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                // 1. Obtener los datos del Repository usando el usuarioId inyectado
                val lista = repository.obtenerMisConversaciones(usuarioId)

                Log.d("HISTORIAL_VM", "Historial recibido: total=${lista.size} para usuarioId solicitada=$usuarioId")

                // Filtrado defensivo: asegurarnos que solo queden conversaciones del usuario actual
                val listaFiltrada = lista.filter { it.usuarioId == usuarioId }

                Log.d("HISTORIAL_VM", "Después de filtrar: filtradas=${listaFiltrada.size}")

                // 2. Ordenar la lista por fecha de inicio (más nuevo primero)
                val listaOrdenada = listaFiltrada.sortedByDescending { it.fechaInicio }

                // 3. Actualizar el StateFlow para refrescar la UI
                _conversaciones.value = listaOrdenada

                // Si hubo discrepancias, notificar (opcional)
                if (listaFiltrada.size < lista.size) {
                    _uiEvent.value = "Algunas conversaciones fueron descartadas por no coincidir con el usuario actual"
                }

            } catch (e: Exception) {
                // Manejo de errores de red o repositorio
                _uiEvent.value = "Error al cargar historial: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    /**
     * Llama al repositorio para crear una nueva conversación en el backend.
     */
    fun crearNuevoChat() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                // El repositorio crea la conversación y devuelve el objeto completo
                val nuevoChat = repository.crearNuevoChat(usuarioId)

                if (nuevoChat != null) {
                    _uiEvent.value = "Nuevo chat creado exitosamente"
                    cargarHistorial() // Recargar la lista para mostrar el nuevo chat

                    // TODO: Puedes agregar lógica para navegar automáticamente al nuevo chat aquí.
                } else {
                    _uiEvent.value = "Error al crear nuevo chat."
                }
            } catch (e: Exception) {
                _uiEvent.value = "Excepción al crear chat: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    /**
     * Se debe llamar desde la UI (Activity/Fragment) después de mostrar un Toast o Snackbar.
     */
    fun uiEventConsumed() {
        _uiEvent.value = null
    }
}