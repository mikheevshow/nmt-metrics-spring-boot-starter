package com.github.mikheevshow

import io.prometheus.client.Collector
import io.prometheus.client.GaugeMetricFamily

class JvmNativeMemoryTrackingMetricsCollector(
    private val nmtProvider: JvmNativeMemoryTrackingProvider,
    private val parser: JvmNativeMemoryTrackingParser,
    private val nameDescriptions: JvmNativeMemoryTrackingMetricsNameDescriptions
) : Collector() {

    private val log = logger()

    override fun collect(): MutableList<MetricFamilySamples> {
        try {

            val nmt = nmtProvider.getNmtDescription()
            val metricsMap = parser.parse(nmt)

            return metricsMap
                .asSequence()
                .map {
                    try {
                        GaugeMetricFamily(
                            "${NATIVE_MEMORY_TRACKING_PREFIX}_${it.key.replace(".", "_").replace("-", "_")}",
                            nameDescriptions.getDescription(it.key) ?: "",
                            it.value.toString().toDouble()
                        )
                    } catch (ex: Exception) {
                        log.trace(ex) { "Can't convert the metric with key ${it.key} to double, because value is ${it.value}" }
                        null
                    }
                }
                .filterNotNull()
                .toMutableList()
        } catch (ex: Exception) {
            log.error("Error when collect nmt metrics", ex)
            return mutableListOf()
        }
    }

    companion object {
        const val NATIVE_MEMORY_TRACKING_PREFIX = "nmt"
    }
}