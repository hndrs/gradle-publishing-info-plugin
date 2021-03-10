package io.hndrs.gradle.plugin

import io.mockk.every
import io.mockk.mockk
import org.gradle.api.invocation.Gradle
import org.gradle.api.publish.PublishingExtension
import org.gradle.api.publish.maven.MavenPom
import org.gradle.api.publish.maven.MavenPublication
import org.junit.jupiter.api.Test

internal class PublishingInfoBuildListenerTest {

    @Test
    fun projectsEvaluated() {
        val listener = PublishingInfoBuildListener()
        val gradle = mockkGradle(PublishingInfoExtension(), mockk(relaxed = true), mockk(relaxed = true))
        listener.projectsEvaluated(gradle)

        val gradle1 = mockkGradle(testPublishingInfo(), mockk(relaxed = true), mockk(relaxed = true))
        listener.projectsEvaluated(gradle1)


    }

    private fun mockkGradle(
        publishingInfoExtension: PublishingInfoExtension,
        rootProjectPom: MavenPom,
        subProjectPom: MavenPom
    ): Gradle {

        val rootProjectMavenPublication = mockk<MavenPublication>() {
            every { name } returns "RootProjectPublication"
            every { pom } returns rootProjectPom
        }

        val subProjectMavenPublication = mockk<MavenPublication>() {
            every { name } returns "RootProjectPublication"
            every { pom } returns subProjectPom
        }

        val gradle = mockk<Gradle>() {
            every { rootProject } returns mockk() {
                every { extensions } returns mockk() {
                    every { findByType(PublishingInfoExtension::class.java) } returns publishingInfoExtension
                    every { findByType(PublishingExtension::class.java) } returns mockk() {
                        every { publications } returns mockk() {
                            every { publications.iterator() } returns mutableSetOf(rootProjectMavenPublication).iterator()
                        }
                    }
                }
                every { subprojects } returns setOf(
                    mockk() {
                        every { extensions } returns mockk() {
                            every { findByType(PublishingInfoExtension::class.java) } returns publishingInfoExtension
                            every { findByType(PublishingExtension::class.java) } returns mockk() {
                                every { publications } returns mockk() {
                                    every { publications.iterator() } returns mutableSetOf<MavenPublication>(subProjectMavenPublication).iterator()
                                }

                            }
                        }
                    })
            }
        }

        return gradle
    }


    private fun testPublishingInfo(): PublishingInfoExtension {
        val extension = PublishingInfoExtension()
        extension.name = "TestName"
        extension.inceptionYear = "2021"
        extension.developers = listOf(Developer("Id", "Name", "email"))
        extension.contributers = listOf(Contributor("ContributerName", "email", "url"))
        extension.license = License("url", "name")
        extension.organization = Organization("Name", "url")
        extension.url = "url"
        extension.description = "description"
        extension.scm = Scm("connection", "url")
        extension.applyFromRoot = true
        return extension
    }
}
