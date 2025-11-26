package com.example.chatbot_diseo.presentation.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chatbot_diseo.data.api.TokenHolder
import com.example.chatbot_diseo.data.models.LoginResponse
import com.example.chatbot_diseo.data.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class LoginViewModel : ViewModel() {
    // Usa RetrofitInstance unificado por defecto
    private val repository = AuthRepository()

    private val _state = MutableStateFlow<Result<LoginResponse>?>(null)
    val state: StateFlow<Result<LoginResponse>?> = _state

    // Limpiar el estado actual
    fun clearState() {
        _state.value = null
    }

    fun login(email: String, password: String) {
        // resetear estado antes de iniciar la petición para evitar resultados antiguos
        _state.value = null

        // Validar entrada
        if (email.isBlank() || password.isBlank()) {
            _state.value = Result.failure(Exception("Email y contraseña son requeridos"))
            return
        }

        viewModelScope.launch {
            try {
                val resp = repository.login(email, password)
                val body = resp.body()

                if (resp.isSuccessful) {
                    if (body != null) {
                        // Guardar token e ID del usuario de forma rápida
                        body.token?.let { token ->
                            TokenHolder.token = token
                        }
                        body.usuario?.id?.let { id ->
                            TokenHolder.usuarioId = id
                        }
                        _state.value = Result.success(body)
                    } else {
                        TokenHolder.token = null
                        TokenHolder.usuarioId = null
                        _state.value = Result.success(LoginResponse(message = "OK", token = null, usuario = null))
                    }
                } else {
                    // Limpiar tokens en caso de error
                    TokenHolder.token = null
                    TokenHolder.usuarioId = null

                    val errorMessage = when (resp.code()) {
                        401 -> "Credenciales incorrectas. Verifica email y contraseña."
                        404 -> "El servidor no está disponible. Verifica la conexión."
                        500 -> "Error del servidor. Intenta más tarde."
                        else -> "Error ${resp.code()}: ${resp.message()}"
                    }
                    _state.value = Result.failure(Exception(errorMessage))
                }
            } catch (e: Exception) {
                TokenHolder.token = null
                TokenHolder.usuarioId = null

                val errorMessage = when {
                    e.message?.contains("timeout", ignoreCase = true) == true ->
                        "Tiempo de espera agotado. Verifica la conexión del servidor."
                    e.message?.contains("UnknownHostException") == true ->
                        "No se puede conectar al servidor. Verifica la IP: 10.185.24.6:5288"
                    else -> "Error de conexión: ${e.message ?: "Desconocido"}"
                }
                _state.value = Result.failure(Exception(errorMessage))
            }
        }
    }
}
