package com.ddowney.vehilytics.models

import com.google.gson.annotations.SerializedName

data class LoginRequest(val email: String, val password: String,
                        @SerializedName("firebase_token") val firebaseToken: String)