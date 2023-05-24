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
import org.gradle.api.publish.maven.MavenPomContributor
import org.gradle.api.publish.maven.MavenPomContributorSpec
import org.gradle.api.publish.maven.MavenPomDeveloper
import org.gradle.api.publish.maven.MavenPomDeveloperSpec
import org.gradle.api.publish.maven.MavenPomLicense
import org.gradle.api.publish.maven.MavenPomLicenseSpec
import org.gradle.api.publish.maven.MavenPomOrganization
import org.gradle.api.publish.maven.MavenPomScm
import org.gradle.api.publish.maven.MavenPublication
import org.junit.jupiter.api.Test

internal class MavenPublicationConfigurationTest {

    @Test
    fun projectsWithoutPublications() {
        val mavenPublicationConfiguration = MavenPublicationConfiguration()

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
        mavenPublicationConfiguration.execute(gradle)
        verify(exactly = 0) { rootProjectExtensions.findByType(PublishingInfoExtension::class.java) }
        verify(exactly = 0) { subProjectExtensions.findByType(PublishingInfoExtension::class.java) }
    }

    @Test
    fun projectsWithoutMavenPublish() {
        val mavenPublicationConfiguration = MavenPublicationConfiguration()

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
        mavenPublicationConfiguration.execute(gradle)
        verify(exactly = 0) { rootProjectExtensions.findByType(PublishingInfoExtension::class.java) }
        verify(exactly = 0) { subProjectExtensions.findByType(PublishingInfoExtension::class.java) }
    }

    @Test
    fun projectsEvaluated() {
        val mavenPublicationConfiguration = MavenPublicationConfiguration()
        val gradle = mockkGradle(PublishingInfoExtension(), mockk(relaxed = true), mockk(relaxed = true))
        mavenPublicationConfiguration.execute(gradle)
    }

    @Test
    fun projectsEvaluatedWithData() {
        val mavenPublicationConfiguration = MavenPublicationConfiguration()


        // Mock Developer setup
        val mavenPomDeveloperSpecSlot = slot<Action<MavenPomDeveloperSpec>>()
        val mavenPomDeveloperSlot = slot<Action<MavenPomDeveloper>>()
        val mavenPomDeveloperSpec = mockk<MavenPomDeveloperSpec>(relaxed = true) {
            every { developer(capture(mavenPomDeveloperSlot)) } returns mockk()
        }

        // Mock Developer setup
        val mavenPomContributorSpecSlot = slot<Action<MavenPomContributorSpec>>()
        val mavenPomContributorSlot = slot<Action<MavenPomContributor>>()
        val mavenPomContributorSpec = mockk<MavenPomContributorSpec>(relaxed = true) {
            every { contributor(capture(mavenPomContributorSlot)) } returns mockk()
        }

        // Mokc Organisation setup
        val mavenPomOrganizationSlot = slot<Action<MavenPomOrganization>>()

        // Mock SCM setup
        val mavenPomScmSlot = slot<Action<MavenPomScm>>()
        // Mock License setup
        val mavenPomLicenseSpecSlot = slot<Action<MavenPomLicenseSpec>>()
        val mavenPomLicenseSlot = slot<Action<MavenPomLicense>>()
        val mavenPomLicenseSpec = mockk<MavenPomLicenseSpec>() {
            every { license(capture(mavenPomLicenseSlot)) } returns mockk()
        }

        val rootProjectPom = mockk<MavenPom>(relaxed = true) {
            every { developers(capture(mavenPomDeveloperSpecSlot)) } returns mockk()
            every { contributors(capture(mavenPomContributorSpecSlot)) } returns mockk()
            every { organization(capture(mavenPomOrganizationSlot)) } returns mockk()
            every { scm(capture(mavenPomScmSlot)) } returns mockk()
            every { licenses(capture(mavenPomLicenseSpecSlot)) } returns mockk()
        }


        val subProjectPom = mockk<MavenPom>(relaxed = true)
        val gradle = mockkGradle(testPublishingInfo(), rootProjectPom, subProjectPom)
        mavenPublicationConfiguration.execute(gradle)

        mavenPomDeveloperSpecSlot.captured.execute(mavenPomDeveloperSpec)
        val developer = mockk<MavenPomDeveloper>(relaxed = true)
        mavenPomDeveloperSlot.captured.execute(developer)
        verify(exactly = 1) { developer.email.set(testPublishingInfo().developers[0].email) }
        verify(exactly = 1) { developer.id.set(testPublishingInfo().developers[0].id) }
        verify(exactly = 1) { developer.name.set(testPublishingInfo().developers[0].name) }

        mavenPomContributorSpecSlot.captured.execute(mavenPomContributorSpec)
        val contributor = mockk<MavenPomContributor>(relaxed = true)
        mavenPomContributorSlot.captured.execute(contributor)
        verify(exactly = 1) { contributor.email.set(testPublishingInfo().contributers[0].email) }
        verify(exactly = 1) { contributor.name.set(testPublishingInfo().contributers[0].name) }
        verify(exactly = 1) { contributor.url.set(testPublishingInfo().contributers[0].url) }

        val org = mockk<MavenPomOrganization>(relaxed = true)
        mavenPomOrganizationSlot.captured.execute(org)
        verify(exactly = 1) { org.name.set(testPublishingInfo().organization!!.name) }
        verify(exactly = 1) { org.url.set(testPublishingInfo().organization!!.url) }

        val scm = mockk<MavenPomScm>(relaxed = true)
        mavenPomScmSlot.captured.execute(scm)
        verify(exactly = 1) { scm.url.set(testPublishingInfo().scm!!.url) }
        verify(exactly = 1) { scm.connection.set(testPublishingInfo().scm!!.connection) }

        mavenPomLicenseSpecSlot.captured.execute(mavenPomLicenseSpec)
        val license = mockk<MavenPomLicense>(relaxed = true)
        mavenPomLicenseSlot.captured.execute(license)
        verify(exactly = 1) { license.name.set(testPublishingInfo().license!!.name) }
        verify(exactly = 1) { license.url.set(testPublishingInfo().license!!.url) }
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
