package io.github.mikheevshow.nmt.metrics

import io.prometheus.client.CollectorRegistry
import io.micrometer.prometheus.PrometheusMeterRegistry
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.boot.test.context.runner.ApplicationContextRunner
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

class AutoConfigurationIntegrationTest {
    private val contextRunner = ApplicationContextRunner()
        .withUserConfiguration(TestApplication::class.java)

    @Test
    fun `loads automatically and publishes metrics to the actuator registry`() {
        var registry: CollectorRegistry? = null
        contextRunner.withPropertyValues("management.metrics.nmt.enabled=true").run { context ->
            assertThat(context).hasNotFailed()
            assertThat(context).hasSingleBean(JvmNativeMemoryTrackingMetricsCollector::class.java)
            registry = context.getBean(CollectorRegistry::class.java)
            assertThat(registry!!.getSampleValue("nmt_total_reserved_kb")).isEqualTo(2048.0)
            assertThat(context.getBean(PrometheusMeterRegistry::class.java).scrape())
                .contains("nmt_total_reserved_kb 2048.0")
        }
        assertThat(registry!!.getSampleValue("nmt_total_reserved_kb") as Double?).isNull()
    }

    @Test
    fun `does not configure collector when disabled`() {
        contextRunner.run { context ->
            assertThat(context).hasNotFailed()
            assertThat(context).doesNotHaveBean(JvmNativeMemoryTrackingMetricsCollector::class.java)
        }
    }

    @Test
    fun `does not configure collector when prometheus export is disabled`() {
        contextRunner.withPropertyValues(
            "management.metrics.nmt.enabled=true",
            "management.metrics.export.prometheus.enabled=false"
        ).run { context ->
            assertThat(context).hasNotFailed()
            assertThat(context).doesNotHaveBean(JvmNativeMemoryTrackingMetricsCollector::class.java)
        }
    }

    @Configuration(proxyBeanMethods = false)
    @EnableAutoConfiguration
    class TestApplication {
        @Bean
        fun commandLineExecutor(): CommandLineExecutor = object : CommandLineExecutor {
            override fun execute(command: String, vararg args: String) =
                "Native Memory Tracking:\nTotal: reserved=2048KB, committed=1024KB\n"
        }
    }
}
