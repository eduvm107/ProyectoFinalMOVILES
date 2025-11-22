package com.example.chatbot_diseo.data.remote.apiChatBot

import com.example.chatbot_diseo.data.remote.model.Actividad
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

typealias ActividadRemota = Actividad.Actividad

interface ApiActividadService {

    @GET("Actividad")
    suspend fun getAllActividades(): List<ActividadRemota>

    @GET("Actividad/{id}")
    suspend fun getActividadById(
        @Path("id") id: String
    ): ActividadRemota

    @POST("Actividad")
    suspend fun createActividad(
        @Body actividad: ActividadRemota
    ): ActividadRemota

    @PUT("Actividad/{id}")
    suspend fun updateActividad(
        @Path("id") id: String,
        @Body actividad: ActividadRemota
    )

    @DELETE("Actividad/{id}")
    suspend fun deleteActividad(
        @Path("id") id: String
    )

    @GET("Actividad/dia/{dia}")
    suspend fun getByDia(
        @Path("dia") dia: Int
    ): List<ActividadRemota>

    @GET("Actividad/tipo/{tipo}")
    suspend fun getByTipo(
        @Path("tipo") tipo: String
    ): List<ActividadRemota>

    @GET("Actividad/obligatorias")
    suspend fun getObligatorias(): List<ActividadRemota>
}

