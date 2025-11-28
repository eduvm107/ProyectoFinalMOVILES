package com.example.chatbot_diseo.data.repository

import com.example.chatbot_diseo.data.api.ApiService
import com.example.chatbot_diseo.data.api.TokenHolder
import com.example.chatbot_diseo.data.models.LoginRequest
import com.example.chatbot_diseo.data.models.LoginResponse
import com.example.chatbot_diseo.data.models.RecoverPasswordRequest
import com.example.chatbot_diseo.data.models.RecoverPasswordResponse
import com.example.chatbot_diseo.data.models.Usuario
import com.example.chatbot_diseo.data.remote.apiChatBot.RetrofitInstance
import retrofit2.Response

/**
 * Repositorio para operaciones de autenticación
 */
class AuthRepository(private val api: ApiService = RetrofitInstance.authApi) {

    // Usuario actualmente logueado
    private var currentUser: Usuario? = null

    /**
     * Realizar login con email y contraseña
     * Guarda el token en TokenHolder automáticamente si el login es exitoso
     */
    suspend fun login(email: String, password: String): Response<LoginResponse> {
        val req = LoginRequest(email = email, password = password)
        val response = api.login(req)

        // Si el login es exitoso, guardar el token y usuario
        if (response.isSuccessful && response.body() != null) {
            response.body()?.token?.let { token ->
                TokenHolder.token = token
            }
            response.body()?.usuario?.let { usuario ->
                currentUser = usuario
            }
        }

        return response
    }

    /**
     * Solicitar recuperación de contraseña
     */
    suspend fun recoverPassword(email: String): Response<RecoverPasswordResponse> {
        val request = RecoverPasswordRequest(email = email)
        return api.recoverPassword(request)
    }


    /**
     * Cerrar sesión - limpia el token y usuario actual
     */
    fun logout() {
        TokenHolder.token = null
        currentUser = null
    }

    /**
     * Verificar si hay un usuario logueado
     */
    fun isLoggedIn(): Boolean {
        return !TokenHolder.token.isNullOrBlank()
    }

    /**
     * Obtener el usuario actual
     */
    fun getCurrentUser(): Usuario? {
        return currentUser
    }

    /**
     * Obtener el ID del usuario actual
     */
    fun getCurrentUserId(): String? {
        return currentUser?.id
    }

    /**
     * Establecer el usuario actual (útil para restaurar sesión)
     */
    fun setCurrentUser(usuario: Usuario?) {
        currentUser = usuario
    }
}
