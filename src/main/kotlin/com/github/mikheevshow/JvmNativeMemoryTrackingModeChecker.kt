package com.github.mikheevshow

import javax.annotation.PostConstruct

class JvmNativeMemoryTrackingModeChecker(
    private val commandLineExecutor: CommandLineExecutor
) {

    private val logger = logger()
    private val nmtNotEnabledMessage = "Native memory tracking is not enabled"

    @PostConstruct
    fun checkNmtEnabled() {
        val commandLineResult = commandLineExecutor.getNmtSummary()
        if (commandLineResult.contains(nmtNotEnabledMessage)) {
            throw JvmNativeMemoryTrackingMetricsException(
                """
                    Native memory tracking (NMT) is not enabled, 
                    please add `-XX:NativeMemoryTracking=detail` to JVM startup options or 
                    set property `management.metrics.nmt.enabled=false`.
                """.trimIndent()
            )
        }

        logger.info { "Native memory tracking metrics are enabled" }
    }
}