package com.example.chatbot_diseo.data.remote.apiChatBot

import com.example.chatbot_diseo.data.api.TokenHolder
import com.example.chatbot_diseo.data.api.ApiService
import com.example.chatbot_diseo.data.remote.*
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
 * Configuración centralizada de URLs del backend
 */
object ApiConfig {
    // URL HTTPS para emulador Android (10.0.2.2 = localhost del PC)
    const val BASE_URL_EMULADOR_HTTPS = "https://10.0.2.2:7095/"

    // URL HTTP para emulador Android (más estable para desarrollo)
    const val BASE_URL_EMULADOR_HTTP = "http://10.0.2.2:5288/"

    // URL para dispositivo físico (cambiar por la IP de tu PC)
    const val BASE_URL_DISPOSITIVO = "http://192.168.100.22:5288/"

    // URL activa - usar HTTPS puerto 7095
    const val BASE_URL = BASE_URL_EMULADOR_HTTPS
}

/**
 * Cliente Retrofit unificado para todos los servicios del backend
 * Incluye autenticación con Bearer Token e interceptores de logging
 */
object RetrofitInstance {

    // Interceptor de logging para debug
    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    // Interceptor de autenticación Bearer Token
    private val authInterceptor = Interceptor { chain ->
        val original = chain.request()
        val token = TokenHolder.token
        val requestBuilder = original.newBuilder()
            .header("Accept", "application/json")
            .header("Content-Type", "application/json")
        if (!token.isNullOrBlank()) {
            requestBuilder.header("Authorization", "Bearer $token")
        }
        chain.proceed(requestBuilder.build())
    }

    /**
     * Cliente OkHttp configurado para desarrollo
     * NOTA: Acepta cualquier certificado SSL (solo para desarrollo)
     */
    private val unsafeClient: OkHttpClient by lazy {
        val trustAllCerts = arrayOf<TrustManager>(
            object : X509TrustManager {
                override fun checkClientTrusted(
                    chain: Array<out X509Certificate>?,
                    authType: String?
                ) = Unit

                override fun checkServerTrusted(
                    chain: Array<out X509Certificate>?,
                    authType: String?
                ) = Unit

                override fun getAcceptedIssuers(): Array<X509Certificate> = emptyArray()
            }
        )

        val sslContext = SSLContext.getInstance("SSL").apply {
            init(null, trustAllCerts, SecureRandom())
        }

        val trustManager = trustAllCerts[0] as X509TrustManager

        OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .addInterceptor(authInterceptor)
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .sslSocketFactory(sslContext.socketFactory, trustManager)
            .hostnameVerifier { _, _ -> true }
            .build()
    }

    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(ApiConfig.BASE_URL)
            .client(unsafeClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    // =====================================================
    // SERVICIOS DE LA API
    // =====================================================

    /** Servicio de Autenticación */
    val authApi: ApiService by lazy {
        retrofit.create(ApiService::class.java)
    }

    /** Servicio de Chatbot (IA con Ollama) */
    val chatbotApi: ChatbotApiService by lazy {
        retrofit.create(ChatbotApiService::class.java)
    }

    /** Servicio de Usuarios */
    val usuarioApi: UsuarioApiService by lazy {
        retrofit.create(UsuarioApiService::class.java)
    }

    /** Servicio de Conversaciones */
    val conversacionApi: ConversacionApiService by lazy {
        retrofit.create(ConversacionApiService::class.java)
    }

    /** Servicio de Mensajes Automáticos */
    val mensajeApi: MensajeApiService by lazy {
        retrofit.create(MensajeApiService::class.java)
    }

    /** Servicio de Documentos */
    val documentosApi: ApiChatBotDocumentos by lazy {
        retrofit.create(ApiChatBotDocumentos::class.java)
    }

    /** Servicio de Actividades */
    val actividadesApi: ApiActividadService by lazy {
        retrofit.create(ApiActividadService::class.java)
    }

    /** Servicio de FAQs */
    val faqApi: FAQApiService by lazy {
        retrofit.create(FAQApiService::class.java)
    }

    /** Servicio de Configuración */
    val configuracionApi: ConfiguracionApiService by lazy {
        retrofit.create(ConfiguracionApiService::class.java)
    }
}

