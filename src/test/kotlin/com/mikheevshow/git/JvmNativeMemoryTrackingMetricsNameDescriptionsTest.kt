package com.mikheevshow.git

import com.github.mikheevshow.JvmNativeMemoryTrackingMetricsNameDescriptions
import io.kotest.matchers.collections.shouldContainAll
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.junit5.MockKExtension
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(MockKExtension::class)
class JvmNativeMemoryTrackingMetricsNameDescriptionsTest {

    @InjectMockKs
    lateinit var jvmNativeMemoryTrackingMetricsNameDescriptions: JvmNativeMemoryTrackingMetricsNameDescriptions

    @Test
    fun `Should read metrics descriptions correct`() {
        val entries = jvmNativeMemoryTrackingMetricsNameDescriptions.keys.toSet()
        entries shouldContainAll setOf(
            "nmt_total_reserved",
            "nmt_total_committed",
            "nmt_total_reserved_kb",
            "nmt_total_committed_kb",
            "nmt_total_reserved_mb",
            "nmt_total_committed_mb",
            "nmt_total_reserved_gb",
            "nmt_total_committed_gb"
        )
    }
}