package com.ddowney.vehilytics.services

import com.ddowney.vehilytics.models.Reminder
import retrofit2.Call
import retrofit2.http.GET

/**
 * This service handles the endpoints relating to recommended reminders
 */
interface RemindersService {

    @GET("v1/reminders")
    fun getRemindersList(): Call<List<Reminder>>
}