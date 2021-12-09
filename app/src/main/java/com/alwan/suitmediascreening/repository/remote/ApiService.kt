package com.alwan.suitmediascreening.repository.remote

import com.alwan.suitmediascreening.repository.model.Guest
import retrofit2.Call
import retrofit2.http.GET

interface ApiService {
    @GET("596dec7f0f000023032b8017")
    fun getGuest(): Call<ArrayList<Guest>>
}