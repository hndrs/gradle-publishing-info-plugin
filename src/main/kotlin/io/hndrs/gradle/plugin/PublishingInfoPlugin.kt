package io.hndrs.gradle.plugin

import org.gradle.api.Action
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.invocation.Gradle
import org.gradle.api.plugins.ExtensionAware
import org.slf4j.Logger
import org.slf4j.LoggerFactory


class PublishingInfoPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        if (project == project.rootProject) {
            val gradle = project.gradle
            // add extension to root project automatically
            registerPublishingInfoExtensions(gradle)
            gradle.addBuildListener(PublishingInfoBuildListener())
        } else {
            logger.warn(
                "Trying to apply plugin to subproject. " +
                        "The plugin automatically registers on all projects. \n" +
                        "To remove this message remove apply plugin"
            )
        }

    }

    private fun registerPublishingInfoExtensions(gradle: Gradle) {
        gradle.rootProject.extensions.create("publishingInfo", PublishingInfoExtension::class.java)
        gradle.rootProject.subprojects.forEach {
            it.extensions.create("publishingInfo", PublishingInfoExtension::class.java)
        }
    }

    companion object {
        private val logger: Logger = LoggerFactory.getLogger(PublishingInfoPlugin::class.java)
    }
}

/**
 * Enable Gradle dsl configuration
 */
fun Project.publishingInfo(configure: Action<PublishingInfoExtension>): Unit =
    (this as ExtensionAware).extensions.configure("publishingInfo", configure)
