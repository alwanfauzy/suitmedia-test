package com.alwan.suitmediascreening.helpers

sealed class LoadingState {
    object Success: LoadingState()
    data class Error(val message: String) : LoadingState()
    object Loading: LoadingState()
    object Empty: LoadingState()
}
