package com.ddowney.vehilytics.models

import com.google.gson.annotations.SerializedName

data class ReadingsResponse(val readings: List<Reading>, val info: String,
                            @SerializedName("upper_range")val upperRange: Float?,
                            @SerializedName("lower_range")val lowerRange: Float?)