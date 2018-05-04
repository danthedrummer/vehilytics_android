package com.ddowney.vehilytics.services

import com.ddowney.vehilytics.models.Device
import retrofit2.Call
import retrofit2.http.*

interface DeviceService {

    @GET("v1/devices")
    fun getDeviceInfo(@Header("X-User-Email") email: String,
                      @Header("X-User-Token") token: String): Call<Device>

    @FormUrlEncoded
    @POST("v1/devices?function=attach")
    fun attachDeviceToUser(@Header("X-User-Email") email: String,
                           @Header("X-User-Token") token: String,
                           @Field("device_name") deviceName: String): Call<Device>

    @FormUrlEncoded
    @POST("v1/devices?function=detach")
    fun detachDeviceFromUser(@Header("X-User-Email") email: String,
                           @Header("X-User-Token") token: String,
                           @Field("device_name") deviceName: String): Call<Void>
}