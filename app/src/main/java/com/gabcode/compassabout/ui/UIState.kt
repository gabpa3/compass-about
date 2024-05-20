package com.gabcode.compassabout.ui

sealed class UIState<out T> {
    data object Idle : UIState<Nothing>()
    data object Loading : UIState<Nothing>()
    data class Success<out T>(val data: T) : UIState<T>()
    data class Error(val throwable: Throwable) : UIState<Nothing>()
}
