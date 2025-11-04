plugins {
    kotlin("jvm") version "2.1.20"
    id("io.gitlab.arturbosch.detekt") version "1.23.8"
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.9.0")
}

detekt {
    config.setFrom(files("$projectDir/detekt.yml"))
    buildUponDefaultConfig = true
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(21)
}
