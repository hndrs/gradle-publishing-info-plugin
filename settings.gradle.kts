pluginManagement {
    val kotlinVersion = "1.5.30"
    plugins {
        kotlin("jvm").version(kotlinVersion)
        kotlin("kapt").version(kotlinVersion)
        id("maven-publish")
        id("idea")
    }
    repositories {
    }
}
