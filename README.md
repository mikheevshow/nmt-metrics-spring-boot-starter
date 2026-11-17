# NMT Metrics Spring Boot Starter

![Robot diagnosing JVM memory with a cup of Java coffee](docs/images/nmt-readme-banner.png)

[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)
[![Build Status](https://github.com/mikheevshow/nmt-metrics-spring-boot-starter/actions/workflows/build.yml/badge.svg?style=flat)](https://github.com/mikheevshow/nmt-metrics-spring-boot-starter/actions/workflows/build.yml)
[![Maven Central](https://img.shields.io/maven-central/v/io.github.mikheevshow/nmt-metrics-spring-boot-starter)](https://central.sonatype.com/artifact/io.github.mikheevshow/nmt-metrics-spring-boot-starter)

## Table of Contents

- [Introduction](#introduction)
- [Quick Start](#1-quick-start)
  - [Requirements and dependencies](#11-requirements-and-dependencies)
  - [Enable NMT](#12-enable-nmt)
  - [Expose and check the Prometheus endpoint](#13-expose-and-check-the-prometheus-endpoint)
- [Migrating from 1.x](#2-migrating-from-1x)
- [Grafana](#3-grafana)
- [License](#4-license)

## Introduction

Native Memory Tracking (NMT) is a HotSpot JVM feature that tracks internal native memory usage. You can inspect a running JVM with `jcmd <pid> VM.native_memory summary`.

The starter runs the JVM diagnostic command through its platform MBean when the Prometheus registry is scraped. It registers NMT gauges directly with the Prometheus Java client used by the application's Prometheus registry.

## 1. Quick Start

### 1.1. Requirements and dependencies

The 2.x line targets Spring Boot 4.0 and 4.1, Java 17 or later, and Prometheus Java client 1.x. The starter includes Actuator and the Prometheus registry. Add a web starter to expose an HTTP endpoint.

**Release status:** Version 2.0.0 is being prepared and is not yet published to Maven Central. Until it is released, the examples below require the artifact to be installed locally or available in another repository configured for your build.

Gradle (Kotlin DSL):

```kotlin
// Include mavenLocal() in repositories if you installed the release locally.
implementation("org.springframework.boot:spring-boot-starter-webmvc")
implementation("io.github.mikheevshow:nmt-metrics-spring-boot-starter:2.0.0")
```

Maven:

```xml
<dependencies>
  <dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-webmvc</artifactId>
  </dependency>
  <dependency>
    <groupId>io.github.mikheevshow</groupId>
    <artifactId>nmt-metrics-spring-boot-starter</artifactId>
    <version>2.0.0</version>
  </dependency>
</dependencies>
```

These examples assume the application manages Spring Boot dependency versions, for example with the Spring Boot Gradle plugin or Maven parent.

### 1.2. Enable NMT

Start the JVM with NMT enabled. The `summary` mode is sufficient:

```text
-XX:NativeMemoryTracking=summary
```

NMT has a runtime cost that depends on the application and JVM. Measure it in your environment. When NMT metrics are enabled but JVM NMT is disabled, the starter fails application startup with an explanatory error.

### 1.3. Expose and check the Prometheus endpoint

Add the following to `application.yml`:

```yaml
management:
  metrics:
    nmt:
      enabled: true
  endpoints:
    web:
      exposure:
        include: "health,prometheus"
```

Prometheus export must be enabled and its registry available; if the registry is unavailable or export is disabled, the NMT collector is not configured. With the application running on port 8080, check the output:

```bash
curl -fsS http://localhost:8080/actuator/prometheus | grep '^nmt_'
```

For example, the output can include `nmt_total_reserved_kb`, `nmt_total_committed_kb`, `nmt_class_classes_count`, and `nmt_thread_threads_count`. Values depend on the JVM and its NMT output. NMT is collected at scrape time, so a scrape invokes a JVM diagnostic command.

## 2. Migrating from 1.x

- Upgrade the application to Java 17 or later and Spring Boot 4.0 or 4.1. The 1.x line targets Spring Boot 2.7 and the legacy Prometheus simpleclient.
- Use the 2.x starter and the Prometheus Java client 1.x registry supplied by its dependencies.
- Update dashboards and alerts: `nmt_class_classes_total` is now `nmt_class_classes_count`, and `nmt_thread_threads_total` is now `nmt_thread_threads_count`. These values are gauges.
- Memory values in `_mb` and `_gb` metrics can now contain fractions rather than being truncated to whole numbers.

## 3. Grafana

Use the `*_kb` metrics to inspect NMT memory usage in kilobytes. Time series visualizations can help reveal changes over time.

<img width="519" alt="grafana-units" src="https://user-images.githubusercontent.com/10999015/214489872-3d3e88d8-2fda-4786-8d6f-fd036a5bdcb2.png">

<img width="1141" alt="time-series-plot" src="https://user-images.githubusercontent.com/10999015/214489960-f8e62c96-0872-40df-9736-67661d3ccf48.png">

## 4. License

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
