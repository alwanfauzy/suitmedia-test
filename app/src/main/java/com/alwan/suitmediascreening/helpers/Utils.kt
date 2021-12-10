package com.alwan.suitmediascreening.helpers

import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SnapHelper


// Format yyyy-mm-dd
fun String.getDateDayInt(): Int {
    val dateSplit = this.split("-").toTypedArray()

    return if (dateSplit.size == 3)
        dateSplit[2].toInt()
    else
        -1
}

fun String.getDateMonthInt(): Int {
    val dateSplit = this.split("-").toTypedArray()

    return if (dateSplit.size == 3)
        dateSplit[1].toInt()
    else
        -1
}

fun SnapHelper.getSnapPosition(recyclerView: RecyclerView): Int {
    val layoutManager = recyclerView.layoutManager ?: return RecyclerView.NO_POSITION
    val snapView = findSnapView(layoutManager) ?: return RecyclerView.NO_POSITION
    return layoutManager.getPosition(snapView)
}

fun RecyclerView.attachSnapHelperWithListener(
    snapHelper: SnapHelper,
    behavior: SnapOnScrollListener.Behavior = SnapOnScrollListener.Behavior.NOTIFY_ON_SCROLL,
    onSnapPositionChangeListener: SnapOnScrollListener.OnSnapPositionChangeListener
) {
    snapHelper.attachToRecyclerView(this)
    val snapOnScrollListener =
        SnapOnScrollListener(snapHelper, behavior, onSnapPositionChangeListener)
    addOnScrollListener(snapOnScrollListener)
}

fun String.isPalindrome(): Boolean {
    val sb = StringBuilder(this)
    val reverseStr = sb.reverse().toString()

    return this.equals(reverseStr, ignoreCase = true)
}

fun Int.isPrime(): Boolean {
    if (this < 2) return false
    for (i in 2..this / 2) {
        if (this % i == 0) {
            return false
        }
    }
    return true
}