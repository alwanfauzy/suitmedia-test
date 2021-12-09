package com.alwan.suitmediascreening.helpers

// Format yyyy-mm-dd
fun String.getDateDayInt(): Int{
    val dateSplit = this.split("-").toTypedArray()

    return if(dateSplit.size == 3)
        dateSplit[2].toInt()
    else
        -1
}