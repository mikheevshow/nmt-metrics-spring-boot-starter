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

package io.github.mikheevshow.nmt.metrics

import io.prometheus.client.CollectorRegistry
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
@ConditionalOnProperty(
    prefix = "management.metrics",
    name = ["nmt.enabled"],
    havingValue = "true"
)
open class JvmNativeMemoryTrackingMetricsAutoConfiguration {

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
