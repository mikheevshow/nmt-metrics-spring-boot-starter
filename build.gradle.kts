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

import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import java.lang.System.getenv
import java.util.Properties

plugins {
    kotlin("jvm") version "1.7.10"
    id("org.jetbrains.kotlin.plugin.spring") version "1.7.10"
    id("io.spring.dependency-management") version "1.1.0"
    id("io.github.gradle-nexus.publish-plugin") version "1.1.0"
    `maven-publish`
    signing
}


nexusPublishing {
    repositories {
        sonatype {
            stagingProfileId.set(getExtraString("stagingProfileId"))
            username.set(getExtraString("ossrhUsername"))
            password.set(getExtraString("ossrhPassword"))
            nexusUrl.set(uri("https://s01.oss.sonatype.org/service/local/"))
            snapshotRepositoryUrl.set(uri("https://s01.oss.sonatype.org/content/repositories/snapshots/"))
        }
    }
}

group = "io.github.mikheevshow"
version = "1.0.0"

val isSnapshot by lazy { version.toString().endsWith("SNAPSHOT") }

ext["signing.keyId"] = getEnv("SIGNING_KEY_ID")
ext["signing.password"] = getEnv("SIGNING_PASSWORD")
ext["signing.key"] = getEnv("SIGNING_SECRET_KEY_RING_FILE")
ext["stagingProfileId"] = getEnv("SONATYPE_STAGING_PROFILE_ID")
ext["ossrhUsername"] = getEnv("OSSRH_USERNAME")
ext["ossrhPassword"] = getEnv("OSSRH_PASSWORD")

task<Jar>("javadocJar") {
    archiveClassifier.set("javadoc")
}

fun getEnv(name: String) = getenv(name)
fun getExtraString(name: String) = try {
    ext[name]?.toString() ?: ""
} catch (ex: Exception) {
    ""
}

publishing {
    repositories {
        maven {
            name = "sonatype"

            url = if (isSnapshot) {
                uri("https://s01.oss.sonatype.org/content/repositories/snapshots/")
            } else {
                uri("https://s01.oss.sonatype.org/service/local/staging/deploy/maven2/")
            }

            credentials {
                username = getExtraString("ossrhUsername")
                password = getExtraString("ossrhPassword")
            }
        }
    }

    publications.withType<MavenPublication> {
        from(components["java"])

        artifact(tasks.named("javadocJar"))

        pom {
            name.set("JVM Native Memory Tracking Metrics Spring Boot Starter")
            description.set("Spring Boot Starter for NMT metrics collecting")
            url.set("https://github.com/mikheevshow/nmt-metrics-spring-boot-starter")

            licenses {
                license {
                    name.set("MIT")
                    url.set("https://opensource.org/licenses/MIT")
                    distribution.set("repo")
                }
            }

            developers {
                developer {
                    id.set("mikheevshow")
                    name.set("Ilya Mikheev")
                    email.set("mikheev.show@gmail.com")
                }
            }

            scm {
                url.set("https://github.com/mikheevshow/nmt-metrics-spring-boot-starter")
            }
        }
    }
}

signing {
    useInMemoryPgpKeys(
        getExtraString("signing.keyId"),
        getExtraString("signing.key"),
        getExtraString("signing.password"),
    )
    sign(publishing.publications)
}

dependencyManagement {
    imports {
        mavenBom("org.springframework.boot:spring-boot-dependencies:2.7.8")
    }
}

repositories {
    mavenCentral()
    gradlePluginPortal()
}

dependencies {

    compileOnly("org.springframework.boot:spring-boot")
    compileOnly("org.springframework.boot:spring-boot-autoconfigure")
    compileOnly("javax.annotation:javax.annotation-api:1.3.2")
    compileOnly("org.slf4j:slf4j-api:2.0.6")
    compileOnly("io.micrometer:micrometer-registry-prometheus:1.10.3")

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
    testLogging {
        events("PASSED", "FAILED", "SKIPPED")
    }
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}

tasks.withType<JavaCompile> {
    sourceCompatibility = "1.8"
    targetCompatibility = "1.8"
}

java {
    withJavadocJar()
    withSourcesJar()
}