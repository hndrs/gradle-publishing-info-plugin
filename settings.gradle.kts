pluginManagement {
    val kotlinVersion = "1.5.30"
    plugins {
        id("io.spring.dependency-management").version("2.4.2")
        kotlin("jvm").version(kotlinVersion)
        kotlin("plugin.spring").version(kotlinVersion)
        kotlin("kapt").version(kotlinVersion)
        id("maven-publish")
        id("idea")
    }
    repositories {
    }
}
