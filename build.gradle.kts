plugins {
    kotlin("jvm") version "2.1.20"
}

group = "io.github.nicokun1316"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("dev.kord:kord-core:0.15.0")
    implementation("io.github.cdimascio:dotenv-kotlin:6.5.1")
    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}