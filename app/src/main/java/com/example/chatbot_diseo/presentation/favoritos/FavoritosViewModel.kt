package com.example.chatbot_diseo.presentation.favoritos

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chatbot_diseo.data.api.TokenHolder
import com.example.chatbot_diseo.data.model.RecursoFavorito
import com.example.chatbot_diseo.data.remote.apiChatBot.RetrofitInstance
import com.example.chatbot_diseo.network.dto.request.FavoritoRequest
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class FavoritosViewModel : ViewModel() {
    private val api = RetrofitInstance.favoritosApi

    // Lista de favoritos desde la API
    private val _favoritos = MutableStateFlow<List<RecursoFavorito>>(emptyList())
    val favoritos: StateFlow<List<RecursoFavorito>> = _favoritos

    // Estado de carga
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    // Mensaje de error
    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    // Mensaje de feedback para Toast
    private val _mensajeFeedback = MutableStateFlow<String?>(null)
    val mensajeFeedback: StateFlow<String?> = _mensajeFeedback

    init {
        // Cargar favoritos automáticamente al iniciar
        val usuarioId = TokenHolder.usuarioId
        if (!usuarioId.isNullOrEmpty()) {
            obtenerFavoritos(usuarioId)
        }

        // Suscribirse al bus de favoritos para recargar cuando alguien haga toggle
        viewModelScope.launch {
            FavoritosBus.events.collect {
                val uid = TokenHolder.usuarioId
                if (!uid.isNullOrEmpty()) {
                    Log.d("FAVORITOS_BUS", "Evento recibido - recargando favoritos para $uid")
                    obtenerFavoritos(uid)
                }
            }
        }
    }

    /**
     * Obtener lista de favoritos desde la API
     * GET /api/Usuario/{usuarioId}/favoritos
     */
    fun obtenerFavoritos(usuarioId: String) {
        // 1. 🛡️ Validación de Seguridad
        if (usuarioId.isNullOrEmpty()) {
            Log.e("FAVORITOS_GET", "ID de usuario es nulo. Abortando la carga de favoritos.")
            _errorMessage.value = "Inicia sesión para ver tus favoritos"
            _favoritos.value = emptyList()
            return
        }

        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null

            try {
                Log.d("FAVORITOS_GET", "📤 Cargando favoritos del usuario: $usuarioId")

                // 2. Llamada GET con el ID dinámico
                val response = api.getMisFavoritos(usuarioId)

                if (response.isSuccessful) {
                    val listaFavoritos = response.body() ?: emptyList()

                    // 3. ✅ Éxito: Actualizar StateFlow
                    _favoritos.value = listaFavoritos

                    Log.d("FAVORITOS_GET", "✅ Favoritos cargados: ${listaFavoritos.size} recursos")
                    listaFavoritos.forEach {
                        Log.d("FAVORITOS_GET", "  - [${it.tipo}] ${it.titulo}")
                    }
                } else {
                    // Manejar errores HTTP
                    val errorBody = response.errorBody()?.string()
                    Log.e("FAVORITOS_GET", "❌ Error ${response.code()}: $errorBody")
                    _errorMessage.value = "Error al cargar favoritos (${response.code()})"
                    _favoritos.value = emptyList()
                }
            } catch (e: Exception) {
                // Manejar errores de conexión
                Log.e("FAVORITOS_GET", "❌ Excepción de red: ${e.message}")
                e.printStackTrace()
                _errorMessage.value = "Error de conexión: ${e.message}"
                _favoritos.value = emptyList()
            } finally {
                _isLoading.value = false
            }
        }
    }

    /**
     * Toggle favorito usando la API (desfavoritear)
     * POST /api/Usuario/{usuarioId}/favoritos
     */
    fun toggleFavorito(usuarioId: String, recurso: RecursoFavorito) {
        // 1. 🛡️ Validación
        if (usuarioId.isNullOrEmpty() || recurso.id.isNullOrEmpty()) {
            Log.e("FAVORITOS_TOGGLE", "Error: ID de usuario o recurso nulo")
            _mensajeFeedback.value = "Error: ID de recurso inválido"
            return
        }

        viewModelScope.launch {
            try {
                val request = FavoritoRequest(
                    tipoRecurso = recurso.tipo,
                    recursoId = recurso.id
                )

                Log.d("FAVORITOS_TOGGLE", "📤 Toggle favorito: [${recurso.tipo}] ${recurso.titulo}")

                // 2. Llamada POST a la API
                val response = api.toggleFavorito(usuarioId, request)

                if (response.isSuccessful) {
                    val body = response.body()
                    val esFavorito = body?.esFavorito ?: false

                    Log.d("FAVORITOS_TOGGLE", "✅ Toggle exitoso: esFavorito=$esFavorito")

                    // 3. Si se eliminó de favoritos, quitarlo de la lista local
                    if (!esFavorito) {
                        val listaActual = _favoritos.value.toMutableList()
                        listaActual.removeAll { it.id == recurso.id }
                        _favoritos.value = listaActual

                        _mensajeFeedback.value = "❌ Eliminado de favoritos"
                        Log.d("FAVORITOS_TOGGLE", "🗑️ Recurso eliminado de la lista local")
                    } else {
                        _mensajeFeedback.value = "✅ Agregado a favoritos"
                    }
                } else {
                    Log.e("FAVORITOS_TOGGLE", "❌ Error ${response.code()}")
                    _mensajeFeedback.value = "Error al actualizar favorito (${response.code()})"
                }
            } catch (e: Exception) {
                Log.e("FAVORITOS_TOGGLE", "❌ Excepción: ${e.message}")
                e.printStackTrace()
                _mensajeFeedback.value = "Error de conexión: ${e.message}"
            }
        }
    }

    /**
     * Recargar favoritos manualmente
     */
    fun recargarFavoritos() {
        val usuarioId = TokenHolder.usuarioId
        if (!usuarioId.isNullOrEmpty()) {
            obtenerFavoritos(usuarioId)
        }
    }

    /**
     * Forzar recarga de favoritos (para sincronización desde otras pantallas)
     */
    fun forzarRecarga(usuarioId: String) {
        obtenerFavoritos(usuarioId)
    }

    /**
     * Limpiar mensaje de error
     */
    fun limpiarError() {
        _errorMessage.value = null
    }

    /**
     * Limpiar mensaje de feedback
     */
    fun limpiarMensaje() {
        _mensajeFeedback.value = null
    }
}