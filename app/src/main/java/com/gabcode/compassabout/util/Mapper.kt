package com.gabcode.compassabout.util

fun <T> T.asResult(): Result<T> {
    return try {
        Result.success(this)
    } catch (e: Exception) {
        Result.failure(e)
    }
}
