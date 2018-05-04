package com.ddowney.vehilytics

object User {
    var email = ""
    var token = ""
    var deviceName = "None"

    fun setUser(email: String, token: String) {
        this.email = email
        this.token = token
    }

    fun clearUser() {
        email = ""
        token = ""
    }
}