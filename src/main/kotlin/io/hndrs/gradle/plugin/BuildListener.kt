package io.hndrs.gradle.plugin

import org.gradle.BuildAdapter
import org.gradle.api.invocation.Gradle
import org.gradle.api.publish.PublishingExtension
import org.gradle.api.publish.maven.MavenPublication
import org.slf4j.Logger
import org.slf4j.LoggerFactory


class PublishingInfoBuildListener(private val extension: PublishingInfoPluginExtension) : BuildAdapter() {

    override fun projectsEvaluated(gradle: Gradle) {
        gradle.rootProject.extensions.findByType(PublishingExtension::class.java)?.publications?.configureEach {
            if (this is MavenPublication) {
                logger.warn("Applying publishing info to publication: ${this.name}")
                applyPublishingDetails(this, extension)
            }
        }
        gradle.rootProject.subprojects.forEach {
            it.extensions.findByType(PublishingExtension::class.java)?.publications?.configureEach {
                if (this is MavenPublication) {
                    logger.warn("Applying publishing info to publication: ${this.name}")
                    applyPublishingDetails(this, extension)
                }
            }
        }
    }

    private fun applyPublishingDetails(mavenPublication: MavenPublication, extension: PublishingInfoPluginExtension) {
        mavenPublication.pom {
            extension.name?.let {
                name.set(it)
            }
            extension.description?.let {
                description.set(it)
            }
            extension.url?.let {
                url.set(it)
            }
            extension.organization?.let {
                this.organization {
                    name.set(it.name)
                    url.set(it.url)
                }
            }
            this.developers {
                extension.developers.forEach {
                    this.developer {
                        id.set(it.id)
                        name.set(it.name)
                        email.set(it.email)
                    }
                }
            }
            this.contributors {
                extension.contributers.forEach {
                    this.contributor {
                        name.set(it.name)
                        email.set(it.email)
                        url.set(it.url)
                    }
                }
            }
            extension.scm?.let {
                this.scm {
                    connection.set(it.connection)
                    url.set(it.url)
                }
            }
            extension.license?.let {
                this.licenses {
                    this.license {
                        name.set(it.name)
                        url.set(it.url)

                    }
                }
            }

        }
    }

    companion object {
        private val logger: Logger = LoggerFactory.getLogger(PublishingInfoBuildListener::class.java)
    }
}
