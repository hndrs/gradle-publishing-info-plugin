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

        findMavenPublications(gradle.rootProject)
            .forEach { publication ->
                getPublishingInfoExtension(gradle.rootProject)?.let {
                    logger.info("Applying publishing info to publication: {}", publication.name)
                    applyPublishingDetails(publication, it)
                }
            }

        gradle.rootProject.subprojects.forEach { subProject ->
            findMavenPublications(subProject).forEach { publication ->
                getPublishingInfoExtension(subProject)?.let {
                    if (it.applyFromRoot) {
                        logger.info("Applying publishing info from RootProject to publication: {}", publication.name)
                        getPublishingInfoExtension(gradle.rootProject)?.let { applyPublishingDetails(publication, it) }
                    }
                    logger.info("Applying publishing info to publication: {}", publication.name)
                    applyPublishingDetails(publication, it)
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

    private fun findMavenPublications(project: Project): List<MavenPublication> {
        return project.extensions.findByType(PublishingExtension::class.java)?.publications
            ?.filter { it is MavenPublication }
            ?.map { it as MavenPublication }.orEmpty()
    }

    private fun getPublishingInfoExtension(project: Project): PublishingInfoExtension? {
        return project.extensions.findByType(PublishingInfoExtension::class.java)
    }

    companion object {
        private val logger: Logger = LoggerFactory.getLogger(PublishingInfoBuildListener::class.java)
    }
}
