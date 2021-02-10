plugins {
    `kotlin-dsl`
    `maven-publish`
    id("java-gradle-plugin")
    id("com.gradle.plugin-publish").version("0.12.0")
    kotlin("jvm").version("1.4.30")
}

group = "io.hndrs.gradle"
version = "1.0.0"

var publishingInfoPlugin: NamedDomainObjectProvider<PluginDeclaration>? = null

repositories {
    jcenter()
}

gradlePlugin {
    plugins {
        publishingInfoPlugin = register("publishing-info") {
            id = "io.hndrs.gradle.publishing.info"
            implementationClass = "io.hndrs.gradle.plugin.PublishingInfoPlugin"
        }
    }
}

pluginBundle {
    website = "https://github.com/kreait/aws-credentials-support-plugin"
    vcsUrl = "https://github.com/kreait/aws-credentials-support-plugin.git"
    description = "Simplifies adding publishing meta data to maven publications"
    tags = listOf("maven", "publish", "repository", "gradle", "library", "pom", "pom.xml")
    plugins {
        publishingInfoPlugin
    }
}

val sourcesJar by tasks.creating(Jar::class) {
    dependsOn("classes")
    archiveClassifier.set("sources")
    from(sourceSets["main"].allSource)
}

publishing {
    repositories {
        gradlePluginPortal()
    }
    publications {
        create<MavenPublication>("mavenJava") {
            from(components["java"])
            artifact(sourcesJar)

            groupId = rootProject.group as? String
            artifactId = rootProject.name
            version = rootProject.version as? String
        }
    }
}
