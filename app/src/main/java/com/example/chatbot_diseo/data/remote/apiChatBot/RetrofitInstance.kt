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
    // ✅ API corriendo en: http://localhost:5288 (HTTP, no HTTPS)
    // Para emulador: http://10.0.2.2:5288
    const val BASE_URL = "http://10.0.2.2:5288/api/"
}

/**
 * Cliente Retrofit unificado para todos los servicios del backend
 * Incluye autenticación con Bearer Token e interceptores de logging
 */
object RetrofitInstance {

    // Interceptor de logging para debug
    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BASIC
    }

    // Interceptor SIN logging para máxima velocidad (chatbot)
    private val noLoggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.NONE
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
     * Cliente OkHttp para login SOLAMENTE (sin interceptor de autenticación, pero con headers correctos)
     */
    private val loginClient: OkHttpClient by lazy {
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

        // Interceptor para agregar headers necesarios en login
        val headerInterceptor = Interceptor { chain ->
            val original = chain.request()
            val requestBuilder = original.newBuilder()
                .header("Accept", "application/json")
                .header("Content-Type", "application/json")
            chain.proceed(requestBuilder.build())
        }

        OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .addInterceptor(headerInterceptor)
            .connectTimeout(15, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(15, TimeUnit.SECONDS)
            .sslSocketFactory(sslContext.socketFactory, trustManager)
            .hostnameVerifier { _, _ -> true }
            .build()
    }

    /**
     * Cliente OkHttp optimizado para operaciones con token (timeouts cortos para rapidez)
     */
    private val fastClient: OkHttpClient by lazy {
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
            .connectTimeout(15, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(15, TimeUnit.SECONDS)
            .sslSocketFactory(sslContext.socketFactory, trustManager)
            .hostnameVerifier { _, _ -> true }
            .build()
    }

    /**
     * Cliente OkHttp optimizado para chatbot (máxima velocidad, sin logging)
     */
    private val chatbotClient: OkHttpClient by lazy {
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
            .addInterceptor(noLoggingInterceptor)
            .addInterceptor(authInterceptor)
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .sslSocketFactory(sslContext.socketFactory, trustManager)
            .hostnameVerifier { _, _ -> true }
            .build()
    }

    /**
     * Cliente OkHttp para operaciones de larga duración (documentos, etc)
     */
    private val slowClient: OkHttpClient by lazy {
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
            .connectTimeout(60, TimeUnit.SECONDS)
            .readTimeout(120, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS)
            .sslSocketFactory(sslContext.socketFactory, trustManager)
            .hostnameVerifier { _, _ -> true }
            .build()
    }

    private val retrofitLogin: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(ApiConfig.BASE_URL)
            .client(loginClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    private val retrofitFast: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(ApiConfig.BASE_URL)
            .client(fastClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    private val retrofitChatbot: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(ApiConfig.BASE_URL)
            .client(chatbotClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    private val retrofitSlow: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(ApiConfig.BASE_URL)
            .client(slowClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    // =====================================================
    // SERVICIOS DE LA API
    // =====================================================

    /** Servicio de Autenticación (sin interceptor, sin token) */
    val authApi: ApiService by lazy {
        retrofitLogin.create(ApiService::class.java)
    }

    /** Servicio de Chatbot (optimizado para velocidad - SIN LOGGING) */
    val chatbotApi: ChatbotApiService by lazy {
        retrofitChatbot.create(ChatbotApiService::class.java)
    }

    /** Servicio de Usuarios (rápido) */
    val usuarioApi: UsuarioApiService by lazy {
        retrofitFast.create(UsuarioApiService::class.java)
    }

    /** Servicio de Conversaciones (rápido) */
    val conversacionApi: ConversacionApiService by lazy {
        retrofitFast.create(ConversacionApiService::class.java)
    }

    /** Servicio de Mensajes Automáticos (rápido) */
    val mensajeApi: MensajeApiService by lazy {
        retrofitFast.create(MensajeApiService::class.java)
    }

    /** Servicio de Documentos (lento) */
    val documentosApi: ApiChatBotDocumentos by lazy {
        retrofitSlow.create(ApiChatBotDocumentos::class.java)
    }

    /** Servicio de Actividades (rápido) */
    val actividadesApi: ApiActividadService by lazy {
        retrofitFast.create(ApiActividadService::class.java)
    }

    /** Servicio de FAQs (rápido) */
    val faqApi: FAQApiService by lazy {
        retrofitFast.create(FAQApiService::class.java)
    }

    /** Servicio de Configuración (rápido) */
    val configuracionApi: ConfiguracionApiService by lazy {
        retrofitFast.create(ConfiguracionApiService::class.java)
    }
}
