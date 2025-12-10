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
     * Carga el historial de conversaciones del usuario desde el repositorio (usa usuarioId del VM).
     */
    fun cargarHistorial() {
        cargarHistorial(usuarioId)
    }

    /**
     * Carga el historial de conversaciones del usuario desde el repositorio usando el usuarioId provisto.
     * NUEVO: Sincroniza el estado de favoritos desde la API de favoritos
     */
    fun cargarHistorial(usuarioId: String) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                // 1. Obtener los datos del Repository usando el usuarioId proporcionado
                val lista = repository.obtenerMisConversaciones(usuarioId)

                Log.d("HISTORIAL_VM", "Historial recibido: total=${lista.size} para usuarioId solicitada=$usuarioId")

                // Filtrado defensivo: asegurarnos que solo queden conversaciones del usuario actual
                val listaFiltrada = lista.filter { it.usuarioId == usuarioId }

                Log.d("HISTORIAL_VM", "Después de filtrar: filtradas=${listaFiltrada.size}")

                // 2. 🔄 SINCRONIZAR FAVORITOS: Obtener la lista de favoritos del usuario
                try {
                    Log.d("HISTORIAL_VM", "🔄 Sincronizando estado de favoritos...")
                    val responseFavoritos = com.example.chatbot_diseo.data.remote.apiChatBot.RetrofitInstance.favoritosApi.getMisFavoritos(usuarioId)

                    if (responseFavoritos.isSuccessful) {
                        val favoritos = responseFavoritos.body() ?: emptyList()

                        // Extraer los IDs de las conversaciones favoritas (tipo="chat")
                        val idsFavoritosChat = favoritos
                            .filter { it.tipo.equals("chat", ignoreCase = true) }
                            .map { it.id }
                            .toSet()

                        Log.d("HISTORIAL_VM", "✅ ${idsFavoritosChat.size} conversaciones favoritas encontradas")

                        // 3. Actualizar las conversaciones marcando cuáles son favoritas
                        val listaConFavoritos = listaFiltrada.map { conv ->
                            val esFavorito = idsFavoritosChat.contains(conv.id)
                            if (conv.favorito != esFavorito) {
                                Log.d("HISTORIAL_VM", "  ❤️ Actualizando: ${conv.tituloMostrado} -> favorito=$esFavorito")
                                conv.copy(favorito = esFavorito)
                            } else {
                                conv
                            }
                        }

                        // 4. Ordenar la lista por fecha de inicio (más nuevo primero)
                        val listaOrdenada = listaConFavoritos.sortedByDescending { it.fechaInicio }

                        // 5. Actualizar el StateFlow para refrescar la UI
                        _conversaciones.value = listaOrdenada

                        Log.d("HISTORIAL_VM", "✅ Historial cargado y sincronizado con favoritos")
                    } else {
                        Log.e("HISTORIAL_VM", "❌ Error al obtener favoritos: ${responseFavoritos.code()}")
                        // Si falla la sincronización, al menos mostramos las conversaciones
                        val listaOrdenada = listaFiltrada.sortedByDescending { it.fechaInicio }
                        _conversaciones.value = listaOrdenada
                    }
                } catch (e: Exception) {
                    Log.e("HISTORIAL_VM", "❌ Error sincronizando favoritos: ${e.message}", e)
                    // Si falla la sincronización, al menos mostramos las conversaciones
                    val listaOrdenada = listaFiltrada.sortedByDescending { it.fechaInicio }
                    _conversaciones.value = listaOrdenada
                }

                // Si hubo discrepancias, notificar (opcional)
                if (listaFiltrada.size < lista.size) {
                    _uiEvent.value = "Algunas conversaciones fueron descartadas por no coincidir con el usuario actual"
                }

            } catch (e: Exception) {
                // Manejo de errores de red o repositorio
                Log.e("HISTORIAL_VM", "❌ Error al cargar historial: ${e.message}", e)
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

    /**
     * Eliminar una conversación: validación, llamada al repositorio en background y manejo de respuestas.
     * Se realiza eliminación optimista en la UI y se hace rollback en caso de error.
     */
    fun eliminarConversacion(conversacionId: String, usuarioId: String) {
        if (conversacionId.isBlank()) {
            _uiEvent.value = "ID de conversación inválido"
            return
        }

        viewModelScope.launch {
            _isLoading.value = true

            // Eliminación optimista: remover localmente para respuesta instantánea en UI
            val beforeList = _conversaciones.value
            _conversaciones.value = beforeList.filterNot { it.id == conversacionId }

            try {
                val resp = repository.eliminarConversacion(conversacionId)

                if (resp == null) {
                    // Error en la llamada
                    _uiEvent.value = "Error eliminando conversación. Intenta de nuevo."
                    // rollback
                    _conversaciones.value = beforeList
                } else {
                    when (resp.code()) {
                        204 -> {
                            _uiEvent.value = "Conversación eliminada"
                            // Recargar para sincronizar el estado desde el servidor usando el usuarioId provisto
                            cargarHistorial(usuarioId)
                        }
                        404 -> {
                            _uiEvent.value = "Conversación no encontrada (404)"
                            // Ya la removimos localmente; recargar para sincronizar
                            cargarHistorial(usuarioId)
                        }
                        in 500..599 -> {
                            _uiEvent.value = "Error de servidor (${resp.code()}). Intenta más tarde."
                            // rollback
                            _conversaciones.value = beforeList
                        }
                        else -> {
                            _uiEvent.value = "No se pudo eliminar la conversación (code=${resp.code()})"
                            _conversaciones.value = beforeList
                        }
                    }
                }
            } catch (e: Exception) {
                Log.e("HISTORIAL_VM", "excepción al eliminar conversacion id=$conversacionId", e)
                _uiEvent.value = "Excepción al eliminar: ${e.message}"
                // rollback
                _conversaciones.value = beforeList
            } finally {
                _isLoading.value = false
            }
        }
    }

    /**
     * Toggle favorito de conversación desde el historial
     * Usa el endpoint unificado POST /api/Usuario/{usuarioId}/favoritos con tipoRecurso="chat"
     * Lógica idéntica a RecursosViewModel.toggleFavorito()
     */
    fun toggleFavoritoConversacion(conversacionId: String, estadoActual: Boolean) {
        val TIPO_RECURSO_API = "chat"

        // 🔍 LOG DEBUG CRÍTICO: Verificar valores recibidos
        Log.d("FAVORITOS_HISTORIAL", "═══════════════════════════════════════")
        Log.d("FAVORITOS_HISTORIAL", "🎯 toggleFavoritoConversacion llamado")
        Log.d("FAVORITOS_HISTORIAL", "📝 Usuario ID: $usuarioId")
        Log.d("FAVORITOS_HISTORIAL", "💬 Conversación ID: $conversacionId")
        Log.d("FAVORITOS_HISTORIAL", "❤️ Estado actual: $estadoActual")
        Log.d("FAVORITOS_HISTORIAL", "🔍 ID es blank? ${conversacionId.isBlank()}")
        Log.d("FAVORITOS_HISTORIAL", "═══════════════════════════════════════")

        // 1. 🛡️ VALIDACIÓN (Previene Error 400 Bad Request)
        if (usuarioId.isEmpty() || conversacionId.isBlank()) {
            Log.e("FAVORITOS_HISTORIAL", "❌ Error: ID de usuario o conversación nulo")
            _uiEvent.value = "Error: ID de conversación inválido"
            return
        }

        viewModelScope.launch {
            try {
                val request = com.example.chatbot_diseo.network.dto.request.FavoritoRequest(
                    tipoRecurso = TIPO_RECURSO_API,
                    recursoId = conversacionId
                )

                Log.d("FAVORITOS_HISTORIAL", "📤 Enviando POST /Usuario/$usuarioId/favoritos")
                Log.d("FAVORITOS_HISTORIAL", "Body: tipoRecurso=$TIPO_RECURSO_API, recursoId=$conversacionId")

                // 2. LLAMADA POST a la API
                val response = com.example.chatbot_diseo.data.remote.apiChatBot.RetrofitInstance.favoritosApi.toggleFavorito(usuarioId, request)

                if (response.isSuccessful) {
                    val body = response.body()
                    Log.d("FAVORITOS_HISTORIAL", "✅ Respuesta exitosa: ${body?.message}")

                    // 3. 🎨 FIX CRÍTICO: ACTUALIZACIÓN LOCAL INMEDIATA (Arregla el color del corazón)
                    val listaActual = _conversaciones.value.toMutableList()

                    // Buscar el índice de la conversación que acabamos de modificar
                    val index = listaActual.indexOfFirst { it.id == conversacionId }

                    // Variable para guardar el nuevo estado y usarla en el mensaje
                    var nuevoEstado = !estadoActual

                    if (index != -1) {
                        // Invertir el estado actual (true <-> false)
                        nuevoEstado = !estadoActual
                        val conversacionActualizada = listaActual[index].copy(
                            favorito = nuevoEstado
                        )

                        // Reemplazar la conversación en la lista
                        listaActual[index] = conversacionActualizada

                        // Emitir la nueva lista para refrescar la UI (el corazón cambia de color)
                        _conversaciones.value = listaActual

                        Log.d("FAVORITOS_HISTORIAL", "🎨 Lista local actualizada: Conversación en posición $index -> favorito=$nuevoEstado")
                    }

                    // 4. 🔄 SINCRONIZACIÓN: Notificar al FavoritosBus para que recargue la pantalla de favoritos
                    com.example.chatbot_diseo.presentation.favoritos.FavoritosBus.emitFavoritosChanged()

                    // Mostrar feedback al usuario usando el nuevo estado local
                    _uiEvent.value = if (nuevoEstado) {
                        "✅ Agregado a favoritos"
                    } else {
                        "❌ Eliminado de favoritos"
                    }

                    Log.d("FAVORITOS_HISTORIAL", "✅ Toggle favorito completado exitosamente")

                } else {
                    val errorBody = response.errorBody()?.string()
                    Log.e("FAVORITOS_HISTORIAL", "❌ Error ${response.code()}: $errorBody")
                    _uiEvent.value = "Error al actualizar favorito (${response.code()})"
                }
            } catch (e: Exception) {
                Log.e("FAVORITOS_HISTORIAL", "❌ Excepción: ${e.message}", e)
                e.printStackTrace()
                _uiEvent.value = "Error de conexión: ${e.message}"
            }
        }
    }
}