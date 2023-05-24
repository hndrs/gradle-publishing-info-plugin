import io.hndrs.gradle.plugin.Contributor
import io.hndrs.gradle.plugin.Developer
import io.hndrs.gradle.plugin.License
import io.hndrs.gradle.plugin.Organization
import io.hndrs.gradle.plugin.Scm
import io.hndrs.gradle.plugin.publishingInfo

plugins {
    id("java")
    kotlin("jvm").version("1.8.21")
    id("maven-publish")
    id("io.hndrs.publishing-info")
}



group = "io.hndrs.gradle.sample"
version = "1.0.0"
java.sourceCompatibility = JavaVersion.VERSION_11
java.targetCompatibility = JavaVersion.VERSION_11

repositories {
    mavenCentral()
}

val sourcesJarProject by tasks.creating(Jar::class) {
    dependsOn("classes")
    archiveClassifier.set("sources")
    from(sourceSets["main"].allSource)
}

publishing {
    repositories {

    }
    publications {
        create<MavenPublication>(project.name) {
            from(components["java"])
            artifact(sourcesJarProject)

            groupId = rootProject.group as? String
            artifactId = rootProject.name
            version = "${rootProject.version}${project.findProperty("version.appendix") ?: ""}"
        }
    }
}

publishingInfo {
    applyFromRoot = true
    name = "Root Project"
    description = "Sample Project to test Publishing Info Plugin"
    inceptionYear = "2021"
    url = "https://github.com/hndrs/gradle-publishing-info-plugin"
    license = License(
        "https://github.com/hndrs/gradle-publishing-info-plugin/blob/main/LICENSE",
        "MIT License"
    )
    developers = listOf(
        Developer("marvinschramm", "Maintainers Name", "maintainer@email.com")
    )
    contributers = listOf(
        Contributor("Contributer Name", "contributers email")
    )
    organization = Organization("Your Org", "https://yourdomain.com")
    scm = Scm(
        "scm:git:git://github.com/hndrs/gradle-publishing-info-plugin",
        "https://github.com/hndrs/gradle-publishing-info-plugin"
    )
}

subprojects {
    apply(plugin = "maven-publish")
    apply(plugin = "java")
    apply(plugin = "io.hndrs.publishing-info")
    java {
        withJavadocJar()
    }

    val sourcesJarSubProject by tasks.creating(Jar::class) {
        dependsOn("classes")
        archiveClassifier.set("sources")
        from(sourceSets["main"].allSource)
    }

    publishing {
        publications {
            create<MavenPublication>(project.name) {
                from(components["java"])
                artifact(sourcesJarSubProject)

                groupId = rootProject.group as? String
                artifactId = project.name
                version = "${rootProject.version}"
            }
        }
    }
}
