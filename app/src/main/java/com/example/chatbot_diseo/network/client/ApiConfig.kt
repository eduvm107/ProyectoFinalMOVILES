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
 *
**/
object ApiConfig {

    // ========== BASE URL ==========
    const val BASE_URL = com.example.chatbot_diseo.config.ApiEnvironment.BASE_URL

    // ========== ENDPOINTS EN ESPAÑOL ==========
    const val MENSAJES_ENDPOINT = "mensajesautomaticos"
    const val ACTIVIDADES_ENDPOINT = "actividades"
    const val DOCUMENTOS_ENDPOINT = "documentos"


    // ========== URLs COMPLETAS ==========
    fun getFullUrl(endpoint: String) = BASE_URL + endpoint

    fun printAllUrls() {
        println("🌐 URLs del Backend ASP.NET Core:")
        println("Mensajes:    ${getFullUrl(MENSAJES_ENDPOINT)}")
        println("Actividades: ${getFullUrl(ACTIVIDADES_ENDPOINT)}")
        println("Documentos:  ${getFullUrl(DOCUMENTOS_ENDPOINT)}")

    }

/**
     * URLs completas para probar en el navegador usando la BASE_URL global:
     * - ${BASE_URL}mensajesautomaticos
     * - ${BASE_URL}actividades
     * - ${BASE_URL}documentos
     **/
}
