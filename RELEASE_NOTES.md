# 2.0.0 release notes

## Compatibility

- Requires Java 17 or later and targets Spring Boot 4.0 and 4.1.
- Uses Prometheus Java client 1.x. Applications on Spring Boot 2.7 and the legacy simpleclient should use the 1.x line.

## Changes

- Registers NMT gauges with the application's Prometheus registry and cleans up the collector when the application context closes.
- Fixes parsing of large memory values, class counts, allocation entries, and modern HotSpot output containing peak values.
- Preserves fractional values in the `_mb` and `_gb` gauges.
- Adds integration tests for Spring Boot auto-configuration, Prometheus scrapes, and real JVM NMT output.

## Migration

Update dashboards and alerts to use `nmt_class_classes_count` and `nmt_thread_threads_count` instead of the corresponding `_total` names. These are gauges. Memory metrics ending in `_mb` or `_gb` may now have fractional values.
