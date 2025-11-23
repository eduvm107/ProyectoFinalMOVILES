package com.example.chatbot_diseo.network.client

/**
 * Configuración de endpoints del backend
 *
 * ✅ CONFIGURADO PARA TU BACKEND ASP.NET CORE
 *
 * Modelos del backend:
 * - MensajeAutomatico
 * - Actividad
 * - Documento
 */
object ApiConfig {

    // ========== BASE URL ==========
    const val BASE_URL = "http://10.185.24.6:5288/api/"

    // ========== ENDPOINTS EN ESPAÑOL ==========
    const val MENSAJES_ENDPOINT = "mensajesautomaticos"
    const val ACTIVIDADES_ENDPOINT = "actividades"
    const val DOCUMENTOS_ENDPOINT = "documentos"
    const val METRICAS_ENDPOINT = "metricas"

    // ========== URLs COMPLETAS ==========
    fun getFullUrl(endpoint: String) = BASE_URL + endpoint

    fun printAllUrls() {
        println("🌐 URLs del Backend ASP.NET Core:")
        println("Mensajes:    ${getFullUrl(MENSAJES_ENDPOINT)}")
        println("Actividades: ${getFullUrl(ACTIVIDADES_ENDPOINT)}")
        println("Documentos:  ${getFullUrl(DOCUMENTOS_ENDPOINT)}")
        println("Métricas:    ${getFullUrl(METRICAS_ENDPOINT)}")
    }

    /**
     * URLs completas para probar en el navegador:
     * - http://10.185.24.6:5288/api/mensajesautomaticos
     * - http://10.185.24.6:5288/api/actividades
     * - http://10.185.24.6:5288/api/documentos
     */
}
