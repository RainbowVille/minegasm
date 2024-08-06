import gg.essential.gradle.util.*

plugins {
    kotlin("jvm") version "2.0.10" apply false
    id("gg.essential.multi-version.root")
}

preprocess {
    val forge11605 = createNode("1.16.5", 11605, "mcp")
    val forge11502 = createNode("1.15.2", 11502, "mcp")
    val forge11202 = createNode("1.12.2", 11202, "mcp")
    forge11605.link(forge11502)
    forge11502.link(forge11202)
}