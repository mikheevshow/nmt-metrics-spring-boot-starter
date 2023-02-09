import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.7.10"
    id("org.jetbrains.kotlin.plugin.spring") version "1.7.10"
    id("org.springframework.boot") version "2.7.8"
    id("io.spring.dependency-management") version "1.1.0"
    `maven-publish`
//    signing
}

group = "com.github.mikheevshow"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter")
    api("io.micrometer:micrometer-registry-prometheus:1.10.3")

    testImplementation(kotlin("test"))

    testImplementation("org.junit.jupiter:junit-jupiter-api:5.9.2")
    testApi("org.junit.jupiter:junit-jupiter-engine:5.9.2")
    testImplementation("io.mockk:mockk:1.13.4")
    testImplementation("io.kotest:kotest-runner-junit5:5.5.5")
    testImplementation("io.kotest:kotest-assertions-core:5.5.5")
}

tasks.test {
    useJUnitPlatform()
    jvmArgs = jvmArgs!! + listOf(
        "-XX:+HeapDumpOnOutOfMemoryError"
    )
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}

java {
    withJavadocJar()
    withSourcesJar()
}