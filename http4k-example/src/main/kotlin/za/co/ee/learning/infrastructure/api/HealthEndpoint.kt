package za.co.ee.learning.infrastructure.api

import kotlinx.serialization.Serializable
import org.http4k.contract.ContractRoute
import org.http4k.contract.meta
import org.http4k.core.Body
import org.http4k.core.Method
import org.http4k.core.Request
import org.http4k.core.Response
import org.http4k.core.Status
import org.http4k.core.with
import org.http4k.format.KotlinxSerialization.auto

@Serializable
data class HealthResponse(
    val status: String,
    val service: String,
)

object HealthEndpoint {
    private val healthLens = Body.auto<HealthResponse>().toLens()
    private val healthResponseLens = Body.auto<HealthResponse>().toLens()

    val handler: (Request) -> Response = { _: Request ->
        Response(Status.OK).with(
            healthLens of
                HealthResponse(
                    status = "UP",
                    service = "example-api",
                ),
        )
    }

    val route: ContractRoute =
        "/health" meta {
            summary = "Health Check"
            description = "Returns the health status of the API"
            returning(
                Status.OK,
                healthResponseLens to HealthResponse(status = "UP", service = "example-api"),
                "Successful health check",
            )
        } bindContract Method.GET to handler
}
