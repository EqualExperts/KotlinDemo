package za.co.ee.learning.integration

import io.kotest.core.spec.style.FunSpec
import io.restassured.RestAssured
import io.restassured.http.ContentType
import za.co.ee.learning.Server
import za.co.ee.learning.ServerConfig

abstract class BaseIntegrationTest : FunSpec() {
    private var server: Server? = null

    init {
        beforeSpec {
            val testConfig =
                ServerConfig(
                    port = 0, // Random available port
                    jwtSecret = "test-secret",
                    jwtIssuer = "http4k-test",
                    jwtExpirationSeconds = 3600,
                    enableDebugFilters = false,
                    enableMetrics = false,
                )

            server = Server(testConfig).start()
            val port = server?.port() ?: throw IllegalStateException("Server not started")
            RestAssured.port = port
            RestAssured.baseURI = "http://localhost"
        }

        afterSpec {
            server?.stop()
            RestAssured.reset()
        }
    }

    protected fun baseUrl() = "http://localhost:${RestAssured.port}"

    protected fun given() =
        RestAssured
            .given()
            .contentType(ContentType.JSON)
            .accept(ContentType.JSON)
}
