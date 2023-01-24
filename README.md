# JVM NMT Metrics Spring Boot Starter

Starter provides metrics of jvm native memory consumption

## Quick Start

1. Add dependency to your project

Gradle (Gradle DSL)

``` gradle
dependencies {
    implementation 'com.github.mikheevshow:jvm-nmt-metrics-spring-boot-starter:<<current_version>>'
}
```

Gradle (Koltin DSL)
``` kotlin
depencencies {
  implementation("com.github.mikheevshow:jvm-nmt-metrics-spring-boot-starter:<<current_version>>")
}
```

Maven
``` xml
<dependencies>
  <dependency>
    <groupId>com.github.mikheevshow</groupId>
    <artifactId>jvm-nmt-metrics-spring-boot-starter</artifactId>
    <version>current_version</version>
  </dependecy>
</dependencies>
```

2. Enable NMT by adding flag in the list of JVM options. Keep in mind that enabling of NMT adds 5-10% overhead to JVM 
performance.

```
-XX:NativeMemoryTracking=summary
```