package com.example.chatbot_diseo.network.client

import com.example.chatbot_diseo.data.api.TokenHolder
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.security.SecureRandom
import java.security.cert.X509Certificate
import java.util.concurrent.TimeUnit
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager

/**
 * Cliente Retrofit para conectar con ASP.NET Core Backend
 * Configuración centralizada para todas las peticiones HTTP
 */
object RetrofitClient {

    /**
     * URL base del backend ASP.NET Core
     * ✅ API corriendo en: http://localhost:5288 (puerto HTTP)
     * Para emulador Android: http://10.0.2.2:5288/api/
     */
    private const val BASE_URL = "http://10.0.2.2:5288/api/"

    /**
     * Configuración de Gson para serialización/deserialización
     */
    private val gson: Gson = GsonBuilder()
        .setLenient()
        .setDateFormat("yyyy-MM-dd'T'HH:mm:ss")
        .create()

    /**
     * Interceptor para logging de peticiones HTTP (solo en DEBUG)
     */
    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    /**
     * Interceptor de autenticación con Bearer Token
     */
    private val authInterceptor = Interceptor { chain ->
        val original = chain.request()
        val token = TokenHolder.token
        val requestBuilder = original.newBuilder()
            .addHeader("Content-Type", "application/json")
            .addHeader("Accept", "application/json")

        // Agregar token de autenticación si está disponible
        if (!token.isNullOrBlank()) {
            requestBuilder.addHeader("Authorization", "Bearer $token")
        }

        chain.proceed(requestBuilder.build())
    }

    /**
     * Cliente HTTP con configuración de timeouts y logging
     */
    private val okHttpClient = OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .addInterceptor(loggingInterceptor)
        .addInterceptor(authInterceptor)
        // Confiar en todos los certificados (para desarrollo local con HTTP)
        .apply {
            try {
                val trustAllCerts = arrayOf<TrustManager>(
                    object : X509TrustManager {
                        override fun checkClientTrusted(chain: Array<out X509Certificate>?, authType: String?) = Unit
                        override fun checkServerTrusted(chain: Array<out X509Certificate>?, authType: String?) = Unit
                        override fun getAcceptedIssuers(): Array<X509Certificate> = emptyArray()
                    }
                )

                val sslContext = SSLContext.getInstance("SSL").apply {
                    init(null, trustAllCerts, SecureRandom())
                }

                val trustManager = trustAllCerts[0] as X509TrustManager

                sslSocketFactory(sslContext.socketFactory, trustManager)
                hostnameVerifier { _, _ -> true }
            } catch (e: Exception) {
                throw RuntimeException(e)
            }
        }
        .build()

    /**
     * Instancia de Retrofit
     */
    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }

    /**
     * Método genérico para crear servicios de API
     */
    fun <T> createService(serviceClass: Class<T>): T {
        return retrofit.create(serviceClass)
    }
}
