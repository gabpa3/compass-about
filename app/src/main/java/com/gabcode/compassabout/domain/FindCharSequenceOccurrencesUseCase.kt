package com.gabcode.compassabout.domain

import com.gabcode.compassabout.data.IAboutRepository
import kotlinx.coroutines.CoroutineDispatcher

class FindCharSequenceOccurrencesUseCase(
    private val repository: IAboutRepository,
    private val defaultDispatcher: CoroutineDispatcher
) {
    suspend fun execute() {}
}
