package com.flowpulse.app.domain.usecase

import android.net.Uri
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.robolectric.junit.jupiter.RobolectricExtension

@ExtendWith(RobolectricExtension::class)
class ProcessDeepLinkUseCaseTest {

    private val useCase = ProcessDeepLinkUseCase()

    @Test
    fun `returns null when missing instance`() {
        val result = useCase(Uri.parse("flowpulse://execution?executionId=1"))
        assertNull(result)
    }

    @Test
    fun `parses execution deep link`() {
        val uri = Uri.parse("flowpulse://execution?instanceId=5&executionId=10")
        val result = useCase(uri)
        requireNotNull(result)
        assertEquals(5, result.instanceId)
        assertEquals(10, result.executionId)
        assertEquals("executions/5", result.destination)
    }
}
