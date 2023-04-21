pluginManagement {
    val kotlinVersion = "1.8.10"
    plugins {
        kotlin("jvm").version(kotlinVersion)
        kotlin("kapt").version(kotlinVersion)
        id("maven-publish")
        id("idea")
    }
    repositories {
    }
}
