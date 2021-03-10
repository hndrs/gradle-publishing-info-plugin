import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("org.sonarqube").version("3.1.1")
    `kotlin-dsl`
    `maven-publish`
    jacoco
    id("java-gradle-plugin")
    id("com.gradle.plugin-publish").version("0.12.0")
    kotlin("jvm").version("1.4.20")
}

group = "io.hndrs.gradle"
version = "2.0.0"

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

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.7.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testImplementation("io.mockk:mockk:1.10.6")
}

sonarqube {
    properties {
        property("sonar.projectKey", "hndrs_gradle-publishing-info-plugin")
        property("sonar.organization", "hndrs")
        property("sonar.host.url", "https://sonarcloud.io")
        property("sonar.exclusions", "**/sample/**, **/io/hndrs/gradle/plugin/Data.kt")
    }
}

configure<JacocoPluginExtension> {
    toolVersion = "0.8.6"
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "11"
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}
