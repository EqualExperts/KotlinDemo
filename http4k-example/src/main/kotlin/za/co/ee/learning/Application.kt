package za.co.ee.learning

import mu.KotlinLogging
import za.co.ee.learning.infrastructure.server.Server

fun main() {
    val logger = KotlinLogging.logger {}

    Server().start()

    logger.info { "API started successfully on port 8080" }
}
