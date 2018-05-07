package com.ddowney.vehilytics

import com.ddowney.vehilytics.models.Device
import com.ddowney.vehilytics.models.Sensor
import com.ddowney.vehilytics.models.User
import com.ddowney.vehilytics.storage.Storage
import com.google.gson.reflect.TypeToken
import org.junit.Test

import org.junit.Assert.*
import org.junit.Before
import org.mockito.Mockito

class VehilyticsTest {

    private val mockStorage: Storage = Mockito.mock(Storage::class.java)

    private val originalUser = User("rick.sanchez@c137.com", "wubalub")
    private val originalDevice = Device("portal_gun@device.com", "portal_gun")
    private val originalSensorPreferences: Map<String, Sensor> = mapOf(
            Pair("dream_inceptor", Sensor("1", "Dream Inceptor", "dream_inceptor", "dreams/night"))
    )

    @Before
    fun setup() {
        Vehilytics.user = originalUser
        Vehilytics.device = originalDevice
        Vehilytics.sensorPreferences = originalSensorPreferences
    }

    @Test
    fun getUser() {
        assertEquals(originalUser, Vehilytics.user)
    }

    @Test
    fun setUser() {
        val newUser = User("morty.smith@c137.com", "oh_geez")
        Vehilytics.user = newUser
        assertNotEquals(originalUser, Vehilytics.user)
        assertEquals(newUser, Vehilytics.user)
    }

    @Test
    fun getDevice() {
        assertEquals(originalDevice, Vehilytics.device)
    }

    @Test
    fun setDevice() {
        val newDevice = Device("neutrino.bomb@device.com", "neutrino_bomb")
        Vehilytics.device = newDevice
        assertNotEquals(originalDevice, Vehilytics.device)
        assertEquals(newDevice, Vehilytics.device)
    }

    @Test
    fun getSensorPreferences() {
        assertEquals(originalSensorPreferences, Vehilytics.sensorPreferences)
    }

    @Test
    fun setSensorPreferences() {
        val newPreferences: Map<String, Sensor> = mapOf(
                Pair("uncertainty_meter", Sensor("2", "Uncertainty Meter", "uncertainty_meter", "uncertainty"))
        )
        Vehilytics.sensorPreferences = newPreferences
        assertNotEquals(originalSensorPreferences, Vehilytics.sensorPreferences)
        assertEquals(newPreferences, Vehilytics.sensorPreferences)
    }

    @Test
    fun clearUserShouldSetUserToAnEmptyUserObject() {
        val emptyUser = User("", "")
        Vehilytics.clearUser()
        assertEquals(emptyUser, Vehilytics.user)
    }

    @Test
    fun clearDeviceShouldSetDeviceToAnEmptyDeviceObject() {
        val emptyDevice = Device("", "")
        Vehilytics.clearDevice()
        assertEquals(emptyDevice, Vehilytics.device)
    }

    @Test
    fun clearPreferencesShouldSetPreferencesToAnEmptyMap() {
        val emptyPreferences: Map<String, Sensor> = mapOf()
        Vehilytics.clearPreferences()
        assertEquals(emptyPreferences, Vehilytics.sensorPreferences)
    }

    @Test
    fun clearAllShouldSetAllAttributesToTheirEmptyForms() {
        val emptyUser = User("", "")
        val emptyDevice = Device("", "")
        val emptyPreferences: Map<String, Sensor> = mapOf()
        Vehilytics.clearAll(mockStorage)

        assertEquals(emptyUser, Vehilytics.user)
        assertEquals(emptyDevice, Vehilytics.device)
        assertEquals(emptyPreferences, Vehilytics.sensorPreferences)
    }

    @Test
    fun storeUser() {
        Vehilytics.storeUser(mockStorage)
        Mockito.verify(mockStorage).writeObjectToStorage(Storage.USER_KEY, Vehilytics.user)
    }

    @Test
    fun retrieveUserFromStorage() {
        Vehilytics.retrieveUserFromStorage(mockStorage)
        Mockito.verify(mockStorage).readObjectFromStorage(Storage.USER_KEY, TypeToken.get(User::class.java))
    }

    @Test
    fun clearUserDetailsFromStorage() {
        Vehilytics.clearUserDetailsFromStorage(mockStorage)
        Mockito.verify(mockStorage).removeKeyFromStorage(Storage.USER_KEY)
    }
}