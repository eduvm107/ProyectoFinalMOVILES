package com.example.chatbot_diseo.data.common

/**
 * Sealed class para manejar estados de peticiones HTTP
 * - Success: Respuesta exitosa con datos
 * - Error: Error con mensaje descriptivo
 * - Loading: Estado de carga
 */
sealed class Result<out T> {
    data class Success<T>(val data: T) : Result<T>()
    data class Error(val message: String, val code: Int? = null) : Result<Nothing>()
    object Loading : Result<Nothing>()

    val isSuccess: Boolean
        get() = this is Success

    val isError: Boolean
        get() = this is Error

    val isLoading: Boolean
        get() = this is Loading
}

/**
 * Extensión para ejecutar código solo si el resultado es exitoso
 */
inline fun <T> Result<T>.onSuccess(action: (T) -> Unit): Result<T> {
    if (this is Result.Success) {
        action(data)
    }
    return this
}

/**
 * Extensión para ejecutar código solo si hay error
 */
inline fun <T> Result<T>.onError(action: (String) -> Unit): Result<T> {
    if (this is Result.Error) {
        action(message)
    }
    return this
}

