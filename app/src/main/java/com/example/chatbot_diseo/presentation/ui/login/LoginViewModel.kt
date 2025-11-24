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

        viewModelScope.launch {
            try {
                val resp = repository.login(email, password)
                val body = resp.body()

                if (resp.isSuccessful) {
                    if (body != null) {
                        // Guardar token e ID del usuario de forma rápida
                        body.token?.let { TokenHolder.token = it }
                        body.usuario?.id?.let { TokenHolder.usuarioId = it }
                        _state.value = Result.success(body)
                    } else {
                        TokenHolder.token = null
                        TokenHolder.usuarioId = null
                        _state.value = Result.success(LoginResponse(message = "OK", token = null, usuario = null))
                    }
                } else {
                    val err = try { resp.errorBody()?.string() } catch (_: Exception) { null }
                    _state.value = Result.failure(Exception("Error ${resp.code()}"))
                }
            } catch (e: Exception) {
                _state.value = Result.failure(e)
            }
        }
    }
}
