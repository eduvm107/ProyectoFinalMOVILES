package com.example.chatbot_diseo.presentation.userperfil

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chatbot_diseo.data.api.ApiService
import com.example.chatbot_diseo.data.models.ChangePasswordRequest
import com.example.chatbot_diseo.data.models.Usuario
import com.example.chatbot_diseo.data.remote.apiChatBot.RetrofitInstance
import com.example.chatbot_diseo.data.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed class ChangePasswordState {
    object Idle : ChangePasswordState()
    object Loading : ChangePasswordState()
    data class Success(val message: String) : ChangePasswordState()
    data class Error(val message: String) : ChangePasswordState()
}

class PerfilViewModel : ViewModel() {

    private val _changePasswordState = MutableStateFlow<ChangePasswordState>(ChangePasswordState.Idle)
    val changePasswordState: StateFlow<ChangePasswordState> = _changePasswordState.asStateFlow()

    // Observar el usuario directamente desde AuthRepository
    val currentUser: StateFlow<Usuario?> = AuthRepository.currentUser

    // Usar la instancia centralizada con configuración SSL correcta
    private val apiService: ApiService = RetrofitInstance.authApi

    fun changePassword(currentPassword: String, newPassword: String) {
        val userEmail = currentUser.value?.email

        if (userEmail.isNullOrBlank()) {
            _changePasswordState.value = ChangePasswordState.Error(
                "No se pudo obtener el email del usuario. Por favor, vuelve a iniciar sesión."
            )
            return
        }

        viewModelScope.launch {
            try {
                _changePasswordState.value = ChangePasswordState.Loading

                val request = ChangePasswordRequest(
                    email = userEmail,
                    currentPassword = currentPassword,
                    newPassword = newPassword
                )

                val response = apiService.changePassword(request)

                if (response.isSuccessful) {
                    val message = response.body()?.message ?: "Contraseña cambiada exitosamente"
                    _changePasswordState.value = ChangePasswordState.Success(message)
                } else {
                    val errorMessage = when (response.code()) {
                        400 -> "La nueva contraseña debe ser diferente a la actual"
                        401 -> "La contraseña actual es incorrecta"
                        else -> "Error al cambiar la contraseña. Intenta nuevamente"
                    }
                    _changePasswordState.value = ChangePasswordState.Error(errorMessage)
                }
            } catch (e: Exception) {
                _changePasswordState.value = ChangePasswordState.Error(
                    "Error de conexión: ${e.message ?: "Verifica tu conexión a internet"}"
                )
            }
        }
    }

    fun resetState() {
        _changePasswordState.value = ChangePasswordState.Idle
    }
}
