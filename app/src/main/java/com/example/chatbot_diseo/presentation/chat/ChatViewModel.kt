package com.example.chatbot_diseo.presentation.chat

import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chatbot_diseo.data.api.TokenHolder
import com.example.chatbot_diseo.data.remote.apiChatBot.RetrofitInstance
import com.example.chatbot_diseo.data.repository.ChatbotRepository
import com.example.chatbot_diseo.data.model.MensajeRequest
import com.example.chatbot_diseo.data.model.Mensaje as DataMensaje
import com.example.chatbot_diseo.data.model.Conversacion as DataConversacion
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

class ChatViewModel : ViewModel() {

    companion object {
        private const val TAG = "ChatViewModel"
        const val TYPING_MESSAGE_TEXT = "escribiendo..."
    }

    private val chatbotRepository = ChatbotRepository()

    // Mutex para asegurar que la creación de la conversación (POST /api/Conversacion) solo
    // se dispare una vez cuando varios mensajes se envían casi al mismo tiempo.
    private val crearConversacionMutex = Mutex()

    val mensajes = mutableStateListOf<Mensaje>()
    val isLoading = mutableStateOf(false)
    val error = mutableStateOf<String?>(null)

    // Controlar visibilidad de FAQs / sugerencias
    val mostrarSugerencias = mutableStateOf(true)

    // Estado para mostrar diálogo de confirmación al crear nuevo chat
    val mostrarDialogoNuevoChat = mutableStateOf(false)

    // ID del usuario actual - se obtiene de TokenHolder después del login
    val usuarioId: String?
        get() = TokenHolder.usuarioId

    // ConversacionId como StateFlow + propiedad de compatibilidad
    private val _conversacionId = MutableStateFlow<String?>(null)
    val conversacionIdState: StateFlow<String?> = _conversacionId

    var conversacionId: String?
        get() = _conversacionId.value
        set(value) { _conversacionId.value = value }

    // Estado de favorito de la conversacion actual (nullable hasta cargar)
    var conversacionFavorito: Boolean? = null

    // Preguntas sugeridas del chatbot (visibles en UI, se pueden ir removiendo)
    val sugerencias = mutableStateListOf(
        "❓ ¿Qué es el onboarding?",
        "📄 ¿Dónde veo mis documentos?",
        "📋 ¿Qué actividades debo completar?",
        "👤 ¿Cómo contacto a mi supervisor?",
        "📱 ¿Qué puedo hacer en esta aplicación?"
    )

    // Respuestas fijas a preguntas sugeridas (versión corta)
    private val respuestasPredefinidas = mapOf(

        "¿Qué es el onboarding?" to Mensaje(
            texto = "El onboarding es el proceso donde te integras a TCS. Aquí completas tus documentos, actividades y recibes la información inicial necesaria para tu ingreso.",
            esUsuario = false
        ),

        "¿Dónde veo mis documentos?" to Mensaje(
            texto = "Tus documentos cargados están en la sección Documentos. Ahí podrás revisar lo que ya enviaste y lo que aún te falta.\n\nPresiona aquí para ir directo:",
            esUsuario = false,
            textoAccion = "Ir a Documentos",
            accion = null,
            actionRoute = "recursos"
        ),

        "¿Qué actividades debo completar?" to Mensaje(
            texto = "Tus actividades del onboarding aparecen en la sección Actividades. Ahí ves tus tareas pendientes, completadas y su detalle.\n\nIngresa aquí:",
            esUsuario = false,
            textoAccion = "Ver Actividades",
            accion = null,
            actionRoute = "calendario"
        ),

        "¿Cómo contacto a mi supervisor?" to Mensaje(
            texto = "Puedes ver a tu supervisor en tu perfil. Encontrarás su nombre, área y correo institucional para contactarlo fácilmente.\n\nToca aquí para ir:",
            esUsuario = false,
            textoAccion = "Ver Supervisor",
            accion = null,
            actionRoute = "perfil"
        ),

        "¿Qué puedo hacer en esta aplicación?" to Mensaje(
            texto = "Aquí puedes revisar tus documentos, ver tus actividades, consultar tu información personal y usar el asistente para resolver dudas del onboarding.",
            esUsuario = false
        )
    )

    // Lista de sugerencias inicial (para restaurar cuando se limpia el chat)
    private val sugerenciasIniciales = listOf(
        "❓ ¿Qué es el onboarding?",
        "📄 ¿Dónde veo mis documentos?",
        "📋 ¿Qué actividades debo completar?",
        "👤 ¿Cómo contacto a mi supervisor?",
        "📱 ¿Qué puedo hacer en esta aplicación?"
    )

    init {
        mensajes.add(Mensaje("¡Hola! Soy tu asistente virtual. ¿En qué puedo ayudarte hoy?", false))
    }

    var navegarADocumentos: (() -> Unit)? = null
    var navegarAActividades: (() -> Unit)? = null
    var navegarAPerfil: (() -> Unit)? = null

    // --- NUEVAS FUNCIONES RELACIONADAS A CONVERSACIONES ---

    private fun ahoraIso(): String {
        return OffsetDateTime.now().format(DateTimeFormatter.ISO_OFFSET_DATE_TIME)
    }

    /**
     * Crear una conversación vacía en el backend (sin mensajes) y guardar el id en el ViewModel.
     * ⚠️ YA NO SE USA - El botón de lápiz ahora solo limpia el chat localmente
     */
    fun crearConversacionVacia() {
        // ❌ YA NO CREAMOS conversación vacía manualmente
        // Solo limpiamos el chat localmente
        Log.d(TAG, "⚠️ crearConversacionVacia: Ya NO se usa, solo limpiamos chat")
        limpiarChat()
    }

    /**
     * Crear una conversación en backend incluyendo el primer mensaje del usuario.
     * ⚠️ ACTUALIZACIÓN: Ya NO se usa, el orquestador crea la conversación automáticamente
     * Devuelve true si se creó y se asignó conversacionId.
     */
    private suspend fun crearConversacionConPrimerMensaje(userId: String, primerMensaje: String): Boolean {
        // ❌ YA NO CREAMOS conversación manualmente
        // El orquestador la creará automáticamente cuando enviemos el primer mensaje
        Log.d(TAG, "⚠️ crearConversacionConPrimerMensaje: Ya NO se crea conversación manualmente")
        Log.d(TAG, "   El orquestador creará la conversación automáticamente")
        return false  // Indicar que NO se creó aquí
    }

    // --- FIN: funciones de conversación ---

    /**
     * Enviar mensaje al chatbot con IA
     *
     * @param texto               Texto de la consulta
     * @param ocultarSugerencias  Si es true, las FAQs se ocultan (solo para input escrito)
     */
    fun enviarMensaje(texto: String, ocultarSugerencias: Boolean = false) {
        if (texto.isBlank()) return

        val textoTrim = texto.trim()

        // Agregar mensaje del usuario localmente
        mensajes.add(Mensaje(textoTrim, true))

        // Ocultar sugerencias solo cuando viene de input escrito
        if (ocultarSugerencias) {
            mostrarSugerencias.value = false
        }

        // Respuesta predefinida sin llamar al backend
        val matchedEntry = respuestasPredefinidas.entries.firstOrNull { (key, _) ->
            val keyTrim = key.trim()
            keyTrim.equals(textoTrim, ignoreCase = true) ||
                    keyTrim.contains(textoTrim, ignoreCase = true) ||
                    textoTrim.contains(keyTrim, ignoreCase = true)
        }

        if (matchedEntry != null) {
            val (key, baseRespuesta) = matchedEntry

            val respuestaConAccion = when (baseRespuesta.actionRoute) {
                "recursos" -> baseRespuesta.copy(accion = { navegarADocumentos?.invoke() })
                "calendario", "actividades" -> baseRespuesta.copy(accion = { navegarAActividades?.invoke() })
                "perfil" -> baseRespuesta.copy(accion = { navegarAPerfil?.invoke() })
                else -> baseRespuesta
            }

            if (!ocultarSugerencias) {
                val index = sugerencias.indexOfFirst { it.equals(key, ignoreCase = true) }
                if (index >= 0) {
                    sugerencias.removeAt(index)
                }
            }

            isLoading.value = true
            error.value = null
            mensajes.add(Mensaje(TYPING_MESSAGE_TEXT, false))

            viewModelScope.launch {
                delay(1000L)
                if (mensajes.isNotEmpty() && mensajes.last().texto == TYPING_MESSAGE_TEXT) {
                    mensajes.removeAt(mensajes.size - 1)
                }
                mensajes.add(respuestaConAccion)
                isLoading.value = false
            }

            return
        }

        // Verificar si hay usuario logueado para llamadas al backend
        val userId = usuarioId
        if (userId.isNullOrBlank()) {
            mensajes.add(Mensaje("Por favor inicia sesión para usar el chatbot.", false))
            return
        }

        isLoading.value = true
        error.value = null

        // Agregar mensaje de "escribiendo..." del bot
        mensajes.add(Mensaje(TYPING_MESSAGE_TEXT, false))

        viewModelScope.launch {
            val startTime = System.currentTimeMillis()
            try {
                // Si no hay conversacionId, crearla primero (incluyendo el primer mensaje).
                // Usar un Mutex para evitar que múltiples coroutines creen la misma conversación
                // (ej: usuario presiona enviar varias veces o hay llamadas concurrentes).
                if (conversacionId.isNullOrBlank()) {
                    crearConversacionMutex.withLock {
                        // Re-check dentro del lock porque otra coroutine pudo crear la conversacion mientras esperábamos
                        if (conversacionId.isNullOrBlank()) {
                            val created = crearConversacionConPrimerMensaje(userId, textoTrim)
                            if (!created) {
                                Log.w(TAG, "No se creó conversación antes de enviar; usando fallback a orquestador")
                            }
                        } else {
                            Log.d(TAG, "Conversación ya fue creada por otra coroutine; no se crea de nuevo")
                        }
                    }
                }

                // Si existe conversacionId, preferimos enviar mensaje a /api/Conversacion/{id}/mensajes
                if (!conversacionId.isNullOrBlank()) {
                    try {
                        Log.d(TAG, "📤 Enviando mensaje a conversación existente: id=$conversacionId")

                        // ✅ FIX: Pasar el conversacionId al orquestador
                        val result = chatbotRepository.enviarPregunta(
                            usuarioId = userId,
                            pregunta = textoTrim,
                            conversacionId = conversacionId  // ⚡ NUEVO: Ahora pasamos el ID
                        )

                        result
                            .onSuccess { response ->
                                // Actualizar conversacionId si el orquestador devuelve uno diferente
                                response.conversacionId?.let { newId ->
                                    if (newId != conversacionId) {
                                        Log.w(TAG, "⚠️ Orquestador devolvió conversacionId diferente: $newId (esperaba: $conversacionId)")
                                        conversacionId = newId
                                    }
                                }

                                // Garantizar mínimo 1 segundo de "escribiendo..."
                                val elapsed = System.currentTimeMillis() - startTime
                                if (elapsed < 1000L) {
                                    delay(1000L - elapsed)
                                }

                                if (mensajes.isNotEmpty() && mensajes.last().texto == TYPING_MESSAGE_TEXT) {
                                    mensajes.removeAt(mensajes.size - 1)
                                }

                                mensajes.add(Mensaje(response.respuesta, false))
                                Log.d(TAG, "✅ Mensaje enviado y respuesta recibida exitosamente")
                            }
                            .onFailure { e ->
                                Log.e(TAG, "❌ Error enviando mensaje al orquestador", e)

                                val elapsed = System.currentTimeMillis() - startTime
                                if (elapsed < 1000L) {
                                    delay(1000L - elapsed)
                                }

                                if (mensajes.isNotEmpty() && mensajes.last().texto == TYPING_MESSAGE_TEXT) {
                                    mensajes.removeAt(mensajes.size - 1)
                                }

                                error.value = e.message
                                val errorMsg = when {
                                    e.message?.contains("timeout", ignoreCase = true) == true ->
                                        "La respuesta tardó demasiado. El servidor está muy ocupado. Por favor, intenta de nuevo."
                                    e.message?.contains("401") == true ->
                                        "Error de autenticación. Por favor, inicia sesión nuevamente."
                                    e.message?.contains("404") == true ->
                                        "Servicio no disponible. Verifica la conexión del servidor."
                                    else -> "Lo siento, ocurrió un error: ${e.message}"
                                }
                                mensajes.add(Mensaje(errorMsg, false))
                            }

                    } catch (e: Exception) {
                        Log.e(TAG, "❌ enviarMensaje excepción al procesar respuesta", e)

                        val elapsed = System.currentTimeMillis() - startTime
                        if (elapsed < 1000L) {
                            delay(1000L - elapsed)
                        }

                        if (mensajes.isNotEmpty() && mensajes.last().texto == TYPING_MESSAGE_TEXT) {
                            mensajes.removeAt(mensajes.size - 1)
                        }

                        error.value = e.message
                        mensajes.add(Mensaje("Error de conexión. Por favor intenta de nuevo.", false))
                    }

                } else {
                    // No se creó conversacion: fallback a orquestador (mismo flujo anterior)
                    // ⚠️ Sin conversacionId, el orquestador creará una nueva
                    val result = chatbotRepository.enviarPregunta(
                        usuarioId = userId,
                        pregunta = textoTrim,
                        conversacionId = null  // Primer mensaje, sin ID aún
                    )

                    // Garantizar mínimo 1 segundo de "escribiendo..."
                    val elapsed = System.currentTimeMillis() - startTime
                    if (elapsed < 1000L) {
                        delay(1000L - elapsed)
                    }

                    result
                        .onSuccess { response ->
                            if (mensajes.isNotEmpty() && mensajes.last().texto == TYPING_MESSAGE_TEXT) {
                                mensajes.removeAt(mensajes.size - 1)
                            }

                            // Guardar el ID de la conversación devuelto por el orquestador si existe
                            response.conversacionId?.let { conversacionId = it }
                            mensajes.add(Mensaje(response.respuesta, false))
                        }
                        .onFailure { e ->
                            if (mensajes.isNotEmpty() && mensajes.last().texto == TYPING_MESSAGE_TEXT) {
                                mensajes.removeAt(mensajes.size - 1)
                            }

                            error.value = e.message
                            val errorMsg = when {
                                e.message?.contains("timeout", ignoreCase = true) == true ->
                                    "La respuesta tardó demasiado. El servidor está muy ocupado. Por favor, intenta de nuevo."
                                e.message?.contains("401") == true ->
                                    "Error de autenticación. Por favor, inicia sesión nuevamente."
                                e.message?.contains("404") == true ->
                                    "Servicio no disponible. Verifica la conexión del servidor."
                                else -> "Lo siento, ocurrió un error: ${e.message}"
                            }
                            mensajes.add(Mensaje(errorMsg, false))
                        }
                }

            } catch (e: Exception) {
                Log.e(TAG, "enviarMensaje excepción", e)
                val elapsed = System.currentTimeMillis() - startTime
                if (elapsed < 1000L) {
                    delay(1000L - elapsed)
                }

                if (mensajes.isNotEmpty() && mensajes.last().texto == TYPING_MESSAGE_TEXT) {
                    mensajes.removeAt(mensajes.size - 1)
                }

                error.value = e.message
                mensajes.add(Mensaje("Error de conexión. Por favor intenta de nuevo.", false))
            } finally {
                isLoading.value = false
            }
        }
    }

    /**
     * Registrar satisfacción con la respuesta (1-5)
     */
    @Suppress("unused")
    fun registrarSatisfaccion(satisfaccion: Int) {
        val convId = conversacionId ?: return

        viewModelScope.launch {
            try {
                chatbotRepository.registrarSatisfaccion(convId, satisfaccion)
            } catch (e: Exception) {
                Log.e(TAG, "registrarSatisfaccion excepción", e)
                // Error silencioso
            }
        }
    }

    /**
     * Limpiar el chat e iniciar nueva conversación
     */
    fun limpiarChat() {
        mensajes.clear()
        conversacionId = null
        // Limpiar estado de favorito
        conversacionFavorito = null
        isLoading.value = false
        error.value = null
        mostrarSugerencias.value = true
        mostrarDialogoNuevoChat.value = false // Asegurarse que el diálogo esté cerrado

        // Restaurar todas las sugerencias iniciales si se han ido removiendo
        sugerencias.clear()
        sugerencias.addAll(
            listOf(
                "¿Qué es el onboarding?",
                "¿Dónde veo mis documentos?",
                "¿Qué actividades debo completar?",
                "¿Cómo contacto a mi supervisor?",
                "¿Qué puedo hacer en esta aplicación?"
            )
        )

        mensajes.add(Mensaje("¡Nuevo chat iniciado! ¿En qué puedo ayudarte ahora?", false))
    }

    /**
     * Verificar estado del servicio de chatbot
     */
    @Suppress("unused")
    fun verificarEstadoChatbot() {
        viewModelScope.launch {
            try {
                chatbotRepository.healthCheck()
            } catch (e: Exception) {
                Log.e(TAG, "verificarEstadoChatbot excepción", e)
                // Error silencioso
            }
        }
    }

    /**
     * Cargar una conversacion completa (historial) por su ID desde la API y poblar los mensajes
     */
    @Suppress("unused")
    fun cargarConversacionPorId(id: String) {
        Log.d(TAG, "🔵 cargarConversacionPorId INICIADO - id=$id")

        viewModelScope.launch {
            isLoading.value = true
            try {
                Log.d(TAG, "📡 Llamando a API: getConversacionById($id)")
                val response = RetrofitInstance.conversacionApi.getConversacionById(id)

                Log.d(TAG, "📨 Respuesta recibida: isSuccessful=${response.isSuccessful}, code=${response.code()}")

                if (response.isSuccessful) {
                    val conversacionCompleta = response.body()
                    Log.d(TAG, "📦 Body recibido: ${conversacionCompleta != null}")

                    if (conversacionCompleta != null) {
                        // ✅ IMPORTANTE: Limpiar COMPLETAMENTE mensajes antes de cargar desde historial
                        val mensajesAnteriores = mensajes.size
                        mensajes.clear()
                        Log.d(TAG, "🗑️ Mensajes limpiados: $mensajesAnteriores -> 0")

                        // Ocultar sugerencias al cargar conversación del historial
                        mostrarSugerencias.value = false

                        // Manejar caso nullable y mapear MensajeChat -> Mensaje (estructura UI)
                        val listaMensajes = conversacionCompleta.mensajes ?: emptyList()

                        Log.d(TAG, "📋 Total mensajes en conversación: ${listaMensajes.size}")

                        if (listaMensajes.isEmpty()) {
                            Log.w(TAG, "⚠️ La conversación NO tiene mensajes")
                            mensajes.add(Mensaje("Esta conversación no tiene mensajes aún.", false))
                        } else {
                            // ✅ FIX: Agregar mensajes uno por uno para evitar duplicados
                            listaMensajes.forEachIndexed { index, m ->
                                val esUsuario = m.tipo.equals("usuario", ignoreCase = true)
                                val nuevoMensaje = Mensaje(m.contenido, esUsuario)
                                mensajes.add(nuevoMensaje)

                                val preview = if (m.contenido.length > 50) {
                                    m.contenido.take(50) + "..."
                                } else {
                                    m.contenido
                                }
                                Log.d(TAG, "  ✅ [$index] Mensaje agregado: tipo=${m.tipo}, contenido=$preview")
                            }
                        }

                        conversacionId = id
                        // set favorito desde la conversacion completa
                        conversacionFavorito = conversacionCompleta.favorito ?: false

                        Log.d(TAG, "✅ cargarConversacionPorId COMPLETADO")
                        Log.d(TAG, "   - conversacionId asignado: $id")
                        Log.d(TAG, "   - Total mensajes en UI: ${mensajes.size}")
                        Log.d(TAG, "   - Favorito: $conversacionFavorito")
                    } else {
                        val errorMsg = "Conversación vacía (body null)"
                        error.value = errorMsg
                        Log.e(TAG, "❌ $errorMsg")
                        mensajes.clear()
                        mensajes.add(Mensaje("Error: No se pudo cargar la conversación.", false))
                    }
                } else {
                    val errorBody = response.errorBody()?.string()
                    val errorMsg = "Error al cargar conversación: ${response.code()}"
                    error.value = errorMsg
                    Log.e(TAG, "❌ $errorMsg")
                    Log.e(TAG, "   ErrorBody: $errorBody")

                    mensajes.clear()
                    mensajes.add(Mensaje("Error al cargar la conversación (${response.code()}). Por favor, intenta de nuevo.", false))
                }
            } catch (e: Exception) {
                Log.e(TAG, "❌ cargarConversacionPorId EXCEPCIÓN", e)
                Log.e(TAG, "   Tipo: ${e.javaClass.simpleName}")
                Log.e(TAG, "   Mensaje: ${e.message}")
                Log.e(TAG, "   StackTrace: ${e.stackTraceToString()}")

                error.value = e.message
                mensajes.clear()
                mensajes.add(Mensaje("Error de conexión al cargar conversación: ${e.message}", false))
            } finally {
                isLoading.value = false
                Log.d(TAG, "🔵 cargarConversacionPorId FINALIZADO")
            }
        }
    }

    /**
     * Cambiar estado de favorito de la conversación actual
     * Usa el endpoint unificado POST /api/Usuario/{usuarioId}/favoritos con tipoRecurso="chat"
     */
    fun toggleFavoritoConversacion(conversacionId: String, estadoActual: Boolean) {
        if (conversacionId.isBlank()) {
            Log.e(TAG, "toggleFavoritoConversacion: conversacionId vacío")
            return
        }

        val userId = usuarioId
        if (userId.isNullOrBlank()) {
            Log.e(TAG, "toggleFavoritoConversacion: usuarioId es nulo")
            return
        }

        val nuevoEstado = !estadoActual

        // Optimistic UI update
        val before = conversacionFavorito
        conversacionFavorito = nuevoEstado

        viewModelScope.launch {
            try {
                // Usar el endpoint unificado de Favoritos (igual que documentos/actividades)
                val requestBody = com.example.chatbot_diseo.network.dto.request.FavoritoRequest(
                    tipoRecurso = "chat",
                    recursoId = conversacionId
                )

                Log.d(TAG, "Enviando toggle favorito: tipoRecurso=chat, recursoId=$conversacionId, usuarioId=$userId")

                val resp = RetrofitInstance.favoritosApi.toggleFavorito(userId, requestBody)

                if (resp.isSuccessful) {
                    val body = resp.body()
                    val esFavoritoBackend = body?.esFavorito ?: nuevoEstado

                    Log.d(TAG, "toggleFavoritoConversacion: SUCCESS, esFavorito=$esFavoritoBackend")

                    // Actualizar estado local con el valor real del backend
                    conversacionFavorito = esFavoritoBackend

                    // Notificar a FavoritosViewModel para que recargue la lista unificada
                    com.example.chatbot_diseo.presentation.favoritos.FavoritosBus.emitFavoritosChanged()
                } else {
                    val errorBody = resp.errorBody()?.string()
                    Log.e(TAG, "toggleFavoritoConversacion: ERROR code=${resp.code()}, body=$errorBody")
                    // rollback
                    conversacionFavorito = before
                }
            } catch (e: Exception) {
                Log.e(TAG, "toggleFavoritoConversacion excepción: ${e.message}", e)
                conversacionFavorito = before
            }
        }
    }
}

