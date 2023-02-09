package com.mikheevshow.git

import com.github.mikheevshow.DefaultCommandLineExecutor
import com.github.mikheevshow.getNmtSummary
import io.kotest.matchers.shouldBe
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.junit5.MockKExtension
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(MockKExtension::class)
class CommandLineExecutorTest {

    @InjectMockKs
    lateinit var defaultCommandLineExecutor: DefaultCommandLineExecutor

    @Test
    fun `Should return nmt is not enabled from console`() {
        val commandLineResult = defaultCommandLineExecutor.getNmtSummary()
        commandLineResult shouldBe "Native memory tracking is not enabled\n"
    }
}