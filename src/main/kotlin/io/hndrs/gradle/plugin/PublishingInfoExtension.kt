package io.hndrs.gradle.plugin

open class PublishingInfoExtension {

    /**
     * Applies information from rootProject
     * Notes:
     * - has no effect on rootProject
     * - applies but DOES NOT override
     */
    var applyFromRoot: Boolean = false

    /**
     * Name of the project
     */
    var name: String? = null

    /**
     * Description of the project
     */
    var description: String? = null

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

    var inceptionYear: String? = null
}
