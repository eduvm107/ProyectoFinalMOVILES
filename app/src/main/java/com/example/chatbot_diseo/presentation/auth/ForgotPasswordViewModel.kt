package com.example.chatbot_diseo.presentation.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chatbot_diseo.data.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ForgotPasswordViewModel : ViewModel() {

    private val _state = MutableStateFlow<Result<String>?>(null)
    val state = _state.asStateFlow()

    fun recoverPassword(email: String) {
        viewModelScope.launch {
            try {
                val response = AuthRepository.recoverPassword(email)
                if (response.isSuccessful && response.body() != null) {
                    _state.value = Result.success(response.body()!!.message)
                } else {
                    val errorBody = response.errorBody()?.string() ?: "Error desconocido"
                    _state.value = Result.failure(Exception(errorBody))
                }
            } catch (e: Exception) {
                _state.value = Result.failure(e)
            }
        }
    }

    fun clearState() {
        _state.value = null
    }
}
