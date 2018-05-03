package com.ddowney.vehilytics.services

import com.ddowney.vehilytics.models.Device
import retrofit2.Call
import retrofit2.http.*

interface DeviceService {

    @FormUrlEncoded
    @POST("v1/devices")
    fun attachDeviceToUser(@Header("X-User-Email") email: String,
                           @Header("X-User-Token") token: String,
                           @Field("device_name") deviceName: String): Call<Device>
}