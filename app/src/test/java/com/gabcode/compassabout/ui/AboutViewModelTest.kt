package com.gabcode.compassabout.ui

import android.util.Log
import com.gabcode.compassabout.domain.FindCharSequenceOccurrencesUseCase
import com.gabcode.compassabout.domain.FindEveryTenthCharacterUseCase
import com.gabcode.compassabout.domain.GrabContentUseCase
import com.gabcode.compassabout.domain.TenthCharacterDomain
import com.gabcode.compassabout.domain.WordOccurrenceDomain
import com.gabcode.compassabout.util.asResult
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockkStatic
import io.mockk.spyk
import io.mockk.unmockkAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class AboutViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @MockK
    private lateinit var grabContentUseCase: GrabContentUseCase

    @MockK
    private lateinit var findTenthUseCaseMock: FindEveryTenthCharacterUseCase

    @MockK
    private lateinit var findOccurrencesMock: FindCharSequenceOccurrencesUseCase

    private lateinit var sut: AboutViewModel

    @Before
    fun setUp() {
        MockKAnnotations.init(this, relaxed = true)

        sut = spyk(AboutViewModel(grabContentUseCase, findTenthUseCaseMock, findOccurrencesMock))
    }

    @After
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun `when fetch content then call grab content usecase`() = runTest {
       mockLog()

        sut.fetchContent()

        coVerify { grabContentUseCase.invoke() }
    }

    @Test
    fun `when start processing data then call usecases`() = runTest {
        mockLog()

        sut.startProcessingData()

        coVerify { findTenthUseCaseMock.invoke() }
        coVerify { findOccurrencesMock.invoke() }
    }

    @Test
    fun `when find tenth character use case get data then notify its success state`() = runTest {
        mockLog()
        val flowResult = flow<Collection<TenthCharacterDomain>> {
            val list = listOf(
                TenthCharacterDomain('a', 10),
                TenthCharacterDomain('b', 20),
                TenthCharacterDomain('c', 30)
            )
            emit(list)
        }.asResult()
        coEvery { findTenthUseCaseMock.invoke() } returns flowResult

        sut.startProcessingData()

        val collectUiState = sut.tenthCharacters.value
        assertTrue(collectUiState is UIState.Success)
        assertTrue((collectUiState as UIState.Success).data.size == 3)
    }

    @Test
    fun `when find tenth character use case result failure then notify its state`() = runTest {
        mockLog()
        val flowResult = flow<Collection<TenthCharacterDomain>> {
            throw IllegalStateException("Error throw in test")
        }.asResult()
        coEvery { findTenthUseCaseMock.invoke() } returns flowResult

        sut.startProcessingData()

        val collectUiState = sut.tenthCharacters.value
        assertTrue(collectUiState is UIState.Error)
        assertTrue((collectUiState as UIState.Error).throwable.message == "Error throw in test")
    }

    @Test
    fun `when find word occurrence use case get result success then notify its state`() = runTest {
        mockLog()
        val flowResult = flow<Collection<WordOccurrenceDomain>> {
            val list = listOf(
                WordOccurrenceDomain("<p>", 3),
                WordOccurrenceDomain("Hola", 1),
                WordOccurrenceDomain("Mundo", 1),
                WordOccurrenceDomain("</p>", 1)
            )
            emit(list)
        }.asResult()
        coEvery { findOccurrencesMock.invoke() } returns flowResult

        sut.startProcessingData()

        val collectUiState = sut.wordOccurrences.value
        assertTrue(collectUiState is UIState.Success)
        assertTrue((collectUiState as UIState.Success).data.size == 4)
    }

    @Test
    fun `when find word occurrence use case result failure then notify its state`() = runTest {
        mockLog()
        val flowResult = flow<Collection<WordOccurrenceDomain>> {
            throw IllegalStateException()
        }.asResult()
        coEvery { findOccurrencesMock.invoke() } returns flowResult

        sut.startProcessingData()

        val collectUiState = sut.wordOccurrences.value
        assertTrue(collectUiState is UIState.Error)
        assertTrue((collectUiState as UIState.Error).throwable is IllegalStateException)
    }

    private fun mockLog() {
        mockkStatic(Log::class)
        every { Log.d(any<String>(), any<String>()) } returns 0
    }
}
