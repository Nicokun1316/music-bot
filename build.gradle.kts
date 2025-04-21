plugins {
    kotlin("jvm") version "2.1.20"
}

group = "io.github.nicokun1316"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    maven(url = "https://jitpack.io")
    maven(url = "https://maven.lavalink.dev/releases")
}

val kordVersion = "0.15.0"

dependencies {
    implementation("dev.kord:kord-core:$kordVersion")
    implementation("dev.kord:kord-core-voice:$kordVersion")
    implementation("dev.kord:kord-voice:$kordVersion")
    implementation("io.github.cdimascio:dotenv-kotlin:6.5.1")
    implementation("dev.arbjerg:lavaplayer:2.2.3")
    implementation("dev.lavalink.youtube:v2:1.12.0")
    implementation("io.github.oshai:kotlin-logging-jvm:7.0.3")
    implementation(group = "ch.qos.logback", name = "logback-classic", version = "1.5.13")
    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}

kotlin {
    compilerOptions {
        freeCompilerArgs = listOf("-Xcontext-parameters")
    }
}