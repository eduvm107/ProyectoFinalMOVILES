package com.example.chatbot_diseo.presentation.favoritos

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chatbot_diseo.data.common.Result
import com.example.chatbot_diseo.data.model.RecursoFavorito
import com.example.chatbot_diseo.data.repository.FavoritosRepository
import com.example.chatbot_diseo.data.api.TokenHolder
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class FavoritosViewModel(
    private val repository: FavoritosRepository = FavoritosRepository()
) : ViewModel() {

    // Lista de favoritos (inicialmente vacía)
    private val _favoritos = MutableStateFlow<List<RecursoFavorito>>(emptyList())
    val favoritos: StateFlow<List<RecursoFavorito>> = _favoritos.asStateFlow()

    // Estado de carga para UI
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    // Mensaje para Snackbar (single message) - null cuando no hay mensaje
    private val _snackbarMessage = MutableStateFlow<String?>(null)
    val snackbarMessage: StateFlow<String?> = _snackbarMessage.asStateFlow()

    init {
        cargarFavoritos()
    }

    private fun cargarFavoritos() {
        viewModelScope.launch {
            _isLoading.value = true
            val usuarioId = TokenHolder.usuarioId
            if (usuarioId.isNullOrBlank()) {
                // Fallback: datos mock para desarrollo
                _favoritos.value = listOf(
                    RecursoFavorito("1", "Manual", "Manual de Bienvenida", "Todo lo que necesitas saber", null, null, true),
                    RecursoFavorito("2", "Beneficio", "Descuento en Gimnasio", "Código: TCSFIT2025", null, null, true),
                    RecursoFavorito("3", "Curso", "Guía de Java Avanzado", "PDF para capacitación", null, null, true)
                )
                _isLoading.value = false
                return@launch
            }

            val res = repository.getMisFavoritos(usuarioId)
            if (res is Result.Success) {
                _favoritos.value = res.data ?: emptyList()
            } else if (res is Result.Error) {
                _favoritos.value = emptyList()
                _snackbarMessage.value = "Error al cargar favoritos"
            } else {
                // Loading u otros: fallback
                _favoritos.value = emptyList()
            }

            _isLoading.value = false
        }
    }

    /**
     * Toggle favorito: llama al endpoint y si tiene éxito elimina el ítem localmente
     */
    fun toggleFavoritoLocalAndRemote(recurso: RecursoFavorito) {
        viewModelScope.launch {
            val usuarioId = TokenHolder.usuarioId
            // Optimistic update: remover localmente
            val current = _favoritos.value.toMutableList()
            current.removeAll { it.id == recurso.id }
            _favoritos.value = current

            // Si no hay usuario, sólo hacemos el cambio local y mostramos mensaje
            if (usuarioId.isNullOrBlank()) {
                _snackbarMessage.value = "Favorito actualizado (offline)"
                return@launch
            }

            _isLoading.value = true
            val res = repository.toggleFavorito(usuarioId, recurso.tipo, recurso.id)
            if (res is Result.Success) {
                _snackbarMessage.value = "Favorito actualizado"
            } else if (res is Result.Error) {
                // Revertir en caso de fallo: volver a agregar
                val reverted = _favoritos.value.toMutableList()
                reverted.add(0, recurso)
                _favoritos.value = reverted
                _snackbarMessage.value = "Error al actualizar favorito"
            }
            _isLoading.value = false
        }
    }

    /**
     * Nueva función pública: toggle central que puede llamarse desde cualquier UI pasando tipo+id.
     * Ejecuta la llamada remota y llama al callback con el Result.
     */
    fun toggleFavorito(tipoRecurso: String, recursoId: String, onResult: (Result<Unit>) -> Unit = {}) {
        viewModelScope.launch {
            val usuarioId = TokenHolder.usuarioId
            if (usuarioId.isNullOrBlank()) {
                onResult(Result.Error("Usuario no autenticado"))
                return@launch
            }
            _isLoading.value = true
            val res = repository.toggleFavorito(usuarioId, tipoRecurso, recursoId)
            onResult(res)
            if (res is Result.Success) {
                _snackbarMessage.value = "Favorito actualizado"
                // Opcional: refrescar lista local de favoritos
                cargarFavoritos()
            } else if (res is Result.Error) {
                _snackbarMessage.value = "Error al actualizar favorito"
            }
            _isLoading.value = false
        }
    }

    // Función pública (usada por UI) para eliminar favorito
    fun eliminarFavorito(id: String) {
        val item = _favoritos.value.firstOrNull { it.id == id } ?: return
        toggleFavoritoLocalAndRemote(item)
    }

    // Limpiar mensaje de snackbar después de mostrarlo en UI
    fun clearSnackbarMessage() {
        _snackbarMessage.value = null
    }
}