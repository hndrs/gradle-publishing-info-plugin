plugins {
    `kotlin-dsl`
    `maven-publish`
    id("java-gradle-plugin")
    id("com.gradle.plugin-publish").version("0.12.0")
    kotlin("jvm").version("1.4.20")
}

group = "io.hndrs.gradle"
version = "1.0.0"

var publishingInfoPlugin: NamedDomainObjectProvider<PluginDeclaration>? = null

repositories {
    jcenter()
}

gradlePlugin {
    plugins {
        publishingInfoPlugin = register("publishingInfoPlugin") {
            id = "io.hndrs.publishing-info"
            implementationClass = "io.hndrs.gradle.plugin.PublishingInfoPlugin"
        }
    }
}

val tagList = listOf("maven", "publish", "repository", "gradle", "library", "pom", "pom.xml", "plugins")

pluginBundle {
    website = "https://github.com/hndrs/gradle-publishing-info-plugin"
    vcsUrl = "https://github.com/hndrs/gradle-publishing-info-plugin.git"
    description = "Simplifies adding publishing meta data to maven publications"
    tags = tagList
    (plugins) {
        // first plugin
        "publishingInfoPlugin" {
            // id is captured from java-gradle-plugin configuration
            displayName = "Gradle Publishing Info plugin"
            tags = tagList
            version = rootProject.version as? String
        }
    }
    mavenCoordinates {
        groupId = rootProject.group as? String
        artifactId = rootProject.name
        version = rootProject.version as? String
    }
}
