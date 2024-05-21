package com.gabcode.compassabout.data

import android.util.Log
import com.gabcode.compassabout.data.database.AboutDao
import com.gabcode.compassabout.util.ConnectivityChecker
import com.gabcode.compassabout.util.NotFoundConnectivityException
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.unmockkAll
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test

class AboutRepositoryTest {

    @MockK
    private lateinit var serviceMock: IAboutService

    @MockK
    private lateinit var connectivityCheckerMock: ConnectivityChecker

    @MockK
    private lateinit var daoMock: AboutDao

    @MockK
    private lateinit var aboutResponseMock: AboutResponse

    private val testDispatcher = StandardTestDispatcher(name = "IO dispatcher")

    private lateinit var sut: AboutRepository

    @Before
    fun setUp() {
        MockKAnnotations.init(this, relaxed = true)
        sut = AboutRepository(serviceMock, connectivityCheckerMock, daoMock, testDispatcher)
    }

    @After
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun `when data is cached then return success`() = runTest(testDispatcher) {
        coEvery { daoMock.getData() } returns mockk(relaxed = true)

        val result = sut.fetchContent()

        assert(result.isSuccess)
        coVerify(exactly = 0) {
            connectivityCheckerMock.isNetworkAvailable()
            serviceMock.makeRequest()
        }
    }

    @Test
    fun `when connectivity is disabled then result failure with exception`() {
        runTest(testDispatcher) {
            coEvery { daoMock.getData() } returns null
            every { connectivityCheckerMock.isNetworkAvailable() } returns false

            val result = sut.fetchContent()

            assert(result.isFailure)
            assert(result.exceptionOrNull() is NotFoundConnectivityException)
        }
    }

    @Test
    fun `when fetch remote data successfully then return success result and insert data`() {
        runTest(testDispatcher) {
            coEvery { daoMock.getData() } returns null
            every { connectivityCheckerMock.isNetworkAvailable() } returns true
            coEvery { serviceMock.makeRequest() } returns aboutResponseMock
            mockkStatic(Log::class)
            every { Log.i(any<String>(), any<String>()) } returns 0

            val result = sut.fetchContent()

            assert(result.isSuccess)
            coVerify { daoMock.insert(any()) }
        }
    }
}
