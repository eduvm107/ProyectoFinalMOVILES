package com.example.chatbot_diseo.presentation.favoritos

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow

/**
 * Event bus simple para notificar cambios en favoritos entre ViewModels.
 * Uso: FavoritosBus.emitFavoritosChanged() desde quien hace toggle,
 * y FavoritosViewModel escucha FavoritosBus.events para recargar.
 */
object FavoritosBus {
    private val _events = MutableSharedFlow<Unit>(extraBufferCapacity = 1)
    val events: SharedFlow<Unit> = _events

    fun emitFavoritosChanged() {
        // tryEmit para no necesitar contexto de coroutines donde se llame
        _events.tryEmit(Unit)
    }
}

