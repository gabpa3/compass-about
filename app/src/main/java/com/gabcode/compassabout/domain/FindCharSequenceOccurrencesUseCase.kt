package com.gabcode.compassabout.domain

import com.gabcode.compassabout.data.IAboutRepository
import com.gabcode.compassabout.util.asResult
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class FindCharSequenceOccurrencesUseCase(
    private val repository: IAboutRepository,
    private val defaultDispatcher: CoroutineDispatcher
) {
    suspend fun invoke(): Flow<Result<Collection<WordOccurrenceDomain>>> = flow {
        val data = repository.fetchContent().getOrThrow()

        val contentSplit = splitContent(data.content)
        val wordCount = mutableMapOf<String, Int>()
        val lowerToOriginal = hashMapOf<String, String>()

        contentSplit.forEach { word ->
            val lower = word.lowercase()
            val original = lowerToOriginal.getOrPut(lower) { word }
            wordCount[original] = wordCount.getOrDefault(original, 0) + 1
        }

        val result = mutableListOf<WordOccurrenceDomain>()
        wordCount.forEach { (key, value) ->
            result.add(WordOccurrenceDomain(key, value))
        }

        emit(result)
    }.flowOn(defaultDispatcher).asResult()

    private fun splitContent(content: String): List<String> {
        val result = mutableListOf<String>()
        val value = StringBuilder()

        content.forEach { char ->
            when {
                char == '<' && value.isNotEmpty() -> {
                    result.add(value.toString())
                    value.clear()
                    value.append(char)
                }
                char == '>' && value.isNotEmpty() -> {
                    value.append(char)
                    result.add(value.toString())
                    value.clear()
                }
                !char.isWhitespace() -> value.append(char)
                else -> {
                    if (value.isNotEmpty()) {
                        result.add(value.toString())
                        value.clear()
                    }
                }
            }
        }

        return result
    }
}
