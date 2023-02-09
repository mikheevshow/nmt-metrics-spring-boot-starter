package com.mikheevshow.git

import com.github.mikheevshow.CommandLineExecutor
import com.github.mikheevshow.JvmNativeMemoryTrackingMetricsException
import com.github.mikheevshow.JvmNativeMemoryTrackingModeChecker
import com.github.mikheevshow.getNmtSummary
import io.kotest.assertions.throwables.shouldNotThrow
import io.kotest.assertions.throwables.shouldThrowExactly
import io.kotest.matchers.comparables.shouldBeEqualComparingTo
import io.kotest.matchers.shouldBe
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
        shouldThrowExactly<JvmNativeMemoryTrackingMetricsException> {
            jvmNativeMemoryTrackingModeChecker.checkNmtEnabled()
        }.message shouldBe "Native memory tracking (NMT) is not enabled, please add `-XX:NativeMemoryTracking=detail`" +
                " to JVM startup options or set property `management.metrics.nmt.enabled=false`."
    }

    @Test
    fun `Should not throw an exception because all is good`() {
        every { commandLineExecutor.getNmtSummary() } returns "abra-ca-da-bra"
        shouldNotThrow<JvmNativeMemoryTrackingMetricsException> {
            jvmNativeMemoryTrackingModeChecker.checkNmtEnabled()
        }
    }
}