import gg.essential.gradle.util.*

plugins {
    kotlin("jvm") version "2.0.10" apply false
    id("gg.essential.multi-version.root")
}

preprocess {
    //val neoforge12100 = createNode("1.21", 12100, "official")
    //val forge12100 = createNode("1.21-legacy", 12100, "official")
    //val neoforge12006 = createNode("1.20.6", 12006, "official")
    val forge12006 = createNode("1.20.6-legacy", 12006, "official")
    val neoforge12004 = createNode("1.20.4", 12004, "official")
    val forge12004 = createNode("1.20.4-legacy", 12004, "official")
    val neoforge12002 = createNode("1.20.2", 12002, "official")
    val forge12002 = createNode("1.20.2-legacy", 12002, "official")
    val forge12001 = createNode("1.20.1", 12001, "official")
    val forge11904 = createNode("1.19.4", 11904, "official")
    val forge11902 = createNode("1.19.2", 11902, "official")
    val forge11802 = createNode("1.18.2", 11802, "official")
    val forge11605 = createNode("1.16.5", 11605, "srg")
    //val forge11502 = createNode("1.15.2", 11502, "official")
    val forge11202 = createNode("1.12.2", 11202, "srg")
    val forge10809 = createNode("1.8.9", 10809, "srg")
    //neoforge12100.link(forge12100)
    //forge12100.link(forge12006)
    //neoforge12006.link(forge12006, file("versions/forge-neoforge"))
    //neoforge12006.link(neoforge12004)
    forge12006.link(forge12004)
    neoforge12004.link(neoforge12002)
    forge12004.link(forge12002)
    neoforge12002.link(forge12002)
    forge12002.link(forge12001)
    forge12001.link(forge11904)
    forge11904.link(forge11902)
    forge11902.link(forge11802)
    forge11802.link(forge11605)
    forge11605.link(forge11202)
    //forge11605.link(forge11502)
    //forge11502.link(forge11202, file("versions/1.15.2-1.12.2"))
    forge11202.link(forge10809, file("versions/1.12.2-1.8.9"))
}

preprocess.strictExtraMappings.set(true)