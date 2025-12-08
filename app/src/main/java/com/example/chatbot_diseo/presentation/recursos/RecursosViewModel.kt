package com.example.chatbot_diseo.presentation.recursos

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chatbot_diseo.data.api.TokenHolder
import com.example.chatbot_diseo.data.remote.apiChatBot.RetrofitInstance
import com.example.chatbot_diseo.data.remote.model.Documento
import com.example.chatbot_diseo.network.dto.request.FavoritoRequest
import com.example.chatbot_diseo.presentation.favoritos.FavoritosViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

/**
 * ViewModel para manejar recursos y favoritos
 */
class RecursosViewModel : ViewModel() {

    private val favoritosApi = RetrofitInstance.favoritosApi
    private val documentosApi = RetrofitInstance.documentosApi

    // 🎯 PASO 1: Lista Observable de Documentos
    private val _recursosList = MutableStateFlow<List<Documento>>(emptyList())
    val recursosList: StateFlow<List<Documento>> = _recursosList

    // Estado de carga
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    // Mensaje de error
    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    // StateFlow para mensajes de feedback
    private val _mensajeFeedback = MutableStateFlow<String?>(null)
    val mensajeFeedback: StateFlow<String?> = _mensajeFeedback

    init {
        cargarRecursos()
    }

    /**
     * Cargar recursos desde la API
     */
    fun cargarRecursos() {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null

            try {
                Log.d("RECURSOS", "🔄 Cargando recursos desde API...")
                Log.d("RECURSOS", "📍 URL: GET /api/Documento")

                val documentos = documentosApi.getAllDocumentos()

                Log.d("RECURSOS", "✅ Recursos cargados: ${documentos.size}")

                // 🔍 LOG DETALLADO: Mostrar el primer documento completo para ver estructura
                if (documentos.isNotEmpty()) {
                    val primerDoc = documentos.first()
                    Log.d("RECURSOS_JSON", "═══════════════════════════════════════")
                    Log.d("RECURSOS_JSON", "📄 PRIMER DOCUMENTO DESERIALIZADO:")
                    Log.d("RECURSOS_JSON", "  - id: ${primerDoc.id}")
                    Log.d("RECURSOS_JSON", "  - titulo: ${primerDoc.titulo}")
                    Log.d("RECURSOS_JSON", "  - descripcion: ${primerDoc.descripcion}")
                    Log.d("RECURSOS_JSON", "  - url: ${primerDoc.url}")
                    Log.d("RECURSOS_JSON", "  - categoria: ${primerDoc.categoria}")
                    Log.d("RECURSOS_JSON", "  - tipo: ${primerDoc.tipo}")
                    Log.d("RECURSOS_JSON", "  - favorito: ${primerDoc.favorito}")
                    Log.d("RECURSOS_JSON", "  - autor: ${primerDoc.autor}")
                    Log.d("RECURSOS_JSON", "  - fechaPublicacion: ${primerDoc.fechaPublicacion}")
                    Log.d("RECURSOS_JSON", "═══════════════════════════════════════")

                    // Mostrar todos los documentos con sus IDs
                    documentos.forEachIndexed { index, doc ->
                        Log.d("RECURSOS_JSON", "📋 Documento #${index + 1}: id=${doc.id}, titulo=${doc.titulo}, favorito=${doc.favorito}")
                    }
                }

                _recursosList.value = documentos

                // 🎯 CARGAR ESTADO DE FAVORITOS DESDE LA API
                cargarEstadoFavoritos()
            } catch (e: Exception) {
                Log.e("RECURSOS", "❌ Error cargando recursos: ${e.message}")
                Log.e("RECURSOS", "❌ Stack trace: ", e)
                _errorMessage.value = "Error: ${e.message ?: "Desconocido"}"
                _recursosList.value = emptyList()
            } finally {
                _isLoading.value = false
            }
        }
    }

    /**
     * Cargar el estado inicial de favoritos desde la API
     * Esto sincroniza los corazones rojos con lo que está en la base de datos
     */
    private suspend fun cargarEstadoFavoritos() {
        val usuarioId = TokenHolder.usuarioId
        if (usuarioId.isNullOrEmpty()) {
            Log.d("RECURSOS", "⚠️ Sin usuario, no se cargan favoritos")
            return
        }

        try {
            Log.d("RECURSOS", "🔄 Sincronizando estado de favoritos...")

            // Obtener lista de favoritos del usuario
            val response = favoritosApi.getMisFavoritos(usuarioId)

            if (response.isSuccessful) {
                val favoritos = response.body() ?: emptyList()

                // Extraer los IDs de los recursos favoritos
                val idsFavoritos = favoritos.map { it.id }.toSet()

                Log.d("RECURSOS", "✅ ${idsFavoritos.size} favoritos encontrados")

                // Actualizar los documentos marcando cuáles son favoritos
                val listaActualizada = _recursosList.value.map { doc ->
                    val esFavorito = doc.id in idsFavoritos
                    if (doc.favorito != esFavorito) {
                        Log.d("RECURSOS", "  ❤️ Actualizando: ${doc.titulo} -> favorito=$esFavorito")
                        doc.copy(favorito = esFavorito)
                    } else {
                        doc
                    }
                }

                _recursosList.value = listaActualizada
                Log.d("RECURSOS", "🎨 Corazones sincronizados con la base de datos")
            } else {
                Log.e("RECURSOS", "❌ Error al obtener favoritos: ${response.code()}")
            }
        } catch (e: Exception) {
            Log.e("RECURSOS", "❌ Error sincronizando favoritos: ${e.message}")
        }
    }

    /**
     * Toggle favorito - Agregar o eliminar de favoritos
     * @param usuarioId ID del usuario
     * @param recurso Documento a marcar/desmarcar como favorito
     * @param favoritosViewModel ViewModel de favoritos para sincronización
     */
    fun toggleFavorito(usuarioId: String, recurso: Documento, favoritosViewModel: FavoritosViewModel? = null) {
        val TIPO_RECURSO_API = "documento"

        // 🔍 LOG DEBUG CRÍTICO: Verificar valores recibidos
        Log.d("FAVORITOS_DEBUG", "═══════════════════════════════════════")
        Log.d("FAVORITOS_DEBUG", "🎯 toggleFavorito llamado")
        Log.d("FAVORITOS_DEBUG", "📝 Usuario ID: $usuarioId")
        Log.d("FAVORITOS_DEBUG", "📄 Recurso ID: ${recurso.id}")
        Log.d("FAVORITOS_DEBUG", "📄 Recurso Titulo: ${recurso.titulo}")
        Log.d("FAVORITOS_DEBUG", "❤️ Recurso Favorito actual: ${recurso.favorito}")
        Log.d("FAVORITOS_DEBUG", "🔍 ID es null? ${recurso.id == null}")
        Log.d("FAVORITOS_DEBUG", "🔍 ID es empty? ${recurso.id?.isEmpty()}")
        Log.d("FAVORITOS_DEBUG", "🔍 ID length: ${recurso.id?.length ?: 0}")
        Log.d("FAVORITOS_DEBUG", "═══════════════════════════════════════")

        // 1. 🛡️ VALIDACIÓN (Previene Error 400 Bad Request)
        if (usuarioId.isEmpty() || recurso.id.isNullOrEmpty()) {
            Log.e("FAVORITOS", "❌ Error: ID de usuario o recurso nulo, el POST fallará.")
            Log.e("FAVORITOS", "   - usuarioId.isEmpty(): ${usuarioId.isEmpty()}")
            Log.e("FAVORITOS", "   - recurso.id.isNullOrEmpty(): ${recurso.id.isNullOrEmpty()}")
            _mensajeFeedback.value = "Error: ID de recurso inválido"
            return
        }

        viewModelScope.launch {
            try {
                val request = FavoritoRequest(
                    tipoRecurso = TIPO_RECURSO_API,
                    recursoId = recurso.id
                )

                Log.d("FAVORITOS", "📤 Enviando POST /Usuario/$usuarioId/favoritos")
                Log.d("FAVORITOS", "Body: tipoRecurso=$TIPO_RECURSO_API, recursoId=${recurso.id}")

                // 2. LLAMADA POST a la DB
                val response = favoritosApi.toggleFavorito(usuarioId, request)

                if (response.isSuccessful) {
                    val body = response.body()
                    Log.d("FAVORITOS", "✅ Respuesta exitosa: ${body?.message}")

                    // 3. 🎨 FIX CRÍTICO: ACTUALIZACIÓN LOCAL INMEDIATA (Arregla el color del corazón)
                    val listaActual = _recursosList.value.toMutableList()

                    // Buscar el índice del documento que acabamos de modificar
                    val index = listaActual.indexOfFirst { it.id == recurso.id }

                    // Variable para guardar el nuevo estado y usarla en el mensaje
                    var nuevoEstado = !recurso.favorito

                    if (index != -1) {
                        // Invertir el estado actual (true <-> false)
                        nuevoEstado = !recurso.favorito
                        val documentoActualizado = listaActual[index].copy(
                            favorito = nuevoEstado
                        )

                        // Reemplazar el documento en la lista
                        listaActual[index] = documentoActualizado

                        // Emitir la nueva lista para refrescar la UI (el corazón cambia de color)
                        _recursosList.value = listaActual

                        Log.d("FAVORITOS", "🎨 Lista local actualizada: ${recurso.titulo} en posición $index -> favorito=$nuevoEstado")
                    }

                    // 4. 🔄 SINCRONIZACIÓN: Llamada al "Jale" (Arregla la persistencia en Mis Favoritos)
                    favoritosViewModel?.forzarRecarga(usuarioId)

                    // Mostrar feedback al usuario usando el nuevo estado local
                    _mensajeFeedback.value = if (nuevoEstado) {
                        "✅ Agregado a favoritos"
                    } else {
                        "❌ Eliminado de favoritos"
                    }

                    Log.d("FAVORITOS", "🔄 Sincronización con pantalla de favoritos completada")
                } else {
                    // Manejar Error 400 (si el ID pasa la validación local pero el server lo rechaza)
                    Log.e("FAVORITOS", "❌ Error ${response.code()} al actualizar favorito")
                    Log.e("FAVORITOS", "Response: ${response.errorBody()?.string()}")
                    _mensajeFeedback.value = "Error al actualizar favorito (${response.code()})"
                }
            } catch (e: Exception) {
                Log.e("FAVORITOS", "❌ Excepción de red al hacer POST: ${e.message}")
                e.printStackTrace()
                _mensajeFeedback.value = "Error de conexión: ${e.message}"
            }
        }
    }

    /**
     * Verifica si un recurso es favorito
     * @param recursoId ID del recurso
     * @return true si es favorito, false si no
     */
    fun esFavorito(recursoId: String?): Boolean {
        if (recursoId == null) return false
        val documento = _recursosList.value.find { it.id == recursoId }
        return documento?.favorito ?: false
    }

    /**
     * Limpia el mensaje de feedback
     */
    fun limpiarMensaje() {
        _mensajeFeedback.value = null
    }

    /**
     * Recargar recursos manualmente
     */
    fun recargarRecursos() {
        cargarRecursos()
    }
}
