package com.example.chatbot_diseo.data.repository

import com.example.chatbot_diseo.data.api.ApiService
import com.example.chatbot_diseo.data.api.TokenHolder
import com.example.chatbot_diseo.data.models.LoginRequest
import com.example.chatbot_diseo.data.models.LoginResponse
import com.example.chatbot_diseo.data.models.RecoverPasswordRequest
import com.example.chatbot_diseo.data.models.RecoverPasswordResponse
import com.example.chatbot_diseo.data.models.Usuario
import com.example.chatbot_diseo.data.remote.UsuarioApiService
import com.example.chatbot_diseo.data.remote.apiChatBot.RetrofitInstance
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import retrofit2.Response

/**
 * Repositorio para operaciones de autenticación
 * Singleton para compartir el estado del usuario en toda la app
 */
object AuthRepository {

    private val api: ApiService = RetrofitInstance.authApi
    private val usuarioApi: UsuarioApiService = RetrofitInstance.usuarioApi

    // Usuario actualmente logueado - Observable
    private val _currentUser = MutableStateFlow<Usuario?>(null)
    val currentUser: StateFlow<Usuario?> = _currentUser.asStateFlow()

    /**
     * Realizar login con email y contraseña
     * Guarda el token en TokenHolder automáticamente si el login es exitoso
     * Obtiene la información completa del usuario después del login
     */
    suspend fun login(email: String, password: String): Response<LoginResponse> {
        val req = LoginRequest(email = email, password = password)
        val response = api.login(req)

        // Si el login es exitoso, guardar el token y obtener información completa del usuario
        if (response.isSuccessful && response.body() != null) {
            response.body()?.token?.let { token ->
                TokenHolder.token = token
            }

            // Primero guardamos el usuario básico del login
            response.body()?.usuario?.let { usuario ->
                _currentUser.value = usuario

                // Luego intentamos obtener información completa del usuario desde el endpoint de usuarios
                usuario.id?.let { userId ->
                    try {
                        val userDetailResponse = usuarioApi.getUsuarioById(userId)
                        if (userDetailResponse.isSuccessful && userDetailResponse.body() != null) {
                            val usuarioCompleto = userDetailResponse.body()!!
                            // Convertir UsuarioCompleto a Usuario con toda la información
                            _currentUser.value = Usuario(
                                id = usuarioCompleto.id,
                                email = usuarioCompleto.email,
                                nombreCompleto = usuarioCompleto.nombreCompleto,
                                nombre = usuarioCompleto.nombre,
                                telefono = usuarioCompleto.telefono,  // Aquí está el teléfono
                                departamento = usuarioCompleto.departamento,
                                puesto = usuarioCompleto.puesto,
                                activo = usuarioCompleto.activo,
                                verificado = usuarioCompleto.verificado,
                                estadoOnboarding = usuarioCompleto.estadoOnboarding,
                                progresoOnboarding = usuarioCompleto.progresoOnboarding?.toInt(),  // Convertir Double a Int
                                rol = usuarioCompleto.rol
                            )
                        }
                    } catch (e: Exception) {
                        // Si falla la obtención de detalles, mantener el usuario básico
                        // El error se ignora para no afectar el flujo de login
                    }
                }
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
        _currentUser.value = null
    }

    /**
     * Verificar si hay un usuario logueado
     */
    fun isLoggedIn(): Boolean {
        return !TokenHolder.token.isNullOrBlank()
    }

    /**
     * Obtener el usuario actual (deprecado - usar currentUser StateFlow)
     */
    @Deprecated("Usa currentUser StateFlow en su lugar")
    fun getCurrentUser(): Usuario? {
        return _currentUser.value
    }

    /**
     * Obtener el ID del usuario actual
     */
    fun getCurrentUserId(): String? {
        return _currentUser.value?.id
    }

    /**
     * Establecer el usuario actual (útil para restaurar sesión)
     */
    fun setCurrentUser(usuario: Usuario?) {
        _currentUser.value = usuario
    }

    /**
     * Solicitar recuperación de contraseña
     * Envía una contraseña temporal al correo del usuario
     */
    suspend fun forgotPassword(email: String): Response<com.example.chatbot_diseo.data.models.ForgotPasswordResponse> {
        val req = com.example.chatbot_diseo.data.models.ForgotPasswordRequest(email = email)
        return api.forgotPassword(req)
    }
}
