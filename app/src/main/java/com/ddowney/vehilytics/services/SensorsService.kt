package com.ddowney.vehilytics.services

import com.ddowney.vehilytics.models.Sensor
import retrofit2.Call
import retrofit2.http.*

interface SensorsService {

    @GET("v1/sensors")
    fun getAllSensors(): Call<List<Sensor>>

    @GET("v1/sensors")
    fun getReportedSensors(@Header("X-User-Email") email: String,
                           @Header("X-User-Token") token: String): Call<List<Sensor>>

    @GET("v1/sensors")
    fun getRequestedSensors(@Header("X-User-Email") email: String,
                            @Header("X-User-Token") token: String): Call<List<Sensor>>

    @POST("v1/sensors")
    fun updateRequestedSensors(@Header("X-User-Email") email: String,
                               @Header("X-User-Token") token: String,
                               @Field("sensors") sensors: List<String>): Call<Void>
}