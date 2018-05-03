package com.ddowney.vehilytics.models

import com.google.gson.annotations.SerializedName

data class UserRegistrationModel(val email: String, val password: String,
                                 @SerializedName("password_confirmation") val passwordConfirmation: String)