package com.alwan.suitmediascreening.repository.model

import android.os.Parcelable
import com.google.android.gms.maps.model.LatLng
import kotlinx.parcelize.Parcelize

@Parcelize
data class Event(
    val nama: String,
    val tanggal: String,
    val image: Int,
    val latLng: LatLng,
) : Parcelable
