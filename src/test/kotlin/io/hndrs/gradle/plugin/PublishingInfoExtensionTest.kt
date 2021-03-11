package io.hndrs.gradle.plugin

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Test

internal class PublishingInfoExtensionTest {


    @Test
    fun testDefaultValues() {
        val extension = PublishingInfoExtension()

        assertFalse(extension.applyFromRoot)
        assertNull(extension.inceptionYear)
        assertNull(extension.description)
        assertNull(extension.license)
        assertNull(extension.name)
        assertNull(extension.scm)
        assertNull(extension.url)
        assertNull(extension.organization)
        assertNull(extension.organization)
        assertEquals(listOf<Developer>(), extension.developers)
        assertEquals(listOf<Contributor>(), extension.contributers)

    }
}
