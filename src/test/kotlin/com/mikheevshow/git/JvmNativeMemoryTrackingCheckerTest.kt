package com.mikheevshow.git

import com.github.mikheevshow.CommandLineExecutor
import com.github.mikheevshow.JvmNativeMemoryTrackingModeChecker
import com.github.mikheevshow.getNmtSummary
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(MockKExtension::class)
class JvmNativeMemoryTrackingCheckerTest {

    @MockK
    lateinit var commandLineExecutor: CommandLineExecutor

    @InjectMockKs
    lateinit var jvmNativeMemoryTrackingModeChecker: JvmNativeMemoryTrackingModeChecker

    @Test
    fun `Should throw an exception because nmt is not enabled`() {
        every { commandLineExecutor.getNmtSummary() } returns "Native memory tracking is not enabled."
    }
}