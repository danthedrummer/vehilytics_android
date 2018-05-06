package com.ddowney.vehilytics.services

import com.ddowney.vehilytics.models.Reminder
import retrofit2.Call
import retrofit2.http.GET

interface RemindersService {

    @GET("v1/reminders")
    fun getRemindersList(): Call<List<Reminder>>
}