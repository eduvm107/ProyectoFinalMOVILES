package com.example.chatbot_diseo.network.client

import android.util.Log
import com.example.chatbot_diseo.config.ApiEnvironment
import com.example.chatbot_diseo.data.common.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Utilidad para diagnosticar problemas de conexión con el backend.
 * Usa siempre la BASE_URL global definida en ApiEnvironment.
 */
object NetworkDiagnostics {

    private const val TAG = "NetworkDiagnostics"

    /**
     * Prueba la conexión al backend y muestra información básica.
     */
    suspend fun testConnection(): Result<String> = withContext(Dispatchers.IO) {
        try {
            val apiBaseUrl = ApiEnvironment.BASE_URL.trimEnd('/')

            Log.d(TAG, "Iniciando diagnóstico de conexión...")
            Log.d(TAG, "API Base URL: $apiBaseUrl")

            // Endpoints de ejemplo relativos a la BASE_URL global
            val endpoints = listOf(
                "$apiBaseUrl/contents",
                "$apiBaseUrl/admin/contents",
                "$apiBaseUrl/activities",
                "$apiBaseUrl/resources",
                "$apiBaseUrl/metrics"
            )

            val results = StringBuilder()
            results.append("Diagnóstico de Conexión\n\n")
            results.append("Base: $apiBaseUrl\n\n")
            results.append("Probando endpoints:\n")

            endpoints.forEach { url ->
                results.append("- $url\n")
                Log.d(TAG, "Probando: $url")
            }

            Result.Success(results.toString())

        } catch (e: Exception) {
            Log.e(TAG, "Error en diagnóstico: ${e.message}", e)
            Result.Error("Error en diagnóstico: ${e.message}")
        }
    }

    /**
     * Obtiene información de configuración actual.
     */
    fun getConfigInfo(): String {
        return """
        Configuración Actual

        Base URL: ${ApiEnvironment.BASE_URL}

        Endpoints configurados:
        • Contents:   /api/contents
        • Activities: /api/activities  
        • Resources:  /api/resources
        • Metrics:    /api/metrics

        Para cambiar las rutas:
        1. Abre ApiConfig.kt
        2. Ajusta la opción correcta
        3. Recompila la app

        Si ves error 404:
        • Tu backend NO tiene esos endpoints
        • Verifica en tu proyecto ASP.NET Core
        • Busca los controladores [Route("api/[controller]")]
        """.trimIndent()
    }
}
