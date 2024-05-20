package com.gabcode.compassabout.domain

import com.gabcode.compassabout.data.AboutResponse
import com.gabcode.compassabout.data.IAboutRepository
import com.gabcode.compassabout.util.NotFoundConnectivityException
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class FindCharSequenceOccurrencesUseCaseTest {

    @MockK
    private lateinit var repositoryMock: IAboutRepository

    private val testDispatcher = StandardTestDispatcher(name = "DEFAULT dispatcher")

    private lateinit var sut: FindCharSequenceOccurrencesUseCase

    @Before
    fun setUp() {
        MockKAnnotations.init(this, relaxed = true)

        sut = FindCharSequenceOccurrencesUseCase(repositoryMock, testDispatcher)
    }

    @Test
    fun `when repository return failure then emit it`() = runTest(testDispatcher) {
        coEvery {
            repositoryMock.fetchContent()
        } returns Result.failure(NotFoundConnectivityException())

        val result = sut.invoke()

        result.collect {
            assertTrue(it.isFailure)
            assertTrue(it.exceptionOrNull() is NotFoundConnectivityException)
        }
    }

    @Test
    fun `when repository return success then data is not null or empty`()  = runTest(testDispatcher) {
        runTest(testDispatcher) {
            coEvery { repositoryMock.fetchContent() } returns Result.success(
                AboutResponse(CONTENT_FAKE))

            val result = sut.invoke()

            result.collect {
                assertTrue(it.isSuccess)
                val data = it.getOrNull()
                assertTrue(data.isNullOrEmpty().not())
            }
        }
    }

    @Test
    fun `when repository return success then check occurrences`()  = runTest(testDispatcher) {
        runTest(testDispatcher) {
            coEvery { repositoryMock.fetchContent() } returns Result.success(
                AboutResponse(CONTENT_FAKE))

            val resultExpected = arrayListOf(
                WordOccurrenceDomain("<!DOCTYPE", 1),
                WordOccurrenceDomain("html>", 1),
                WordOccurrenceDomain("<html>", 1),
                WordOccurrenceDomain("<body>", 1),
                WordOccurrenceDomain("<h1>", 1),
                WordOccurrenceDomain("Hello,", 1),
                WordOccurrenceDomain("Document", 3),
                WordOccurrenceDomain("</h1>", 1),
                WordOccurrenceDomain("<p>", 3),
                WordOccurrenceDomain("This", 3),
                WordOccurrenceDomain("is", 3),
                WordOccurrenceDomain("a", 2),
                WordOccurrenceDomain("sample", 3),
                WordOccurrenceDomain("HTML", 3),
                WordOccurrenceDomain("</p>", 3),
                WordOccurrenceDomain("another,", 1),
                WordOccurrenceDomain("</body>", 1),
                WordOccurrenceDomain("</html>", 1)
            )

            val result = sut.invoke()

            result.collect {
                val data = it.getOrNull()!!.toList()
                for (i in resultExpected.indices) {
                    assertEquals(resultExpected[i].word, data[i].word)
                    assertEquals(resultExpected[i].count, data[i].count)
                }
            }
        }
    }
}
