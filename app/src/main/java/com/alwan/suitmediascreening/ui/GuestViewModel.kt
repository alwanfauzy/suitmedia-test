package com.alwan.suitmediascreening.ui

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.alwan.suitmediascreening.helpers.LoadingState
import com.alwan.suitmediascreening.repository.model.Guest
import com.alwan.suitmediascreening.repository.remote.ApiService
import com.alwan.suitmediascreening.repository.remote.RetrofitClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class GuestViewModel : ViewModel() {
    private val _guests = MutableLiveData<ArrayList<Guest>>()
    val guests: LiveData<ArrayList<Guest>> get() = _guests
    private val _loadingState = MutableStateFlow<LoadingState>(LoadingState.Empty)
    val loadingState: StateFlow<LoadingState> get() = _loadingState

    fun setGuest() {
        _loadingState.value = LoadingState.Loading
        val service = RetrofitClient.buildService(ApiService::class.java)
        val requestCall = service.getGuest()

        requestCall.enqueue(object : Callback<ArrayList<Guest>> {
            override fun onResponse(
                call: Call<ArrayList<Guest>>,
                response: Response<ArrayList<Guest>>
            ) {
                if (response.code() < 300) {
                    _guests.postValue(response.body()!!)
                    _loadingState.value = LoadingState.Success
                } else {
                    _loadingState.value = LoadingState.Error("Error, response ${response.code()}")
                }
            }

            override fun onFailure(call: Call<ArrayList<Guest>>, t: Throwable) {
                _loadingState.value = LoadingState.Error("Error, onFailure")
            }
        })
    }
}