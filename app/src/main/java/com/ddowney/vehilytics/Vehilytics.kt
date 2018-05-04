package com.ddowney.vehilytics

import android.content.Context
import android.util.Log
import com.ddowney.vehilytics.models.Device
import com.ddowney.vehilytics.models.Sensor
import com.ddowney.vehilytics.models.User
import com.ddowney.vehilytics.services.ServiceManager
import com.ddowney.vehilytics.storage.Storage
import com.google.gson.reflect.TypeToken
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

object Vehilytics {

    private const val LOG_TAG = "Vehilytics"

    var user = User("", "")
    var device = Device("", "")
    var sensorPreferences = mapOf<String, Sensor>()

    fun clearUser() {
        user = User("", "")
    }

    fun clearDevice() {
        device = Device("", "")
    }

    fun clearPreferences() {
        sensorPreferences = mapOf()
    }

    fun clearAll(context: Context) {
        clearUser()
        clearDevice()
        clearPreferences()
        clearUserDetailsFromStorage(context)
    }

    fun storeUser(context: Context) {
        val storage = Storage(context)
        storage.writeObjectToStorage(Storage.USER_KEY, user)
    }

    fun retrieveUserFromStorage(context: Context){
        val storage = Storage(context)
        user = storage.readObjectFromStorage(Storage.USER_KEY, object: TypeToken<User>(){})
                ?: User("", "")
    }

    fun clearUserDetailsFromStorage(context: Context) {
        val storage = Storage(context)
        storage.removeKeyFromStorage(Storage.USER_KEY)
        storage.removeKeyFromStorage(Storage.DEVICE_KEY)
    }
}