package com.example.chatbot_diseo.data.repository

import com.example.chatbot_diseo.data.model.UsuarioCompleto
import com.example.chatbot_diseo.data.model.UsuarioRequest
import com.example.chatbot_diseo.data.remote.apiChatBot.RetrofitInstance

/**
 * Repositorio para operaciones de Usuario
 */
class UsuarioRepository {

    private val api = RetrofitInstance.usuarioApi

    /**
     * Obtener todos los usuarios
     */
    suspend fun getAllUsuarios(): Result<List<UsuarioCompleto>> {
        return try {
            val response = api.getAllUsuarios()
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Error: ${response.code()} - ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Obtener usuario por ID
     */
    suspend fun getUsuarioById(id: String): Result<UsuarioCompleto> {
        return try {
            val response = api.getUsuarioById(id)
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Error: ${response.code()} - ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Crear un nuevo usuario
     */
    suspend fun createUsuario(usuario: UsuarioRequest): Result<UsuarioCompleto> {
        return try {
            val response = api.createUsuario(usuario)
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Error: ${response.code()} - ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Actualizar un usuario existente
     */
    suspend fun updateUsuario(id: String, usuario: UsuarioRequest): Result<Unit> {
        return try {
            val response = api.updateUsuario(id, usuario)
            if (response.isSuccessful) {
                Result.success(Unit)
            } else {
                Result.failure(Exception("Error: ${response.code()} - ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Eliminar un usuario
     */
    suspend fun deleteUsuario(id: String): Result<Unit> {
        return try {
            val response = api.deleteUsuario(id)
            if (response.isSuccessful) {
                Result.success(Unit)
            } else {
                Result.failure(Exception("Error: ${response.code()} - ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Buscar usuario por email
     */
    suspend fun getUsuarioByEmail(email: String): Result<UsuarioCompleto> {
        return try {
            val response = api.getUsuarioByEmail(email)
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Error: ${response.code()} - ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Buscar usuario por DNI
     */
    suspend fun getUsuarioByDni(dni: String): Result<UsuarioCompleto> {
        return try {
            val response = api.getUsuarioByDni(dni)
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Error: ${response.code()} - ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Obtener usuarios por estado de onboarding
     */
    suspend fun getUsuariosByOnboarding(estado: String): Result<List<UsuarioCompleto>> {
        return try {
            val response = api.getUsuariosByOnboarding(estado)
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Error: ${response.code()} - ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Obtener usuarios activos
     */
    suspend fun getUsuariosActivos(): Result<List<UsuarioCompleto>> {
        return try {
            val response = api.getUsuariosActivos()
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Error: ${response.code()} - ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Obtener usuarios por departamento
     */
    suspend fun getUsuariosByDepartamento(departamento: String): Result<List<UsuarioCompleto>> {
        return try {
            val response = api.getUsuariosByDepartamento(departamento)
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Error: ${response.code()} - ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
