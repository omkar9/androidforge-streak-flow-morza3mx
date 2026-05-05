package com.androidforge.streakflow.core.common

import com.androidforge.streakflow.presentation.common.util.UiText

sealed class Result<out T> {
    data class Success<out T>(val data: T?) : Result<T>()
    data class Error(val message: UiText?, val exception: Throwable? = null) : Result<Nothing>()
    object Loading : Result<Nothing>()
    object Offline : Result<Nothing>()
}