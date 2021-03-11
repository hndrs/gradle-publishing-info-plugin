package io.hndrs.gradle.plugin

import org.slf4j.LoggerFactory

object Logger {

    private const val LOG_PREFIX = "PublishingInfoPlugin:"
    private val logger = LoggerFactory.getLogger("PublishingInfoLogger")

    fun info(message: String, vararg any: Any) {
        logger.info("$LOG_PREFIX $message", any)
    }

    fun warn(message: String, vararg any: Any) {
        logger.warn("$LOG_PREFIX $message", any)
    }

}
