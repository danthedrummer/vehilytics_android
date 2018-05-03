package com.ddowney.vehilytics.services

import com.ddowney.vehilytics.models.ReadingsResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface ReadingsService {

    @GET("v1/readings")
    fun getReadings(@Header("X-User-Email") email: String,
                    @Header("X-User-Token") token: String): Call<ReadingsResponse>

    @GET("v1/readings")
    fun getReadingsForSensor(@Header("X-User-Email") email: String,
                             @Header("X-User-Token") token: String,
                             @Query("sensor") sensor: String): Call<ReadingsResponse>
}