package com.gabcode.compassabout.util

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map

fun <T> Flow<Collection<T>>.asResult(): Flow<Result<Collection<T>>> {
    return this
        .map { Result.success(it) }
        .catch { emit(Result.failure(it)) }
}
