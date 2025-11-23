package com.example.chatbot_diseo.data.repository

import com.example.chatbot_diseo.data.model.Configuracion
import com.example.chatbot_diseo.data.model.ConfiguracionRequest
import com.example.chatbot_diseo.data.remote.apiChatBot.RetrofitInstance

/**
 * Repositorio para operaciones de Configuración
 */
class ConfiguracionRepository {

    private val api = RetrofitInstance.configuracionApi

    /**
     * Obtener todas las configuraciones
     */
    suspend fun getAllConfiguraciones(): Result<List<Configuracion>> {
        return try {
            val response = api.getAllConfiguraciones()
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
     * Obtener configuración por ID
     */
    suspend fun getConfiguracionById(id: String): Result<Configuracion> {
        return try {
            val response = api.getConfiguracionById(id)
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
     * Crear una nueva configuración
     */
    suspend fun createConfiguracion(config: ConfiguracionRequest): Result<Configuracion> {
        return try {
            val response = api.createConfiguracion(config)
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
     * Actualizar una configuración existente
     */
    suspend fun updateConfiguracion(id: String, config: ConfiguracionRequest): Result<Unit> {
        return try {
            val response = api.updateConfiguracion(id, config)
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
     * Eliminar una configuración
     */
    suspend fun deleteConfiguracion(id: String): Result<Unit> {
        return try {
            val response = api.deleteConfiguracion(id)
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
     * Obtener configuraciones por tipo
     */
    suspend fun getConfiguracionesByTipo(tipo: String): Result<List<Configuracion>> {
        return try {
            val response = api.getConfiguracionesByTipo(tipo)
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
     * Obtener configuraciones activas
     */
    suspend fun getConfiguracionesActivas(): Result<List<Configuracion>> {
        return try {
            val response = api.getConfiguracionesActivas()
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
     * Buscar configuración por nombre
     */
    suspend fun getConfiguracionByNombre(nombre: String): Result<Configuracion> {
        return try {
            val response = api.getConfiguracionByNombre(nombre)
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
