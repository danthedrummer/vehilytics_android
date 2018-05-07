package com.ddowney.vehilytics

import com.ddowney.vehilytics.models.Device
import com.ddowney.vehilytics.models.Sensor
import com.ddowney.vehilytics.models.User
import com.ddowney.vehilytics.storage.Storage
import com.google.gson.reflect.TypeToken

object Vehilytics {

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

    fun clearAll(storage: Storage) {
        clearUser()
        clearDevice()
        clearPreferences()
        clearUserDetailsFromStorage(storage)
    }

    fun storeUser(storage: Storage) {
        storage.writeObjectToStorage(Storage.USER_KEY, user)
    }

    fun retrieveUserFromStorage(storage: Storage){
        user = storage.readObjectFromStorage(Storage.USER_KEY, object: TypeToken<User>(){})
                ?: User("", "")
    }

    fun clearUserDetailsFromStorage(storage: Storage) {
        storage.removeKeyFromStorage(Storage.USER_KEY)
        storage.removeKeyFromStorage(Storage.DEVICE_KEY)
    }
}