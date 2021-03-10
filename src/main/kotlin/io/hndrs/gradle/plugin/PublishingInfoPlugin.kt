package io.hndrs.gradle.plugin

import org.gradle.api.Action
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.ExtensionAware


class PublishingInfoPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        // add extension to root project automatically
        val extension = project.extensions.create("publishingInfo", PublishingInfoPluginExtension::class.java)

        project.gradle
            .addBuildListener(
                PublishingInfoBuildListener(extension)
            )
    }
}

/**
 * Enable Gradle dsl configuration
 */
fun Project.`publishingInfo`(configure: Action<PublishingInfoPluginExtension>): Unit =
    (this as ExtensionAware).extensions.configure("publishingInfo", configure)
