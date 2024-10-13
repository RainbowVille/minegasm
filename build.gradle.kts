import net.fabricmc.loom.extension.MixinExtension

plugins {
    kotlin("jvm")
    id("gg.essential.multi-version")
    id("gg.essential.defaults.repo")
    id("gg.essential.defaults.java")
    id("gg.essential.defaults.loom")
}

val modBaseName: String by project

val mcVersion: Int by project.extra
val mcVersionStr: String by project.extra
val mcPlatform: String by project.extra

java.withSourcesJar()

repositories {
    mavenCentral()
    gradlePluginPortal()
    maven(url = "https://maven.fabricmc.net/")
    maven(url = "https://maven.minecraftforge.net")
    maven(url = "https://jitpack.io")
    maven(url = "https://maven.architectury.dev/")
    maven(url = "https://repo.essential.gg/repository/maven-public")
    maven("https://maven.neoforged.net/releases")
    maven("https://repo.spongepowered.org/repository/maven-public/")
}

// Common dependencies for all versions
dependencies {
    // Add any common dependencies here
    // For example:
    // implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.5.2")
    annotationProcessor("org.spongepowered:mixin:0.8.5:processor")
    implementation("io.github.blackspherefollower:buttplug4j.connectors.jetty.websocket.client:3.1.105")
}

tasks {
    withType<JavaCompile> {
        options.encoding = "UTF-8"
    }
}