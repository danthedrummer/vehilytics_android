package com.ddowney.vehilytics.services

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Provides access to a retrofit instance for performing requests
 * to the vehilytics api.
 */
object ServiceManager {

    private val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl("https://vehilytics-api.herokuapp.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    val authenticationService: AuthenticationService by lazy { retrofit.create(AuthenticationService::class.java) }

    val sensorsService: SensorsService by lazy { retrofit.create(SensorsService::class.java) }

    val readingsService: ReadingsService by lazy { retrofit.create(ReadingsService::class.java) }

    val deviceService: DeviceService by lazy { retrofit.create(DeviceService::class.java) }

    val remindersService: RemindersService by  lazy { retrofit.create(RemindersService::class.java) }
}