package com.ddowney.vehilytics.services

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ServiceManager {

    private val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl("https://vehilytics-api.herokuapp.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    val authenticationService: AuthenticationService = retrofit.create(AuthenticationService::class.java)

    val sensorsService: SensorsService = retrofit.create(SensorsService::class.java)

    val readingsService: ReadingsService = retrofit.create(ReadingsService::class.java)

    val deviceService: DeviceService = retrofit.create(DeviceService::class.java)
}