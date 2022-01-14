package com.alwan.suitmediascreening.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MainViewModel : ViewModel() {
    private val _eventName = MutableLiveData<String>()
    val eventName : LiveData<String> = _eventName
    private val _guestName = MutableLiveData<String>()
    val guestName : LiveData<String> = _guestName

    fun setEventName(name: String){
        _eventName.postValue(name)
    }

    fun setGuestName(name: String){
        _guestName.postValue(name)
    }
}