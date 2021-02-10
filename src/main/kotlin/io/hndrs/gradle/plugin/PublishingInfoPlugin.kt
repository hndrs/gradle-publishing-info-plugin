package io.hndrs.gradle.plugin

import org.gradle.api.Action
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.ExtensionAware
import org.gradle.api.publish.PublishingExtension
import org.gradle.api.publish.maven.MavenPublication


class PublishingInfoPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        // Add the 'greeting' extension object
        val extension = project.extensions.create("publishingInfo", PublishingInfoPluginExtension::class.java)

        // Add a task that uses configuration from the extension object
        project.extensions.findByType(PublishingExtension::class.java)?.publications?.configureEach {
            if (this is MavenPublication) {
                this.pom {
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
        }
    }
}

/**
 * Enable Gradle dsl configuration
 */
fun Project.`publishingInfo`(configure: Action<PublishingInfoPluginExtension>): Unit =
    (this as ExtensionAware).extensions.configure("publishingInfo", configure)
