package com.flowpulse.app.domain

import com.flowpulse.app.domain.model.AuthType
import com.flowpulse.app.domain.model.N8nInstance
import com.flowpulse.app.domain.repo.FlowPulseRepository
import com.flowpulse.app.domain.usecase.AddOrUpdateInstanceUseCase
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

@OptIn(ExperimentalCoroutinesApi::class)
class AddOrUpdateInstanceUseCaseTest {

    private val repository: FlowPulseRepository = mockk(relaxed = true)
    private val useCase = AddOrUpdateInstanceUseCase(repository)

    @Test
    fun `invoke validates and stores instance`() = runTest {
        val instance = N8nInstance(
            id = 0,
            name = "Demo",
            baseUrl = "https://example.com/",
            authType = AuthType.BEARER,
            apiKey = "token"
        )
        coEvery { repository.validateInstance(instance) } returns Result.success(Unit)

        val result = useCase(instance)

        assertTrue(result.isSuccess)
        coVerify { repository.addOrUpdateInstance(instance) }
    }
}
