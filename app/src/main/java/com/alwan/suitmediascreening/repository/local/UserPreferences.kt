package com.alwan.suitmediascreening.helpers

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class SettingPreferences private constructor(private val dataStore: DataStore<Preferences>) {

    private val USER_KEY = stringPreferencesKey("user_name")
    private val GUEST_KEY = stringPreferencesKey("guest_name")
    private val EVENT_KEY = stringPreferencesKey("event_name")

    suspend fun saveUserName(userName: String) {
        dataStore.edit { preferences ->
            preferences[USER_KEY] = userName
        }
    }

    fun getUserName(): Flow<String> {
        return dataStore.data.map { preferences ->
            preferences[USER_KEY] ?: "-"
        }
    }

    suspend fun saveGuestName(guestName: String) {
        dataStore.edit { preferences ->
            preferences[GUEST_KEY] = guestName
        }
    }

    fun getGuestName(): Flow<String> {
        return dataStore.data.map { preferences ->
            preferences[GUEST_KEY] ?: "Pilih Guest"
        }
    }

    suspend fun saveEventName(eventName: String) {
        dataStore.edit { preferences ->
            preferences[EVENT_KEY] = eventName
        }
    }

    fun getEventName(): Flow<String> {
        return dataStore.data.map { preferences ->
            preferences[EVENT_KEY] ?: "Pilih Event"
        }
    }

    suspend fun reset() {
        dataStore.edit { preferences ->
            preferences.remove(USER_KEY)
            preferences.remove(GUEST_KEY)
            preferences.remove(EVENT_KEY)
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: SettingPreferences? = null

        fun getInstance(dataStore: DataStore<Preferences>): SettingPreferences {
            return INSTANCE ?: synchronized(this) {
                val instance = SettingPreferences(dataStore)
                INSTANCE = instance
                instance
            }
        }
    }
}