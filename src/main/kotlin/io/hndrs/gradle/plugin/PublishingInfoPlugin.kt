package io.hndrs.gradle.plugin

import org.gradle.api.Action
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.invocation.Gradle
import org.gradle.api.plugins.ExtensionAware


class PublishingInfoPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        if (project == project.rootProject) {
            val gradle = project.gradle
            // add extension to root project automatically
            registerPublishingInfoExtensions(gradle)
            gradle.addBuildListener(PublishingInfoBuildListener())
        } else {
            Logger.warn(
                "Trying to apply plugin to SubProject {}. " +
                        "The plugin automatically registers on all projects. \n" +
                        "To remove this message remove apply plugin on the SubProject",
                project.name
            )
        }

    }

    private fun registerPublishingInfoExtensions(gradle: Gradle) {
        gradle.rootProject.extensions.create("publishingInfo", PublishingInfoExtension::class.java)
        gradle.rootProject.subprojects.forEach {
            it.extensions.create("publishingInfo", PublishingInfoExtension::class.java)
        }
    }
}

/**
 * Enable Gradle dsl configuration
 */
fun Project.publishingInfo(configure: Action<PublishingInfoExtension>): Unit =
    (this as ExtensionAware).extensions.configure("publishingInfo", configure)
