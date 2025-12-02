package com.example.chatbot_diseo.presentation.ui.forgotpassword

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chatbot_diseo.data.models.ForgotPasswordResponse
import com.example.chatbot_diseo.data.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ForgotPasswordViewModel : ViewModel() {

    private val _state = MutableStateFlow<Result<ForgotPasswordResponse>?>(null)
    val state: StateFlow<Result<ForgotPasswordResponse>?> = _state

    fun clearState() {
        _state.value = null
    }

    fun requestPasswordReset(email: String) {
        _state.value = null

        if (email.isBlank()) {
            _state.value = Result.failure(Exception("El correo es requerido"))
            return
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _state.value = Result.failure(Exception("Correo electrónico inválido"))
            return
        }

        viewModelScope.launch {
            try {
                val resp = AuthRepository.forgotPassword(email)
                val body = resp.body()

                if (resp.isSuccessful && body != null) {
                    _state.value = Result.success(body)
                } else {
                    val errorMessage = when (resp.code()) {
                        404 -> "El servidor no está disponible"
                        500 -> "Error del servidor. Intenta más tarde"
                        else -> "Error ${resp.code()}: ${resp.message()}"
                    }
                    _state.value = Result.failure(Exception(errorMessage))
                }
            } catch (e: Exception) {
                val errorMessage = when {
                    e.message?.contains("timeout", ignoreCase = true) == true ->
                        "Tiempo de espera agotado"
                    e.message?.contains("UnknownHostException") == true ->
                        "No se puede conectar al servidor"
                    else -> "Error: ${e.message ?: "Desconocido"}"
                }
                _state.value = Result.failure(Exception(errorMessage))
            }
        }
    }
}
