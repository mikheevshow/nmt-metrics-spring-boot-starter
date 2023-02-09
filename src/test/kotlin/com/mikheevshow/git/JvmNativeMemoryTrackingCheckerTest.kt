/*

MIT License

Copyright (c) 2023 Ilya Mikheev

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.

 */

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