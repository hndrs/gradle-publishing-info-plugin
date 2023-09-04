import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("org.sonarqube").version("4.0.0.2929")
    `kotlin-dsl`
    `maven-publish`
    jacoco
    id("java-gradle-plugin")
    id("com.gradle.plugin-publish").version("1.2.0")
    kotlin("jvm")
}

group = "io.hndrs.gradle"
version = "3.1.0"

java.sourceCompatibility = JavaVersion.VERSION_17
java.targetCompatibility = JavaVersion.VERSION_17

var publishingInfoPlugin: NamedDomainObjectProvider<PluginDeclaration>? = null

repositories {
    mavenCentral()
}

val tagList = listOf("maven", "publish", "repository", "library", "pom", "pom.xml", "kotlin")

gradlePlugin {
    website.set("https://github.com/hndrs/gradle-publishing-info-plugin")
    vcsUrl.set("https://github.com/hndrs/gradle-publishing-info-plugin.git")

    plugins {
        create("publishingInfoPlugin") {
            id = "io.hndrs.publishing-info"
            displayName = "Gradle Publishing Info plugin"
            implementationClass = "io.hndrs.gradle.plugin.PublishingInfoPlugin"
            description = "Simplifies adding publishing meta data to maven publications"
            tags.set(tagList)
        }
    }
}
dependencies {
    testImplementation(platform("org.junit:junit-bom:5.9.3"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testImplementation("io.mockk:mockk:1.13.5")
    testImplementation(group = "io.kotest", name = "kotest-assertions-core", version = "5.7.1")
}

sonarqube {
    properties {
        property("sonar.projectKey", "hndrs_gradle-publishing-info-plugin")
        property("sonar.organization", "hndrs")
        property("sonar.host.url", "https://sonarcloud.io")
        property("sonar.exclusions", "**/sample/**")
    }
}

configure<JacocoPluginExtension> {
    toolVersion = "0.8.9"
}
tasks.withType<JacocoReport> {
    reports {
        xml.apply {
            this.required.set(true)
        }

    }
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "17"
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}
