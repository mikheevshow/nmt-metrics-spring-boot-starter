# NMT Metrics Spring Boot Starter

The Native Memory Tracking (NMT) is a Java HotSpot VM feature that tracks internal memory usage for a HotSpot JVM. 
You can access NMT data using `jcmd` utility.

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