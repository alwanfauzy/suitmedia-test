package com.alwan.suitmediascreening.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.alwan.suitmediascreening.helpers.SettingPreferences
import kotlinx.coroutines.launch

class MainViewModel(private val pref: SettingPreferences) : ViewModel() {
    fun saveUserName(userName: String) {
        viewModelScope.launch {
            pref.saveUserName(userName)
        }
    }

    fun getUserName(): LiveData<String> {
        return pref.getUserName().asLiveData()
    }

    fun saveGuestName(guestName: String) {
        viewModelScope.launch {
            pref.saveGuestName(guestName)
        }
    }

    fun getGuestName(): LiveData<String> {
        return pref.getGuestName().asLiveData()
    }

    fun saveEventName(eventName: String) {
        viewModelScope.launch {
            pref.saveEventName(eventName)
        }
    }

    fun getEventName(): LiveData<String> {
        return pref.getEventName().asLiveData()
    }
}