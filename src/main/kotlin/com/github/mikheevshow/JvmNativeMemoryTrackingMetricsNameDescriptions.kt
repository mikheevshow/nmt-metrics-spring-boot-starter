package com.github.mikheevshow

import java.util.*

/**
 * Loads and provides Native Memory Tracking metrics description
 */
class JvmNativeMemoryTrackingMetricsNameDescriptions: Properties() {

    init {
        try {
            val file = javaClass.getResourceAsStream("/metrics.properties")
            load(file)
        } catch (ex: Exception) {
            throw JvmNativeMemoryTrackingMetricsException("Error when read metrics descriptions", ex)
        }
    }
}