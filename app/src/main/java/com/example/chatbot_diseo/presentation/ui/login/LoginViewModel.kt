package com.example.chatbot_diseo.presentation.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import android.util.Log
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
        Log.d("LoginViewModel", "Iniciando login para: $email")

        viewModelScope.launch {
            try {
                val resp = repository.login(email, password)
                Log.d("LoginViewModel", "Respuesta login code=${resp.code()}")
                val body = resp.body()

                if (resp.isSuccessful) {
                    if (body != null) {
                        Log.d("LoginViewModel", "Login success token=${body.token}, usuario=${body.usuario?.email}")
                        body.token?.let { TokenHolder.token = it }
                        _state.value = Result.success(body)
                    } else {
                        Log.d("LoginViewModel", "Login success sin body")
                        TokenHolder.token = null
                        _state.value = Result.success(LoginResponse(message = "OK", token = null, usuario = null))
                    }
                } else {
                    val err = try { resp.errorBody()?.string() } catch (_: Exception) { null }
                    Log.d("LoginViewModel", "Login error ${resp.code()} - ${err ?: resp.message()}")
                    _state.value = Result.failure(Exception("Error ${resp.code()} - ${err ?: resp.message()}"))
                }
            } catch (e: Exception) {
                Log.e("LoginViewModel", "Exception en login", e)
                _state.value = Result.failure(e)
            }
        }
    }
}
