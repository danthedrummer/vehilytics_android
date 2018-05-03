package com.ddowney.vehilytics.models

import com.google.gson.annotations.SerializedName

data class Reading(val sensor: String, val value: String,
                   @SerializedName("time_reported") val time: String)