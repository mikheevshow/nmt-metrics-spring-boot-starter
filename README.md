# NMT Metrics Spring Boot Starter

[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)

The Native Memory Tracking (NMT) is a Java HotSpot VM feature that tracks internal memory usage for a HotSpot JVM. 
You can access NMT data using `jcmd` utility.

## Table of Content
- [NMT Metrics Motivation](#1-nmt-metrics-motivation)
- [Quick Start](#2-quick-start)
  - [Exposing Prometheus endpoint](#21-exposing-prometheus-endpoint)
  - [Enable NMT](#22-enable-nmt)
- [Grafana Settings Suggestions](#3-grafana-settings-suggestions)
- [License](#4-license)

### 1. NMT Metrics Motivation

One common case of NMT using is detecting memory leaks.



### 2. Quick Start

#### 2.1. Exposing Prometheus endpoint

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

#### 2.2. Enable NMT
Enable NMT by adding flag in the list of JVM options. Keep in mind that enabling this will cause 5-10% performance overhead.

```
-XX:NativeMemoryTracking=summary
```

### 3. Grafana Settings Suggestions

To investigate memory leaks it's useful to work with `*_kb` metrics. This metrics are exporting from NMT in 
kilobytes. Use the same unit when create Grafana Dashboard.

<img width="519" alt="grafana-units" src="https://user-images.githubusercontent.com/10999015/214489872-3d3e88d8-2fda-4786-8d6f-fd036a5bdcb2.png">

Using `Time Series` plots with starter's gauges will make diagnostic more visual

<img width="1141" alt="time-series-plot" src="https://user-images.githubusercontent.com/10999015/214489960-f8e62c96-0872-40df-9736-67661d3ccf48.png">

### 4. License

```
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
```
