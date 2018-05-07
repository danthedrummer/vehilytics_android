package com.ddowney.vehilytics.storage

import android.content.Context
import android.content.SharedPreferences
import com.ddowney.vehilytics.models.User
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import org.junit.Test

import org.junit.Assert.*
import org.junit.Before
import org.mockito.ArgumentMatchers.anyInt
import org.mockito.ArgumentMatchers.anyString
import org.mockito.Mockito

class StorageTest {

    private val context: Context = Mockito.mock(Context::class.java)
    private val sharedPrefs: SharedPreferences = Mockito.mock(SharedPreferences::class.java)
    private val editor: SharedPreferences.Editor = Mockito.mock(SharedPreferences.Editor::class.java)

    private lateinit var storage: Storage

    private val gson = Gson()

    private val user = User("rick.sanchez@c137.com", "wubalub")
    private val key = "user"

    @Before
    fun setup() {
        Mockito.`when`(context.getSharedPreferences(anyString(), anyInt())).thenReturn(sharedPrefs)
        Mockito.`when`(sharedPrefs.edit()).thenReturn(editor)
        Mockito.`when`(editor.putString(anyString(), anyString())).thenReturn(editor)
        storage = Storage(context)
    }

    @Test
    fun writeObjectToStorage() {
        storage.writeObjectToStorage(key, user)
        val inOrder = Mockito.inOrder(editor)
        inOrder.verify(editor).putString(key, gson.toJson(user))
        inOrder.verify(editor).apply()
    }

    @Test
    fun readObjectFromStorage() {
        Mockito.`when`(sharedPrefs.getString(anyString(), anyString())).thenReturn(gson.toJson(user))
        assertEquals(user, storage.readObjectFromStorage(key, TypeToken.get(User::class.java)))
    }

    @Test
    fun removeKeyFromStorage() {
        storage.removeKeyFromStorage(key)
        val inOrder = Mockito.inOrder(editor)
        inOrder.verify(editor).remove(key)
        inOrder.verify(editor).apply()
    }
}