pluginManagement {
    repositories {
        gradlePluginPortal()
        mavenCentral()
        maven("https://maven.fabricmc.net")
        maven("https://maven.neoforged.net/releases")
        maven("https://maven.architectury.dev/")
        maven("https://maven.minecraftforge.net")
        maven("https://repo.essential.gg/repository/maven-public")
    }
    plugins {
        val egtVersion = "0.6.2"
        id("gg.essential.defaults") version egtVersion
        id("gg.essential.multi-version.root") version egtVersion
        id("gg.essential.multi-version.api-validation") version egtVersion
        id("com.gradleup.shadow") version "8.3.3"
    }
}

rootProject.name = "minegasm"
rootProject.buildFileName = "root.gradle.kts"

/*file("versions").listFiles()?.filter { it.isDirectory }?.forEach { versionDir ->
    val versionName = versionDir.name
    include(":$versionName")
    project(":$versionName").apply {
        projectDir = versionDir
        buildFileName = "../../build.gradle.kts"
    }
}*/

listOf(
    //"1.21",
    //"1.21-legacy",
    //"1.20.6",
    //"1.20.6-legacy",
    //"1.20.4",
    //"1.20.4-legacy",
    //"1.20.2",
    //"1.20.2-legacy",
    //"1.20.1",
    //"1.19.4",
    //"1.19.2",
    "1.18.2",
    "1.16.5",
    //"1.12.2",
    //"1.8.9"
).forEach { version ->
    include(":$version")
    project(":$version").apply {
        projectDir = file("versions/$version")
        buildFileName = "../../build.gradle.kts"
    }
}
