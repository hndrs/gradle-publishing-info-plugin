package io.hndrs.gradle.plugin

open class PublishingInfoPluginExtension {

    /**
     * Url of this project
     */
    var url: String? = null

    /**
     * Organisational information
     */
    var organization: Organization? = null

    /**
     * SCM information
     */
    var scm: Scm? = null

    /**
     * List of developers
     */
    var developers: List<Developer> = listOf()

    /**
     * List of contributers
     */
    var contributers: List<Contributor> = listOf()

    /**
     * License information
     */
    var license: License? = null
}
