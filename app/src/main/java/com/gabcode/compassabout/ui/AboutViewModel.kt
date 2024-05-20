package com.gabcode.compassabout.ui

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gabcode.compassabout.domain.FindCharSequenceOccurrencesUseCase
import com.gabcode.compassabout.domain.FindEveryTenthCharacterUseCase
import com.gabcode.compassabout.domain.GrabContentUseCase
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AboutViewModel(
    private val grabContentUseCase: GrabContentUseCase,
    private val findEveryTenthCharacterUseCase: FindEveryTenthCharacterUseCase,
    private val findCharSequenceOccurrencesUseCase: FindCharSequenceOccurrencesUseCase
) : ViewModel() {

    private val mWordOccurrences = MutableStateFlow<UIState<List<WordOccurrenceItem>>>(UIState.Idle)
    val wordOccurrences: StateFlow<UIState<List<WordOccurrenceItem>>> = mWordOccurrences.asStateFlow()

    private val mTenthCharacters = MutableStateFlow<UIState<List<TenthCharacterItem>>>(UIState.Idle)
    val tenthCharacters: StateFlow<UIState<List<TenthCharacterItem>>> = mTenthCharacters.asStateFlow()

    fun fetchContent() {
        viewModelScope.launch(CoroutineName("fetchContent")) {
            Log.d("AboutViewModel", "fetching content...")
            grabContentUseCase.invoke()
        }
    }

    fun startProcessingData() {
        with(viewModelScope) {
            Log.d("AboutViewModel", "start processing data...")
            launch(CoroutineName("findTenthCharacters")) { findTenthCharacters() }
            launch(CoroutineName("findWordOccurrences")) { findWordOccurrences() }
        }
    }

    private suspend fun findTenthCharacters() {
        findEveryTenthCharacterUseCase.invoke().collect { result ->
            if (result.isFailure) {
                mTenthCharacters.update {
                    val exception = result.exceptionOrNull() ?: IllegalStateException("Unknown error")
                    UIState.Error(exception)
                }
                return@collect
            }

            result.getOrNull()?.let { data ->
                val uiModel = data.toUIModel()
                Log.d("AboutViewModel", "findTenthCharacters result: $uiModel")

                mTenthCharacters.update {
                    UIState.Success(uiModel)
                }
            }
        }
    }

    private suspend fun findWordOccurrences() {
        findCharSequenceOccurrencesUseCase.invoke().collect { result ->
            if (result.isFailure) {
                mWordOccurrences.update {
                    val exception =
                        result.exceptionOrNull() ?: IllegalStateException("Unknown error")
                    UIState.Error(exception)
                }
                return@collect
            }

            result.getOrNull()?.let { data ->
                val uiModel = data.toUIModel()
                Log.d("AboutViewModel", "findOccurrences result: $uiModel")

                mWordOccurrences.update {
                    UIState.Success(uiModel)
                }
            }
        }
    }
}
