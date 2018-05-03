package com.ddowney.vehilytics.services

import com.ddowney.vehilytics.models.Sensor
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header

interface SensorsService {

    @GET("v1/sensors")
    fun getReportedSensors(@Header("X-User-Email") email: String,
                           @Header("X-User-Token") token: String): Call<List<Sensor>>
}