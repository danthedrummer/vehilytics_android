package com.ddowney.vehilytics.models

import com.google.gson.annotations.SerializedName

data class Device(val email: String,
                  @SerializedName("device_name") val deviceName: String)