package com.ddowney.vehilytics

import com.ddowney.vehilytics.models.Device
import com.ddowney.vehilytics.models.Sensor
import com.ddowney.vehilytics.models.User
import com.ddowney.vehilytics.storage.Storage
import com.google.firebase.iid.FirebaseInstanceId
import com.google.gson.reflect.TypeToken

/**
 * The knowledge expert on all things "User"
 *
 * Provides a means of quick access to the user credentials, device info,
 * and sensor preferences once they have been stored.
 */
object Vehilytics {

    var user = User("", "")
    var device = Device("", "")
    var sensorPreferences = mapOf<String, Sensor>()

    var firebaseToken = FirebaseInstanceId.getInstance().token ?: ""

    /**
     * Clears the current user info
     */
    fun clearUser() {
        user = User("", "")
    }

    /**
     * Clears the current device info
     */
    fun clearDevice() {
        device = Device("", "")
    }

    /**
     * Clears the current user sensor preferences
     */
    fun clearPreferences() {
        sensorPreferences = mapOf()
    }

    /**
     * Clears all user information
     *
     * @param storage: The storage class to be used
     */
    fun clearAll(storage: Storage) {
        clearUser()
        clearDevice()
        clearPreferences()
        clearUserDetailsFromStorage(storage)
    }

    /**
     * Stores the current user credentials to Shared Preferences
     *
     * @param storage: The storage class to be used
     */
    fun storeUser(storage: Storage) {
        storage.writeObjectToStorage(Storage.USER_KEY, user)
    }

    /**
     * Gets any user information stored in Shared Preferences.
     * Sets a default empty user if there is nothing stored.
     *
     * @param storage: The storage class to be used
     */
    fun retrieveUserFromStorage(storage: Storage){
        user = storage.readObjectFromStorage(Storage.USER_KEY, object: TypeToken<User>(){})
                ?: User("", "")
    }

    /**
     * Removes any stored user credentials from Shared Preferences
     *
     * @param storage: The storage class to be used
     */
    fun clearUserDetailsFromStorage(storage: Storage) {
        storage.removeKeyFromStorage(Storage.USER_KEY)
        storage.removeKeyFromStorage(Storage.DEVICE_KEY)
    }
}