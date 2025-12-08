package com.example.chatbot_diseo.data.admin.datasource

import com.example.chatbot_diseo.config.ApiEnvironment
import com.example.chatbot_diseo.data.common.Result
import com.example.chatbot_diseo.network.api.AdminApiService
import com.example.chatbot_diseo.network.client.RetrofitClient
import com.example.chatbot_diseo.network.dto.request.ActivityRequest
import com.example.chatbot_diseo.network.dto.request.ContentRequest
import com.example.chatbot_diseo.network.dto.request.ResourceRequest
import com.example.chatbot_diseo.network.dto.response.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response

/**
 * RemoteDataSource para el panel de administración
 * Maneja todas las peticiones HTTP al backend ASP.NET Core
 */
class AdminRemoteDataSource {

    private val apiService: AdminApiService = RetrofitClient.createService(AdminApiService::class.java)

    // ============== CONTENIDOS ==============

    suspend fun getContents(): Result<List<ContentResponse>> = withContext(Dispatchers.IO) {
        try {
            val response = apiService.getContents()
            handleResponse(response) { it }
        } catch (e: Exception) {
            Result.Error(handleException(e))
        }
    }

    suspend fun getContentById(id: String): Result<ContentResponse> = withContext(Dispatchers.IO) {
        try {
            val response = apiService.getContentById(id)
            handleResponse(response) { it }
        } catch (e: Exception) {
            Result.Error(handleException(e))
        }
    }

    suspend fun createContent(
        titulo: String,
        contenido: String,
        tipo: String,
        diaGatillo: Int,
        prioridad: String,
        canal: List<String>,
        activo: Boolean,
        segmento: String,
        horaEnvio: String
    ): Result<ContentResponse> = withContext(Dispatchers.IO) {
        try {
            val request = ContentRequest(
                titulo = titulo,
                contenido = contenido,
                tipo = tipo,
                diaGatillo = diaGatillo,
                prioridad = prioridad,
                canal = canal,
                activo = activo,
                segmento = segmento,
                horaEnvio = horaEnvio,
                creadoPor = "admin"
            )
            val response = apiService.createContent(request)
            handleResponse(response) { it }
        } catch (e: Exception) {
            Result.Error(handleException(e))
        }
    }

    suspend fun updateContent(
        id: String,
        titulo: String,
        contenido: String,
        tipo: String,
        diaGatillo: Int,
        prioridad: String,
        canal: List<String>,
        activo: Boolean,
        segmento: String,
        horaEnvio: String
    ): Result<ContentResponse> = withContext(Dispatchers.IO) {
        try {
            val request = ContentRequest(
                titulo = titulo,
                contenido = contenido,
                tipo = tipo,
                diaGatillo = diaGatillo,
                prioridad = prioridad,
                canal = canal,
                activo = activo,
                segmento = segmento,
                horaEnvio = horaEnvio,
                creadoPor = "admin"
            )
            val response = apiService.updateContent(id, request)

            // Si es 204 No Content, hacer GET para obtener el objeto actualizado
            if (response.code() == 204) {
                return@withContext getContentById(id)
            }

            handleResponse(response) { it }
        } catch (e: Exception) {
            Result.Error(handleException(e))
        }
    }

    suspend fun deleteContent(id: String): Result<Boolean> = withContext(Dispatchers.IO) {
        try {
            val response = apiService.deleteContent(id)
            if (response.isSuccessful) {
                Result.Success(true)
            } else {
                Result.Error("Error al eliminar: código ${response.code()}", response.code())
            }
        } catch (e: Exception) {
            Result.Error(handleException(e))
        }
    }

    // ============== ACTIVIDADES ==============

    suspend fun getActivities(): Result<List<ActivityResponse>> = withContext(Dispatchers.IO) {
        try {
            val response = apiService.getActivities()
            handleResponse(response) { it }
        } catch (e: Exception) {
            Result.Error(handleException(e))
        }
    }

    suspend fun getActivityById(id: String): Result<ActivityResponse> = withContext(Dispatchers.IO) {
        try {
            val response = apiService.getActivityById(id)
            handleResponse(response) { it }
        } catch (e: Exception) {
            Result.Error(handleException(e))
        }
    }

    suspend fun createActivity(
        title: String,
        date: String,
        modality: String
    ): Result<ActivityResponse> = withContext(Dispatchers.IO) {
        try {
            // Parsear date que viene como "Día X - HH:mm" o simplemente el día
            val dia = date.substringAfter("Día ").substringBefore(" ").toIntOrNull() ?: 1
            val horaInicio = if (date.contains("-")) date.substringAfter("- ").trim() else "09:00"
            val horaFin = "18:00"

            val request = ActivityRequest(
                titulo = title,
                descripcion = "Actividad de onboarding: $title",
                dia = dia,
                duracionHoras = 8.0,
                horaInicio = horaInicio,
                horaFin = horaFin,
                lugar = "Por definir",
                modalidad = modality,
                tipo = "induccion",
                responsable = "RRHH"
            )
            val response = apiService.createActivity(request)
            handleResponse(response) { it }
        } catch (e: Exception) {
            Result.Error(handleException(e))
        }
    }

    suspend fun updateActivity(
        id: String,
        title: String,
        date: String,
        modality: String
    ): Result<ActivityResponse> = withContext(Dispatchers.IO) {
        try {
            val dia = date.substringAfter("Día ").substringBefore(" ").toIntOrNull() ?: 1
            val horaInicio = if (date.contains("-")) date.substringAfter("- ").trim() else "09:00"
            val horaFin = "18:00"

            val request = ActivityRequest(
                titulo = title,
                descripcion = "Actividad de onboarding: $title",
                dia = dia,
                duracionHoras = 8.0,
                horaInicio = horaInicio,
                horaFin = horaFin,
                lugar = "Por definir",
                modalidad = modality,
                tipo = "induccion",
                responsable = "RRHH"
            )
            val response = apiService.updateActivity(id, request)

            // Si es 204 No Content, hacer GET para obtener el objeto actualizado
            if (response.code() == 204) {
                return@withContext getActivityById(id)
            }

            handleResponse(response) { it }
        } catch (e: Exception) {
            Result.Error(handleException(e))
        }
    }

    suspend fun updateActivityComplete(
        id: String,
        activityRequest: ActivityRequest
    ): Result<ActivityResponse> = withContext(Dispatchers.IO) {
        try {
            val response = apiService.updateActivity(id, activityRequest)

            // Si es 204 No Content, hacer GET para obtener el objeto actualizado
            if (response.code() == 204) {
                return@withContext getActivityById(id)
            }

            handleResponse(response) { it }
        } catch (e: Exception) {
            Result.Error(handleException(e))
        }
    }

    suspend fun deleteActivity(id: String): Result<Boolean> = withContext(Dispatchers.IO) {
        try {
            val response = apiService.deleteActivity(id)
            if (response.isSuccessful) {
                Result.Success(true)
            } else {
                Result.Error("Error al eliminar: código ${response.code()}", response.code())
            }
        } catch (e: Exception) {
            Result.Error(handleException(e))
        }
    }

    // ============== RECURSOS ==============

    suspend fun getResources(): Result<List<ResourceResponse>> = withContext(Dispatchers.IO) {
        try {
            val response = apiService.getResources()
            handleResponse(response) { it }
        } catch (e: Exception) {
            Result.Error(handleException(e))
        }
    }

    suspend fun getResourceById(id: String): Result<ResourceResponse> = withContext(Dispatchers.IO) {
        try {
            val response = apiService.getResourceById(id)
            handleResponse(response) { it }
        } catch (e: Exception) {
            Result.Error(handleException(e))
        }
    }

    suspend fun createResource(
        title: String,
        category: String,
        url: String
    ): Result<ResourceResponse> = withContext(Dispatchers.IO) {
        try {
            val request = ResourceRequest(
                titulo = title,
                descripcion = "Recurso: $title",
                url = url,
                tipo = "PDF",
                categoria = category,
                subcategoria = "",
                tags = emptyList(),
                icono = "📄",
                tamaño = null,
                idioma = "Español",
                version = "1.0",
                publico = "Nuevos empleados",
                obligatorio = false,
                autor = "Administrador",
                valoracion = 0
            )
            val response = apiService.createResource(request)
            handleResponse(response) { it }
        } catch (e: Exception) {
            Result.Error(handleException(e))
        }
    }

    suspend fun updateResource(
        id: String,
        title: String,
        category: String,
        url: String
    ): Result<ResourceResponse> = withContext(Dispatchers.IO) {
        try {
            val request = ResourceRequest(
                titulo = title,
                descripcion = "Recurso: $title",
                url = url,
                tipo = "PDF",
                categoria = category,
                subcategoria = "",
                tags = emptyList(),
                icono = "📄",
                tamaño = null,
                idioma = "Español",
                version = "1.0",
                publico = "Nuevos empleados",
                obligatorio = false,
                autor = "Administrador",
                valoracion = 0
            )
            val response = apiService.updateResource(id, request)

            // Si es 204 No Content, hacer GET para obtener el objeto actualizado
            if (response.code() == 204) {
                return@withContext getResourceById(id)
            }

            handleResponse(response) { it }
        } catch (e: Exception) {
            Result.Error(handleException(e))
        }
    }

    suspend fun deleteResource(id: String): Result<Boolean> = withContext(Dispatchers.IO) {
        try {
            val response = apiService.deleteResource(id)
            if (response.isSuccessful) {
                Result.Success(true)
            } else {
                Result.Error("Error al eliminar: código ${response.code()}", response.code())
            }
        } catch (e: Exception) {
            Result.Error(handleException(e))
        }
    }

    // ============== MÉTRICAS ==============

    suspend fun getMetrics(): Result<MetricsResponse> = withContext(Dispatchers.IO) {
        try {
            val response = apiService.getMetrics()
            handleResponse(response) { it }
        } catch (e: Exception) {
            Result.Error(handleException(e))
        }
    }

    // ============== HELPER METHODS ==============

    /**
     * Procesa respuestas de listas HTTP
     */
    private fun <T, R> handleListResponse(
        response: Response<T>,
        transform: (T) -> R
    ): Result<R> {
        return when {
            response.isSuccessful && response.body() != null -> {
                Result.Success(transform(response.body()!!))
            }
            response.isSuccessful && response.body() == null -> {
                // Si no hay body pero es exitoso, devolver lista vacía o valor por defecto
                Result.Success(transform(response.body()!!))
            }
            response.code() == 400 -> {
                Result.Error("Datos inválidos. Verifica la información enviada.", 400)
            }
            response.code() == 404 -> {
                Result.Error("Recurso no encontrado. Verifica que el endpoint exista en tu backend.", 404)
            }
            response.code() == 500 -> {
                Result.Error("Error en el servidor. Intenta más tarde.", 500)
            }
            else -> {
                Result.Error("Error desconocido: ${response.code()}", response.code())
            }
        }
    }

    /**
     * Procesa respuestas HTTP y maneja códigos de estado
     */
    private fun <T, R> handleResponse(
        response: Response<T>,
        transform: (T) -> R
    ): Result<R> {
        return when {
            response.code() == 204 -> {
                // 204 No Content - actualización exitosa sin cuerpo de respuesta
                Result.Error("UPDATE_SUCCESS_NO_CONTENT", 204)
            }
            response.isSuccessful && response.body() != null -> {
                Result.Success(transform(response.body()!!))
            }
            response.isSuccessful && response.body() == null -> {
                // Respuesta exitosa pero sin contenido - el servidor respondió OK
                // Esto puede indicar que la colección está vacía
                Result.Error("El servidor respondió OK pero sin datos. Verifica que la colección tenga elementos en MongoDB.", response.code())
            }
            response.code() == 400 -> {
                val errorBody = try {
                    response.errorBody()?.string() ?: "Datos inválidos"
                } catch (e: Exception) {
                    "Datos inválidos: ${e.message}"
                }
                Result.Error("Petición incorrecta: $errorBody", 400)
            }
            response.code() == 401 -> {
                Result.Error("No autorizado. Verifica tus credenciales de administrador.", 401)
            }
            response.code() == 403 -> {
                Result.Error("Acceso denegado. No tienes permisos para esta operación.", 403)
            }
            response.code() == 404 -> {
                Result.Error("Endpoint no encontrado. Verifica la configuración del servidor: http://10.185.24.6:5288", 404)
            }
            response.code() == 500 -> {
                val errorBody = try {
                    response.errorBody()?.string() ?: "Sin detalles del error"
                } catch (e: Exception) {
                    "Error al leer respuesta: ${e.message}"
                }
                Result.Error("Error 500 del servidor: $errorBody\n\nDetalles técnicos: El backend ASP.NET Core tiene un problema interno. Revisa los logs del servidor para más información.", 500)
            }
            response.code() == 502 -> {
                Result.Error("Bad Gateway (502). El servidor proxy no puede conectarse al backend.", 502)
            }
            response.code() == 503 -> {
                Result.Error("Servicio no disponible (503). El servidor está temporalmente sobrecargado o en mantenimiento.", 503)
            }
            response.code() == 504 -> {
                Result.Error("Gateway Timeout (504). El servidor tardó demasiado en responder.", 504)
            }
            else -> {
                val errorBody = try {
                    response.errorBody()?.string() ?: response.message()
                } catch (e: Exception) {
                    response.message()
                }
                Result.Error("Error HTTP ${response.code()}: $errorBody", response.code())
            }
        }
    }

    /**
     * Maneja excepciones de red
     */
    private fun handleException(e: Exception): String {
        return when {
            e is java.net.UnknownHostException -> "Sin conexión a internet o servidor no encontrado"
            e is java.net.SocketTimeoutException -> "Tiempo de espera agotado. El servidor no responde"
            e is java.net.ConnectException -> "No se pudo conectar al servidor en http://10.185.24.6:5288"
            e is retrofit2.HttpException -> "Error HTTP ${e.code()}: ${e.message()}"
            else -> "Error: ${e.message ?: "Error desconocido"}"
        }
    }
}
