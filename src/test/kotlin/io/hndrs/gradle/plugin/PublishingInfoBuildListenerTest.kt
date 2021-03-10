package io.hndrs.gradle.plugin

import io.mockk.every
import io.mockk.mockk
import org.gradle.api.invocation.Gradle
import org.gradle.api.publish.PublishingExtension
import org.gradle.api.publish.maven.MavenPublication
import org.junit.jupiter.api.Test

internal class PublishingInfoBuildListenerTest {

    @Test
    fun projectsEvaluated() {
        val listener = PublishingInfoBuildListener()


        val gradle = mockk<Gradle>() {
            every { rootProject } returns mockk() {
                every { extensions } returns mockk() {
                    every { findByType(PublishingExtension::class.java) } returns mockk() {
                        every { publications } returns mockk() {
                            every { publications.iterator() } returns mutableSetOf<MavenPublication>().iterator()
                        }

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

        listener.projectsEvaluated(gradle)
    }
}
