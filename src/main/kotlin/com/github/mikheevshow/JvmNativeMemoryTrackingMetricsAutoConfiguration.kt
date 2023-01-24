package com.github.mikheevshow

import io.prometheus.client.CollectorRegistry
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
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
    @ConditionalOnMissingBean
    fun jvmNativeMemoryTrackingParser(): JvmNativeMemoryTrackingParser {
        return JvmNativeMemoryTrackingParser()
    }

    @Bean
    @ConditionalOnMissingBean
    fun jvmNativeMemoryTrackingProvider(): JvmNativeMemoryTrackingProvider {
        return JvmNativeMemoryTrackingProvider()
    }

    @Bean
    @ConditionalOnMissingBean
    fun jvmNativeMemoryTrackingMetricsNameDescriptions(): JvmNativeMemoryTrackingMetricsNameDescriptions {
        return JvmNativeMemoryTrackingMetricsNameDescriptions()
    }

    @Bean
    @ConditionalOnMissingBean
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

    @Bean
    fun jvmNativeMemoryCollectorRegistrar(
        collectorRegistry: CollectorRegistry,
        jvmNativeMemoryTrackingMetricsCollector: JvmNativeMemoryTrackingMetricsCollector
    ): NmtMetricsRegistrar {
        return NmtMetricsRegistrar(
            collectorRegistry = collectorRegistry,
            jvmNativeMemoryTrackingMetricsCollector = jvmNativeMemoryTrackingMetricsCollector
        )
    }
}