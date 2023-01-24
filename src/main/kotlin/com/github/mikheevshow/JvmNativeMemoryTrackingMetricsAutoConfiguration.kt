package com.github.mikheevshow

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
@ConditionalOnProperty(
    prefix = "spring.management.metrics.native-memory.enabled",
    havingValue = "true",
    matchIfMissing = true
)
class JvmNativeMemoryTrackingMetricsAutoConfiguration {

    @Bean
    fun jvmNativeMemoryTrackingParser(): JvmNativeMemoryTrackingParser {
        return JvmNativeMemoryTrackingParser()
    }

    @Bean
    fun jvmNativeMemoryTrackingProvider(): JvmNativeMemoryTrackingProvider {
        return JvmNativeMemoryTrackingProvider()
    }

    @Bean
    fun jvmNativeMemoryTrackingMetricsNameDescriptions(): JvmNativeMemoryTrackingMetricsNameDescriptions {
        return JvmNativeMemoryTrackingMetricsNameDescriptions()
    }

    @Bean
    fun jvmNativeMemoryTrackingMetricsCollector(
        jvmNativeMemoryTrackingProvider: JvmNativeMemoryTrackingProvider,
        jvmNativeMemoryTrackingParser: JvmNativeMemoryTrackingParser,
        jvmNativeMemoryTrackingMetricsNameDescriptions: JvmNativeMemoryTrackingMetricsNameDescriptions
    ): JvmNativeMemoryTrackingMetricsCollector {
        return JvmNativeMemoryTrackingMetricsCollector(
            nmtProvider = jvmNativeMemoryTrackingProvider,
            parser = jvmNativeMemoryTrackingParser,
            nameDescriptions = jvmNativeMemoryTrackingMetricsNameDescriptions
        )
    }
}