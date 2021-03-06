package com.ddowney.vehilytics.models

import com.google.gson.annotations.SerializedName

data class RemindersResponse(val title: String, val description: String,
                             @SerializedName("weekly_frequency") val weeklyFrequency: Int)