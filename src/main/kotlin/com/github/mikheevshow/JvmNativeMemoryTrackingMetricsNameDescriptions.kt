package com.github.mikheevshow

class JvmNativeMemoryTrackingMetricsNameDescriptions {

    private val descriptionMap = mapOf(
        "" to ""
    )

    fun getDescription(forMetric: String): String? {
        return descriptionMap[forMetric]
    }
}