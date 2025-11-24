package com.example.chatbot_diseo.data.api

/**
 * Holder para datos de autenticación del usuario.
 * Almacena token e ID del usuario de forma volátil en memoria.
 * Para persistencia a largo plazo, usar DataStore o EncryptedSharedPreferences.
 */
object TokenHolder {
    @Volatile
    var token: String? = null

    @Volatile
    var usuarioId: String? = null

    /**
     * Limpiar los datos de autenticación (logout)
     */
    fun clear() {
        token = null
        usuarioId = null
    }
}
