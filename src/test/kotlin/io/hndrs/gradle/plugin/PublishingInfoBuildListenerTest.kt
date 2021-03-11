package io.hndrs.gradle.plugin

import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import io.mockk.verify
import org.gradle.api.Action
import org.gradle.api.invocation.Gradle
import org.gradle.api.publish.Publication
import org.gradle.api.publish.PublishingExtension
import org.gradle.api.publish.maven.MavenPom
import org.gradle.api.publish.maven.MavenPomDeveloper
import org.gradle.api.publish.maven.MavenPomDeveloperSpec
import org.gradle.api.publish.maven.MavenPomOrganization
import org.gradle.api.publish.maven.MavenPublication
import org.junit.jupiter.api.Test

internal class PublishingInfoBuildListenerTest {

    @Test
    fun projectsWithoutPublications() {
        val listener = PublishingInfoBuildListener()

        val gradle = mockk<Gradle>() {
            every { rootProject } returns mockk() {
                every { extensions } returns mockk() {
                    every { findByType(PublishingExtension::class.java) } returns mockk() {
                        every { publications } returns mockk() {
                            every { publications.iterator() } returns mutableSetOf<MavenPublication>().iterator()
                        }
                    }
                    every { subprojects } returns setOf(
                        mockk() {
                            every { extensions } returns mockk() {
                                every { findByType(PublishingExtension::class.java) } returns mockk() {
                                    every { publications } returns mockk() {
                                        every { publications.iterator() } returns mutableSetOf<MavenPublication>().iterator()
                                    }
                                }
                            }
                        })
                }
            }
        }
        val rootProjectExtensions = gradle.rootProject.extensions
        val subProjectExtensions = gradle.rootProject.subprojects.first().extensions
        listener.projectsEvaluated(gradle)
        verify(exactly = 0) { rootProjectExtensions.findByType(PublishingInfoExtension::class.java) }
        verify(exactly = 0) { subProjectExtensions.findByType(PublishingInfoExtension::class.java) }
    }

    @Test
    fun projectsWithoutMavenPublish() {
        val listener = PublishingInfoBuildListener()

        val gradle = mockk<Gradle>() {
            every { rootProject } returns mockk() {
                every { extensions } returns mockk() {
                    every { findByType(PublishingExtension::class.java) } returns null
                }
                every { subprojects } returns setOf(
                    mockk() {
                        every { extensions } returns mockk() {
                            every { findByType(PublishingExtension::class.java) } returns mockk() {
                                every { publications } returns null
                            }
                        }
                    })
            }
        }

        val rootProjectExtensions = gradle.rootProject.extensions
        val subProjectExtensions = gradle.rootProject.subprojects.first().extensions
        listener.projectsEvaluated(gradle)
        verify(exactly = 0) { rootProjectExtensions.findByType(PublishingInfoExtension::class.java) }
        verify(exactly = 0) { subProjectExtensions.findByType(PublishingInfoExtension::class.java) }
    }

    @Test
    fun projectsEvaluated() {
        val listener = PublishingInfoBuildListener()
        val gradle = mockkGradle(PublishingInfoExtension(), mockk(relaxed = true), mockk(relaxed = true))
        listener.projectsEvaluated(gradle)
    }

    @Test
    fun projectsEvaluatedWithData() {
        val listener = PublishingInfoBuildListener()


        val developerSpec = slot<Action<MavenPomDeveloperSpec>>()
        val developerPom = slot<Action<MavenPomDeveloper>>()
        val mavenPomDeveloper = mockk<MavenPomDeveloperSpec>(relaxed = true) {
            every { developer(capture(developerPom)) } returns mockk()
        }

        val mavenPomOrganization = slot<Action<MavenPomOrganization>>()
        val rootProjectPom = mockk<MavenPom>(relaxed = true) {
            every { developers(capture(developerSpec)) } returns mockk()
            every { organization(capture(mavenPomOrganization)) } returns mockk()
        }


        val subProjectPom = mockk<MavenPom>(relaxed = true)
        val gradle = mockkGradle(testPublishingInfo(), rootProjectPom, subProjectPom)
        listener.projectsEvaluated(gradle)

        developerSpec.captured.execute(mavenPomDeveloper)
        val developer = mockk<MavenPomDeveloper>(relaxed = true)
        developerPom.captured.execute(developer)
        verify(exactly = 1) { developer.email }
        verify(exactly = 1) { developer.id }
        verify(exactly = 1) { developer.name }

        val org = mockk<MavenPomOrganization>(relaxed = true)
        mavenPomOrganization.captured.execute(org)
        verify(exactly = 1) { org.name }
        verify(exactly = 1) { org.url }
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
                            every { publications.iterator() } returns mutableSetOf(rootProjectMavenPublication, mockk<Publication>()).iterator()
                        }
                    }
                }
                every { subprojects } returns setOf(
                    mockk() {
                        every { extensions } returns mockk() {
                            every { findByType(PublishingInfoExtension::class.java) } returns publishingInfoExtension
                            every { findByType(PublishingExtension::class.java) } returns mockk() {
                                every { publications } returns mockk() {
                                    every { publications.iterator() } returns mutableSetOf(subProjectMavenPublication, mockk<Publication>()).iterator()
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
