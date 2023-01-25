# NMT Metrics Spring Boot Starter

[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)

The Native Memory Tracking (NMT) is a Java HotSpot VM feature that tracks internal memory usage for a HotSpot JVM. 
You can access NMT data using `jcmd` utility.

## Table of Content
- [NMT Metrics Motivation](#1-nmt-metrics-motivation)
- [Quick Start](#2-quick-start)
  - [Exposing Prometheus endpoint](#21-exposing-prometheus-endpoint)
  - [Enable NMT](#22-enable-nmt)
- [Metrics List](#3-metrics-list)
- [Grafana Settings Suggestions](#4-grafana-settings-suggestions)
- [License](#5-license)

## 1. NMT Metrics Motivation

One common case of NMT using is detecting memory leaks.



## 2. Quick Start

### 2.1. Exposing Prometheus endpoint

Make sure to include below dependencies:

```kotlin
implementation("org.springframework.boot:spring-boot-starter-actuator")
implementation("io.micrometer:micrometer-registry-prometheus")
implementation("org.springframework.boot:spring-boot-starter-web")

// And nmt starter dependency
implementation("com.github.mikheevshow:jvm-nmt-metrics-spring-boot-starter:<<current_version>>")
```

Configuration:
```yaml
management:
  metrics:
    export:
      prometheus:
        enabled: true
  endpoints:
    web:
      exposure:
        include: "*"
```

Standard `/actuator/metrics` and `actuator/prometheus` endpoints will render `nmt_*` metrics

### 2.2. Enable NMT
Enable NMT by adding flag in the list of JVM options. Keep in mind that enabling of NMT adds 5-10% overhead to JVM 
performance.

```
-XX:NativeMemoryTracking=summary
```

## 3. Metrics List

## 4. Grafana Settings Suggestions

## 5. License

