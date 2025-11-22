package com.example.chatbot_diseo.data.remote // <--- ESTO es lo que faltaba: ".data"

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {

    // Usamos 10.0.2.2 para el emulador. Si usas celular real, pon tu IP de PC.
    private const val BASE_URL = "http://10.0.2.2:5288/"

    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val api: MensajeApiService by lazy {
        retrofit.create(MensajeApiService::class.java)
    }

    val conversacionApi: ConversacionApiService by lazy {
        retrofit.create(ConversacionApiService::class.java)
    }



}