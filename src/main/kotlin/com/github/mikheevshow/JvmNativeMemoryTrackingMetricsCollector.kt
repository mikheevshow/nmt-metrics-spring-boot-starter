package com.github.mikheevshow

import io.prometheus.client.Collector
import io.prometheus.client.GaugeMetricFamily

class JvmNativeMemoryTrackingMetricsCollector(
    private val nameDescriptions: JvmNativeMemoryTrackingMetricsNameDescriptions,
    private val parser: JvmNativeMemoryTrackingParser,
    private val commandLineExecutor: CommandLineExecutor,
) : Collector() {

    private val log = logger()

    override fun collect(): MutableList<MetricFamilySamples> {
        try {

            val nmt = commandLineExecutor.getNmtSummary()
            val metricsMap = parser.parse(nmt)

            return metricsMap
                .asSequence()
                .map {
                    try {
                        GaugeMetricFamily(
                            "${NATIVE_MEMORY_TRACKING_PREFIX}_${it.key.replace(".", "_").replace("-", "_")}",
                            nameDescriptions[it.key]?.toString() ?: "",
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
            log.error("Error when collect NMT metrics", ex)
            return mutableListOf()
        }
    }

    companion object {
        const val NATIVE_MEMORY_TRACKING_PREFIX = "nmt"
    }
}