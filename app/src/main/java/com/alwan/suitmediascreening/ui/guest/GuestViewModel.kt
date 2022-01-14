package com.alwan.suitmediascreening.ui.guest

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.alwan.suitmediascreening.repository.model.Guest
import com.alwan.suitmediascreening.repository.remote.ApiService
import com.alwan.suitmediascreening.repository.remote.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class GuestViewModel : ViewModel() {
    private val _guests = MutableLiveData<ArrayList<Guest>>()
    val guests: LiveData<ArrayList<Guest>> get() = _guests
    private val _loadingState = MutableLiveData<Boolean>()
    val loadingState: LiveData<Boolean> get() = _loadingState

    fun setGuest() {
        _loadingState.value = true
        val service = RetrofitClient.buildService(ApiService::class.java)
        val requestCall = service.getGuest()

        requestCall.enqueue(object : Callback<ArrayList<Guest>> {
            override fun onResponse(
                call: Call<ArrayList<Guest>>,
                response: Response<ArrayList<Guest>>
            ) {
                if (response.isSuccessful) {
                    _guests.postValue(response.body()!!)
                }
                _loadingState.value = false
            }

            override fun onFailure(call: Call<ArrayList<Guest>>, t: Throwable) {
                Log.e("GuestViewModel", "setGuest onFailure ${t.message}")
                _loadingState.value = false
            }
        })
    }
}