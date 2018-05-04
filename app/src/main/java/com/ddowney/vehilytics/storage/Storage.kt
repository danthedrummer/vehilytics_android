package com.ddowney.vehilytics.storage

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class Storage(val context: Context) {

    companion object {
        private const val PREFERENCES = "VehilyticsPreferences"
        const val USER_KEY = "USER_KEY"
        const val DEVICE_KEY = "DEVICE"
    }

    private val sharedPrefs = context.getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE)

    private val gson = Gson()

    /**
     * Write an object to Shared Preferences
     *
     * @param key: The key to store the object against
     * @param data: The object to be stored
     */
    fun <T> writeObjectToStorage(key: String, data: T) {
        val editor = sharedPrefs.edit()
        editor.putString(key, gson.toJson(data))
        editor.apply()
    }

    /**
     * Read an object from Shared Preferences
     *
     * @param key: The key to be retrieved
     *
     * @return: The object retrieved from storage
     */
    fun <T> readObjectFromStorage(key: String, token: TypeToken<T>): T? {
        val storedString = sharedPrefs.getString(key, "")
        return gson.fromJson(storedString, token.type)
    }

    fun removeKeyFromStorage(key: String) {
        val editor = sharedPrefs.edit()
        editor.remove(key)
        editor.apply()
    }
}