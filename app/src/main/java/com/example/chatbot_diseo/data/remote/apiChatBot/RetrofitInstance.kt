package com.example.chatbot_diseo.data.remote.apiChatBot

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.security.SecureRandom
import java.security.cert.X509Certificate
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager

// Usa esta si trabajas con emulador
const val BASE_URL_EMULADOR = "http://10.0.2.2:5288/api/"

// Usa esta si trabajas con celular físico (reemplaza la IP por la de tu PC)
const val BASE_URL_DISPOSITIVO = "http://192.168.100.22:5288/api/"

// Cambia aquí según necesites
const val BASE_URL = BASE_URL_EMULADOR

object RetrofitInstance {

    /**
     * Cliente inseguro que acepta cualquier certificado.
     * ÚSALO SOLO PARA DESARROLLO / PRUEBAS.
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
            .sslSocketFactory(sslContext.socketFactory, trustManager)
            .hostnameVerifier { _, _ -> true }
            .build()
    }

    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(unsafeClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val documentosApi: ApiChatBotDocumentos by lazy {
        retrofit.create(ApiChatBotDocumentos::class.java)
    }

    val actividadesApi: ApiActividadService by lazy {
        retrofit.create(ApiActividadService::class.java)
    }
}

