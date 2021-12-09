package com.alwan.suitmediascreening.repository.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Event(
    val nama: String,
    val tanggal: String,
    val image: Int,
) : Parcelable
