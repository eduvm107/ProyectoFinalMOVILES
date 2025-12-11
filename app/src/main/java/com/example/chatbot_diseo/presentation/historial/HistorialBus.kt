package com.example.chatbot_diseo.presentation.historial

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow

object HistorialBus {
    private val _events = MutableSharedFlow<Unit>(extraBufferCapacity = 1)
    val events: SharedFlow<Unit> = _events

    fun emitHistorialChanged() {
        _events.tryEmit(Unit)
    }
}

