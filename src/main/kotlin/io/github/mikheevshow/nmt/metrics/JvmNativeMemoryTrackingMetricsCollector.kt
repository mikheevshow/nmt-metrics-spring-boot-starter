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

import io.prometheus.metrics.model.registry.MultiCollector
import io.prometheus.metrics.model.snapshots.GaugeSnapshot
import io.prometheus.metrics.model.snapshots.MetricSnapshots

open class JvmNativeMemoryTrackingMetricsCollector(
    private val nameDescriptions: JvmNativeMemoryTrackingMetricsNameDescriptions,
    private val parser: JvmNativeMemoryTrackingParser,
    private val commandLineExecutor: CommandLineExecutor,
) : MultiCollector {
    private val log = logger()

    override fun collect(): MetricSnapshots {
        return try {
            val metrics = parser.parse(commandLineExecutor.getNmtSummary()).mapNotNull { (key, value) ->
                val metricName = "${NATIVE_MEMORY_TRACKING_PREFIX}_${key.replace(".", "_").replace("-", "_")}"
                val number = value.toString().toDoubleOrNull()
                if (number == null) {
                    null
                } else {
                    GaugeSnapshot.builder()
                        .name(metricName)
                        .help(nameDescriptions.getOrEmpty(metricName))
                        .dataPoint(GaugeSnapshot.GaugeDataPointSnapshot.builder().value(number).build())
                        .build()
                }
            }
            MetricSnapshots(metrics)
        } catch (ex: Exception) {
            log.error("Error when collect NMT metrics", ex)
            MetricSnapshots(emptyList())
        }
    }

    companion object {
        const val NATIVE_MEMORY_TRACKING_PREFIX = "nmt"
    }
}
