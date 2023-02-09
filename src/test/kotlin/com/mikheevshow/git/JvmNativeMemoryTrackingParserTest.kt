package com.mikheevshow.git

import com.github.mikheevshow.JvmNativeMemoryTrackingParser
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.junit5.MockKExtension
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(MockKExtension::class)
class JvmNativeMemoryTrackingParserTest {

    @InjectMockKs
    lateinit var jvmNativeMemoryTrackingParser: JvmNativeMemoryTrackingParser

}