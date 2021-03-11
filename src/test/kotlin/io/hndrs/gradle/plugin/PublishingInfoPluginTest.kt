package io.hndrs.gradle.plugin

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.gradle.api.Project
import org.gradle.api.invocation.Gradle
import org.gradle.api.plugins.ExtensionContainer
import org.junit.jupiter.api.Test

internal class PublishingInfoPluginTest {

    @Test
    fun applyOnRootProject() {
        val plugin = PublishingInfoPlugin()


        val rootProjectExtensions = mockk<ExtensionContainer>(relaxed = true)
        val subProjectExtensions = mockk<ExtensionContainer>(relaxed = true)

        val gradleMockk = mockk<Gradle>(relaxed = true) {
            every { rootProject } returns mockk() {
                every { extensions } returns rootProjectExtensions
                every { subprojects } returns setOf(
                    mockk() {
                        every { extensions } returns subProjectExtensions
                    })
            }
        }

        val project = mockk<Project>() {
            every { rootProject } returns this
            every { gradle } returns gradleMockk
        }

        plugin.apply(project)

        verify(exactly = 1) { gradleMockk.addBuildListener(any()) }
        verify(exactly = 1) { rootProjectExtensions.create("publishingInfo", PublishingInfoExtension::class.java) }
        verify(exactly = 1) { subProjectExtensions.create("publishingInfo", PublishingInfoExtension::class.java) }
    }

    @Test
    fun applyOnSubProject() {
        val plugin = PublishingInfoPlugin()

        val gradleMockk = mockk<Gradle>()

        val project = mockk<Project>() {
            every { rootProject } returns mockk()
            every { gradle } returns gradleMockk
        }

        plugin.apply(project)

        verify(exactly = 0) { gradleMockk.addBuildListener(any()) }
    }
}
