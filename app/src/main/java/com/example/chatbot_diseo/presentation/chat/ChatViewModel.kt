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
import kotlinx.coroutines.launch
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter

class ChatViewModel : ViewModel() {

    companion object {
        private const val TAG = "ChatViewModel"
    }

    private val chatbotRepository = ChatbotRepository()

    val mensajes = mutableStateListOf<Mensaje>()
    val isLoading = mutableStateOf(false)
    val error = mutableStateOf<String?>(null)

    // Controlar visibilidad de FAQs / sugerencias
    val mostrarSugerencias = mutableStateOf(true)

    // ID del usuario actual - se obtiene de TokenHolder después del login
    val usuarioId: String?
        get() = TokenHolder.usuarioId

    // ID de la conversación actual
    var conversacionId: String? = null

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
     * Usado por el botón "Nuevo chat".
     */
    fun crearConversacionVacia() {
        val userId = usuarioId
        if (userId.isNullOrBlank()) {
            mensajes.add(Mensaje("Por favor inicia sesión para usar el chatbot.", false))
            return
        }

        viewModelScope.launch {
            isLoading.value = true
            try {
                val nueva = DataConversacion(
                    id = "",
                    usuarioId = userId,
                    tituloBackend = "",
                    mensajes = emptyList(),
                    fechaInicio = ahoraIso(),
                    activa = true,
                    favorito = false
                )

                val resp = RetrofitInstance.conversacionApi.crearConversacion(nueva)
                if (resp.isSuccessful) {
                    val created = resp.body()
                    created?.id?.let { conversacionId = it }
                    // update favorito state (simplificado)
                    conversacionFavorito = created?.favorito ?: false
                    // Limpiamos el chat para la nueva conversacion localmente
                    mensajes.clear()
                    mensajes.add(Mensaje("¡Nuevo chat iniciado! ¿En qué puedo ayudarte ahora?", false))
                    mostrarSugerencias.value = true
                } else {
                    mensajes.add(Mensaje("No se pudo crear conversación (code=${resp.code()}). Intenta de nuevo.", false))
                }
            } catch (e: Exception) {
                Log.e(TAG, "crearConversacionVacia excepción", e)
                mensajes.add(Mensaje("Error creando conversación. Por favor intenta de nuevo.", false))
            } finally {
                isLoading.value = false
            }
        }
    }

    /**
     * Crear una conversación en backend incluyendo el primer mensaje del usuario.
     * Devuelve true si se creó y se asignó conversacionId.
     */
    private suspend fun crearConversacionConPrimerMensaje(userId: String, primerMensaje: String): Boolean {
        return try {
            val dataMsg = DataMensaje(
                tipo = "usuario",
                contenido = primerMensaje,
                timestamp = ahoraIso()
            )
            val nueva = DataConversacion(
                id = "",
                usuarioId = userId,
                tituloBackend = "",
                mensajes = listOf(dataMsg),
                fechaInicio = ahoraIso(),
                activa = true,
                favorito = false
            )

            val resp = RetrofitInstance.conversacionApi.crearConversacion(nueva)
            if (resp.isSuccessful) {
                val created = resp.body()
                created?.id?.let { conversacionId = it }
                // set favorito
                conversacionFavorito = created?.favorito ?: false
                true
            } else {
                Log.e(TAG, "crearConversacionConPrimerMensaje failed: ${resp.code()} ${resp.message()}")
                false
            }
        } catch (e: Exception) {
            Log.e(TAG, "crearConversacionConPrimerMensaje excepción", e)
            false
        }
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
                // Si no hay conversacionId, crearla primero (incluyendo el primer mensaje)
                if (conversacionId.isNullOrBlank()) {
                    val created = crearConversacionConPrimerMensaje(userId, textoTrim)
                    // Si no se pudo crear, dejar como estaba y proceder a usar el orquestador como fallback
                    if (!created) {
                        Log.w(TAG, "No se creó conversación antes de enviar; usando fallback a orquestador")
                    }
                }

                // Si existe conversacionId, preferimos enviar mensaje a /api/Conversacion/{id}/mensajes
                if (!conversacionId.isNullOrBlank()) {
                    try {
                        val resp = RetrofitInstance.conversacionApi.enviarMensaje(conversacionId!!, MensajeRequest(contenido = textoTrim, tipo = "usuario"))
                        // Intentar obtener respuesta del body si el endpoint la retorna
                        var botRespuesta: String? = null
                        if (resp.isSuccessful) {
                            val body = resp.body()
                            when (body) {
                                is String -> botRespuesta = body
                                is Map<*, *> -> {
                                    botRespuesta = (body["respuesta"] ?: body["respuestaBot"] ?: body["contenido"])?.toString()
                                }
                                else -> {
                                    // no sabemos el formato; lo dejamos nulo para fallback
                                }
                            }
                        }

                        // Si no obtuvimos respuesta útil del endpoint, usar orquestador (fallback)
                        if (botRespuesta == null) {
                            val result = chatbotRepository.enviarPregunta(userId, textoTrim)
                            result
                                .onSuccess { response ->
                                    response.conversacionId?.let { conversacionId = it }
                                    botRespuesta = response.respuesta
                                }
                                .onFailure { e ->
                                    throw e
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

                        botRespuesta?.let { mensajes.add(Mensaje(it, false)) } ?: run {
                            mensajes.add(Mensaje("No hubo respuesta del servidor. Intenta de nuevo.", false))
                        }

                    } catch (e: Exception) {
                        Log.e(TAG, "enviarMensaje vía Conversacion API excepción", e)
                        // Fallback: usar orquestador para obtener respuesta
                        val result = chatbotRepository.enviarPregunta(userId, textoTrim)
                        result
                            .onSuccess { response ->
                                response.conversacionId?.let { conversacionId = it }
                                if (mensajes.isNotEmpty() && mensajes.last().texto == TYPING_MESSAGE_TEXT) {
                                    mensajes.removeAt(mensajes.size - 1)
                                }
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

                } else {
                    // No se creó conversacion: fallback a orquestador (mismo flujo anterior)
                    val result = chatbotRepository.enviarPregunta(userId, textoTrim)

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
        mensajes.add(Mensaje("¡Nuevo chat iniciado! ¿En qué puedo ayudarte ahora?", false))
        mostrarSugerencias.value = true
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
        viewModelScope.launch {
            isLoading.value = true
            try {
                val response = RetrofitInstance.conversacionApi.getConversacionById(id)
                if (response.isSuccessful) {
                    val conversacionCompleta = response.body()
                    if (conversacionCompleta != null) {
                        // Limpiar mensajes actuales y agregar los de la conversacion
                        mensajes.clear()

                        // Manejar caso nullable y mapear MensajeChat -> Mensaje (estructura UI)
                        val listaMensajes = conversacionCompleta.mensajes ?: emptyList()
                        listaMensajes.forEach { m ->
                            val esUsuario = m.tipo.equals("usuario", ignoreCase = true)
                            mensajes.add(Mensaje(m.contenido, esUsuario))
                        }

                        conversacionId = id
                        // set favorito desde la conversacion completa
                        conversacionFavorito = conversacionCompleta.favorito ?: false
                        mostrarSugerencias.value = false
                    } else {
                        error.value = "Conversación vacía"
                    }
                } else {
                    error.value = "Error al cargar conversación: ${response.code()}"
                }
            } catch (e: Exception) {
                Log.e(TAG, "cargarConversacionPorId excepción", e)
                error.value = e.message
            } finally {
                isLoading.value = false
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
