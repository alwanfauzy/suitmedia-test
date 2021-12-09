package com.alwan.suitmediascreening.repository.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Guest(
    val name: String,
    val birthdate: String,
    val image: Int? = null,
) : Parcelable
