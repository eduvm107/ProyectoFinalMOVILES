package com.example.chatbot_diseo.presentation.historial

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.chatbot_diseo.data.repository.HistorialRepository

class HistorialViewModelFactory(
    private val repository: HistorialRepository,
    private val usuarioId: String
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HistorialViewModel::class.java)) {
            return HistorialViewModel(repository, usuarioId) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

