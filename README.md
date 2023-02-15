# NMT Metrics Spring Boot Starter

[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)
[![Build Status](https://github.com/mikheevshow/nmt-metrics-spring-boot-starter/actions/workflows/build.yml/badge.svg?style=flat)]()
[![Sonatype Central](https://img.shields.io/nexus/r/io.github.mikheevshow/nmt-metrics-spring-boot-starter?color=light%20green&label=Maven%20Central&server=https%3A%2F%2Fs01.oss.sonatype.org)](https://central.sonatype.com/namespace/io.github.mikheevshow)

## Table of Content
- [Introduction](#introduction)
- [Quick Start](#1-quick-start)
  - [Add Dependencies](#11-add-dependencies)
  - [Exposing Prometheus endpoint](#12-exposing-prometheus-endpoint)
  - [Enable NMT](#13-enable-nmt)
- [Grafana Settings Suggestions](#2-grafana-settings-suggestions)
- [License](#3-license)

### Introduction

The Native Memory Tracking (NMT) is a Java HotSpot VM feature that tracks internal memory usage for a HotSpot JVM. 
You can access NMT data using `jcmd` utility.

The result of command execution in console
```bash
jcmd <pid> VM.native_memory summary
```

Will give you a result like this
```
Total:  reserved=664192KB,  committed=253120KB
 
-                 Java Heap (reserved=516096KB, committed=204800KB)
                            (mmap: reserved=516096KB, committed=204800KB)
 
-                     Class (reserved=6568KB, committed=4140KB)
                            (classes #665)
                            (malloc=424KB, #1000)
                            (mmap: reserved=6144KB, committed=3716KB)
 
-                    Thread (reserved=6868KB, committed=6868KB)
                            (thread #15)
                            (stack: reserved=6780KB, committed=6780KB)
                            (malloc=27KB, #66)
                            (arena=61KB, #30)
 
-                      Code (reserved=102414KB, committed=6314KB)
                            (malloc=2574KB, #74316)
                            (mmap: reserved=99840KB, committed=3740KB)
 
-                        GC (reserved=26154KB, committed=24938KB)
                            (malloc=486KB, #110)
                            (mmap: reserved=25668KB, committed=24452KB)
...
```
The starter executes `jcmd` internally in application every time when `/actuator/metrics` endpoint is triggered and 
produce NMT metrics using micrometer gauges.

### 1. Quick Start

#### 1.1. Add Dependencies

Make sure to include below dependencies:

For Gradle:
```kotlin
implementation("org.springframework.boot:spring-boot-starter-actuator")
implementation("io.micrometer:micrometer-registry-prometheus")
implementation("org.springframework.boot:spring-boot-starter-web")

// And nmt starter dependency
implementation("com.github.mikheevshow:jvm-nmt-metrics-spring-boot-starter:<<current_version>>")
```

For Apache Maven
```xml

<dependencies>
  
  <dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-actuator</artifactId>
  </dependency>
  
  <dependency>
    <groupId>io.micrometer</groupId>
    <artifactId>micrometer-registry-prometheus</artifactId>
  </dependency>
  
  <dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-web</artifactId>
  </dependency>
  
  <!--  And nmt starter dependency-->
  <dependency>
    <groupId>com.github.mikheevshow</groupId>
    <artifactId>jvm-nmt-metrics-spring-boot-starter</artifactId>
    <version>current_version</version>
  </dependency>
  
</dependencies>
```

#### 1.2. Exposing Prometheus endpoint

Configuration:
```yaml
management:
  metrics:
    nmt:
      enabled: true # enable NMT metrics
    export:
      prometheus:
        enabled: true
  endpoints:
    web:
      exposure:
        include: "*"
```

Standard `/actuator/metrics` and `actuator/prometheus` endpoints will render `nmt_*` metrics

#### 1.3. Add JVM Option
Enable NMT by adding flag in the list of JVM options. Keep in mind that enabling this will cause 5-10% performance overhead.

```
-XX:NativeMemoryTracking=detail
```

### 2. Grafana Settings Suggestions

To investigate memory leaks it's useful to work with `*_kb` metrics. This metrics are exporting from NMT in 
kilobytes. Use the same unit when create Grafana Dashboard.

<img width="519" alt="grafana-units" src="https://user-images.githubusercontent.com/10999015/214489872-3d3e88d8-2fda-4786-8d6f-fd036a5bdcb2.png">

Using `Time Series` plots with starter's gauges will make diagnostic more visual

<img width="1141" alt="time-series-plot" src="https://user-images.githubusercontent.com/10999015/214489960-f8e62c96-0872-40df-9736-67661d3ccf48.png">

### 3. License

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
