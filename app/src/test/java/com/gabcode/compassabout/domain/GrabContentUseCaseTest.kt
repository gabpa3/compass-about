package com.gabcode.compassabout.domain

import com.gabcode.compassabout.data.IAboutRepository
import io.mockk.coJustRun
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Test

class GrabContentUseCaseTest {

    @Test
    fun `when invoke usecase then call repository`() = runTest {
        val repositoryMock = mockk<IAboutRepository>()
        coJustRun { repositoryMock.fetchContent() }

        val sut = GrabContentUseCase(repositoryMock)
        sut.invoke()

        coVerify { repositoryMock.fetchContent() }
    }
}
