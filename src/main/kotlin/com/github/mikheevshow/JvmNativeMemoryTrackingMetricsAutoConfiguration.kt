package com.github.mikheevshow

import io.prometheus.client.CollectorRegistry
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
@ConditionalOnProperty(
    prefix = "management.metrics.nmt.enabled",
    havingValue = "true",
    matchIfMissing = true
)
class JvmNativeMemoryTrackingMetricsAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    fun commandLineExecutor(): CommandLineExecutor {
        return DefaultCommandLineExecutor()
    }

    @Bean
    @ConditionalOnMissingBean
    fun jvmNativeMemoryTrackingModeChecker(
        commandLineExecutor: CommandLineExecutor
    ): JvmNativeMemoryTrackingModeChecker {
        return JvmNativeMemoryTrackingModeChecker(commandLineExecutor)
    }

    @Bean
    @ConditionalOnMissingBean
    fun jvmNativeMemoryTrackingParser(): JvmNativeMemoryTrackingParser {
        return JvmNativeMemoryTrackingParser()
    }

    @Bean
    @ConditionalOnMissingBean
    fun jvmNativeMemoryTrackingMetricsNameDescriptions(): JvmNativeMemoryTrackingMetricsNameDescriptions {
        return JvmNativeMemoryTrackingMetricsNameDescriptions()
    }

    @Bean
    @ConditionalOnMissingBean
    fun jvmNativeMemoryTrackingMetricsCollector(
        commandLineExecutor: CommandLineExecutor,
        jvmNativeMemoryTrackingParser: JvmNativeMemoryTrackingParser,
        jvmNativeMemoryTrackingMetricsNameDescriptions: JvmNativeMemoryTrackingMetricsNameDescriptions
    ): JvmNativeMemoryTrackingMetricsCollector {
        return JvmNativeMemoryTrackingMetricsCollector(
            parser = jvmNativeMemoryTrackingParser,
            nameDescriptions = jvmNativeMemoryTrackingMetricsNameDescriptions,
            commandLineExecutor = commandLineExecutor
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