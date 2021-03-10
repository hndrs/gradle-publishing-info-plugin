package io.hndrs.gradle.plugin

import org.gradle.BuildAdapter
import org.gradle.api.Project
import org.gradle.api.invocation.Gradle
import org.gradle.api.publish.PublishingExtension
import org.gradle.api.publish.maven.MavenPublication
import org.slf4j.Logger
import org.slf4j.LoggerFactory


class PublishingInfoBuildListener() : BuildAdapter() {

    override fun projectsEvaluated(gradle: Gradle) {

        gradle.rootProject.extensions.findByType(PublishingExtension::class.java)?.publications?.forEach { publication ->
            if (publication is MavenPublication) {
                getPublishingExtension(gradle.rootProject)?.let {
                    logger.info("Applying publishing info to publication: {}", publication.name)
                    applyPublishingDetails(publication, it)
                }
            }
        }
        gradle.rootProject.subprojects.forEach { subProject ->
            subProject.extensions.findByType(PublishingExtension::class.java)?.publications?.forEach { publication ->
                if (publication is MavenPublication) {
                    getPublishingExtension(subProject)?.let {
                        if (it.applyFromRoot) {
                            logger.info("Applying publishing info from RootProject to publication: {}", publication.name)
                            getPublishingExtension(gradle.rootProject)?.let { applyPublishingDetails(publication, it) }
                        }
                        logger.info("Applying publishing info to publication: {}", publication.name)
                        applyPublishingDetails(publication, it)
                    }
                }
            }
        }
    }

    private fun applyPublishingDetails(mavenPublication: MavenPublication, extension: PublishingInfoExtension) {



        mavenPublication.pom.apply {
            extension.name?.let {
                name.set(it)
            }
            extension.description?.let {
                description.set(it)
            }
            extension.url?.let {
                url.set(it)
            }
            extension.inceptionYear?.let {
                inceptionYear.set(it)
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

    private fun getPublishingExtension(project: Project): PublishingInfoExtension? {
        return project.extensions.findByType(PublishingInfoExtension::class.java)
    }

    companion object {
        private val logger: Logger = LoggerFactory.getLogger(PublishingInfoBuildListener::class.java)
    }
}
