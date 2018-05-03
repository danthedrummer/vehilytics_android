package com.ddowney.vehilytics.models

import com.google.gson.annotations.SerializedName

data class LoginResponse(val email: String, @SerializedName("authentication_token")val token: String)