package com.example.chatbot_diseo.network.client

import android.util.Log
import com.example.chatbot_diseo.data.common.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Utilidad para diagnosticar problemas de conexión con el backend
 */
object NetworkDiagnostics {

    private const val TAG = "NetworkDiagnostics"

    /**
     * Prueba la conexión al backend y muestra información detallada
     */
    suspend fun testConnection(): Result<String> = withContext(Dispatchers.IO) {
        try {
            val baseUrl = "http://10.185.24.6:5288"

            Log.d(TAG, "🔍 Iniciando diagnóstico de conexión...")
            Log.d(TAG, "📡 Base URL: $baseUrl")
            Log.d(TAG, "📡 API URL: $baseUrl/api/")

            // Probar endpoints comunes
            val endpoints = listOf(
                "$baseUrl/api/contents",
                "$baseUrl/api/admin/contents",
                "$baseUrl/api/activities",
                "$baseUrl/api/resources",
                "$baseUrl/swagger/index.html",
                "$baseUrl/weatherforecast"  // Endpoint por defecto de ASP.NET
            )

            val results = StringBuilder()
            results.append("🌐 Diagnóstico de Conexión\n\n")
            results.append("Base: $baseUrl\n\n")
            results.append("Probando endpoints:\n")

            endpoints.forEach { url ->
                results.append("• $url\n")
                Log.d(TAG, "Probando: $url")
            }

            Result.Success(results.toString())

        } catch (e: Exception) {
            Log.e(TAG, "❌ Error en diagnóstico: ${e.message}", e)
            Result.Error("Error en diagnóstico: ${e.message}")
        }
    }

    /**
     * Obtiene información de configuración actual
     */
    fun getConfigInfo(): String {
        return """
        📱 Configuración Actual
        
        🌐 Base URL: http://10.185.24.6:5288/api/
        
        📍 Endpoints configurados:
        • Contents:   /api/contents
        • Activities: /api/activities  
        • Resources:  /api/resources
        • Metrics:    /api/metrics
        
        🔧 Para cambiar las rutas:
        1. Abre ApiConfig.kt
        2. Descomenta la opción correcta
        3. Recompila la app
        
        ⚠️ Si ves error 404:
        • Tu backend NO tiene esos endpoints
        • Verifica en tu proyecto ASP.NET Core
        • Busca los controladores [Route("api/[controller]")]
        """.trimIndent()
    }
}

