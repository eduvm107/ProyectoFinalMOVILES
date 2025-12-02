package com.example.chatbot_diseo.presentation.ayuda

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chatbot_diseo.data.model.FAQ
import com.example.chatbot_diseo.data.repository.FAQRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

// Estado UI para AyudaScreen
sealed class AyudaUiState {
    object Loading : AyudaUiState()
    data class Success(val faqs: List<FAQ>) : AyudaUiState()
    data class Error(val message: String) : AyudaUiState()
}

class AyudaViewModel(
    private val repository: FAQRepository = FAQRepository()
) : ViewModel() {

    private val _uiState = MutableStateFlow<AyudaUiState>(AyudaUiState.Loading)
    val uiState: StateFlow<AyudaUiState> = _uiState.asStateFlow()

    private val _expandedFaqId = MutableStateFlow<String?>(null)
    val expandedFaqId: StateFlow<String?> = _expandedFaqId.asStateFlow()

    init {
        loadFAQs()
    }

    fun loadFAQs() {
        viewModelScope.launch {
            _uiState.value = AyudaUiState.Loading
            try {
                val res = repository.getAllFAQs() // Result<List<FAQ>>
                if (res.isSuccess) {
                    val faqs = res.getOrNull() ?: emptyList()
                    _uiState.value = AyudaUiState.Success(faqs)
                } else {
                    val ex = res.exceptionOrNull()
                    _uiState.value = AyudaUiState.Error(ex?.message ?: "Error al cargar FAQs")
                }
            } catch (e: Exception) {
                _uiState.value = AyudaUiState.Error(e.message ?: "Error al cargar FAQs")
            }
        }
    }

    fun toggleFaqExpansion(id: String?) {
        _expandedFaqId.value = if (_expandedFaqId.value == id) null else id
    }
}
