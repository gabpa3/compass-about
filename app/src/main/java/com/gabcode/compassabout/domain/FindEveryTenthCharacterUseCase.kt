package com.gabcode.compassabout.domain

import com.gabcode.compassabout.data.IAboutRepository
import com.gabcode.compassabout.util.asResult
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class FindEveryTenthCharacterUseCase(
    private val repository: IAboutRepository,
    private val defaultDispatcher: CoroutineDispatcher
) {
    fun invoke(): Flow<Result<Collection<TenthCharacterDomain>>> = flow {
        val data = repository.fetchContent().getOrThrow()
        val content = data.content
        val result = mutableListOf<TenthCharacterDomain>()

        for (i in TENTH_INDEX..content.length) {
            if (i % TENTH_INDEX == 0) {
                val domainModel = TenthCharacterDomain(content[i-1], i)
                result.add(domainModel)
            }
        }

        emit(result)
    }.flowOn(defaultDispatcher).asResult()

    companion object {
        private const val TENTH_INDEX = 10
    }
}
