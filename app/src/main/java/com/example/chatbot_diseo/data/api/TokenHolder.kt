package com.example.chatbot_diseo.data.api

/**
 * Simple in-memory token holder. Persist with DataStore/EncryptedSharedPreferences if needed.
 */
object TokenHolder {
    @Volatile
    var token: String? = null
}

