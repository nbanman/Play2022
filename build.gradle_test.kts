import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.7.20"
    id("org.jetbrains.kotlin.plugin.allopen") version "1.8.0-Beta"
    id("org.jetbrains.kotlinx.benchmark") version "0.4.6"
}

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
    testImplementation 'org.junit.jupiter:junit-jupiter-api:5.2.0'
    implementation("org.jetbrains.kotlinx:kotlinx-collections-immutable:0.3.5")
    implementation(kotlin("script-runtime"))
    implementation("org.jetbrains.kotlinx:kotlinx-benchmark-runtime:0.4.6")
}

tasks.test {
    useJUnitPlatform()
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}

sourceSets.all {
    java.setSrcDirs(listOf("$name/src"))
    resources.setSrcDirs(listOf("$name/resources"))
}

allOpen {
    annotation("org.openjdk.jmh.annotations.State")
}

benchmark {
    configurations {
        named("main") {
            iterationTime = 5
            iterationTimeUnit = "sec"

        }
    }
    targets {
        register("main") {
            this as kotlinx.benchmark.gradle.JvmBenchmarkTarget
            jmhVersion = "1.21"
        }
    }
}