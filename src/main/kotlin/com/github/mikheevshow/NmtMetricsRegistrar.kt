package com.github.mikheevshow

import io.prometheus.client.CollectorRegistry
import javax.annotation.PostConstruct

class NmtMetricsRegistrar(
    private val jvmNativeMemoryTrackingMetricsCollector: JvmNativeMemoryTrackingMetricsCollector,
    private val collectorRegistry: CollectorRegistry
) {

    @PostConstruct
    fun init() {
        collectorRegistry.register(jvmNativeMemoryTrackingMetricsCollector)
    }
}