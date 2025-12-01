package com.example.chatbot_diseo.network.client

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/**
 * Cliente Retrofit para conectar con ASP.NET Core Backend
 * Configuración centralizada para todas las peticiones HTTP
 */
object  RetrofitClient {

    /**
     * 🔧 IMPORTANTE: Cambiar esta URL por la de tu backend
     *
     * Ejemplos:
     * - Producción: "https://api.tudominio.com/"
     * - Desarrollo local: "http://10.0.2.2:5000/api/" (para emulador Android)
     * - Desarrollo local: "http://TU_IP_LOCAL:5000/api/" (para dispositivo físico)
     */
    private const val BASE_URL = "http://192.168.1.44:5288/api/"

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
     * Cliente HTTP con configuración de timeouts y logging
     */
    private val okHttpClient = OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .addInterceptor(loggingInterceptor)
        .addInterceptor { chain ->
            val request = chain.request().newBuilder()
                .addHeader("Content-Type", "application/json")
                .addHeader("Accept", "application/json")
                .build()
            chain.proceed(request)
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
