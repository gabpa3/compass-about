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
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class FindEveryTenthCharacterUseCaseTest {

    @MockK
    private lateinit var repositoryMock: IAboutRepository

    private val testDispatcher = StandardTestDispatcher(name = "DEFAULT dispatcher")

    private lateinit var sut: FindEveryTenthCharacterUseCase

    @Before
    fun setUp() {
        MockKAnnotations.init(this, relaxed = true)

        sut = FindEveryTenthCharacterUseCase(repositoryMock, testDispatcher)
    }

    @Test
    fun `when repository return failure then emit it`() = runTest(testDispatcher) {
        coEvery { repositoryMock.fetchContent() } returns Result.failure(NotFoundConnectivityException())

        val result = sut.invoke()

        result.collect {
            assertTrue(it.isFailure)
            assertTrue(it.exceptionOrNull() is NotFoundConnectivityException)
        }
    }

    @Test
    fun `when repository return success then get characters correctly`() = runTest(testDispatcher) {
        runTest(testDispatcher) {
            coEvery {
                repositoryMock.fetchContent()
            } returns Result.success(AboutResponse("Lorem Ipsum is simply dummy text"))
            val listExpected = arrayListOf(
                TenthCharacterDomain('u', 10),
                TenthCharacterDomain('l', 20),
                TenthCharacterDomain('e', 30))

            val result = sut.invoke()

            result.collect {
                assertTrue(it.isSuccess)
                val data = it.getOrNull()
                assertNotNull(data)
                assertEquals(listExpected, data!!.toList())
            }
        }
    }
}
