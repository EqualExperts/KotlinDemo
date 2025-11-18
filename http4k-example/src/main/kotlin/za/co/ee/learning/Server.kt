package za.co.ee.learning

import io.micrometer.core.instrument.simple.SimpleMeterRegistry
import org.http4k.core.HttpHandler
import org.http4k.core.then
import org.http4k.filter.CorsPolicy
import org.http4k.filter.DebuggingFilters
import org.http4k.filter.DebuggingFilters.PrintRequestAndResponse.invoke
import org.http4k.filter.MicrometerMetrics
import org.http4k.filter.OpenTelemetryMetrics
import org.http4k.filter.OpenTelemetryTracing
import org.http4k.filter.ServerFilters
import org.http4k.filter.ServerFilters.CatchAll.invoke
import org.http4k.filter.ServerFilters.CatchLensFailure
import org.http4k.filter.ServerFilters.Cors.invoke
import org.http4k.routing.routes
import org.http4k.server.Netty
import org.http4k.server.asServer
import za.co.ee.learning.infrastructure.database.InMemoryUserRepository
import za.co.ee.learning.infrastructure.routes.ContractRoutes
import za.co.ee.learning.infrastructure.routes.SwaggerRoutes
import za.co.ee.learning.infrastructure.security.BCryptPasswordProvider
import za.co.ee.learning.infrastructure.security.DefaultJWTProvider
import za.co.ee.learning.infrastructure.security.JWTFilter

class Server(
    private val config: ServerConfig = ServerConfig(),
) {
    private val registry = SimpleMeterRegistry()

    private val passwordProvider = BCryptPasswordProvider()
    private val userRepository = InMemoryUserRepository()
    private val jwtProvider = DefaultJWTProvider(config.jwtSecret, config.jwtIssuer, config.jwtExpirationSeconds)

    private val contractRoutes = ContractRoutes(userRepository, passwordProvider, jwtProvider)
    private val swaggerRoutes = SwaggerRoutes()

    private val corsFilter = ServerFilters.Cors(CorsPolicy.UnsafeGlobalPermissive)
    private val jwtFilter =
        JWTFilter(
            jwtProvider = jwtProvider,
            excludedPaths = setOf("/health", "/api/v1/authenticate", "/openapi.json"),
        )

    private val app: HttpHandler = { request ->
        swaggerRoutes.routes(request).let { response ->
            if (response.status.successful || response.status.redirection) {
                response
            } else {
                jwtFilter.then(contractRoutes.routes)(request)
            }
        }
    }

    private val appWithFilters =
        if (config.enableDebugFilters && config.enableMetrics) {
            DebuggingFilters
                .PrintRequestAndResponse()
                .then(ServerFilters.CatchAll())
                .then(CatchLensFailure())
                .then(ServerFilters.MicrometerMetrics.RequestCounter(registry))
                .then(ServerFilters.MicrometerMetrics.RequestTimer(registry))
                .then(ServerFilters.OpenTelemetryTracing())
                .then(ServerFilters.OpenTelemetryMetrics.RequestCounter())
                .then(ServerFilters.OpenTelemetryMetrics.RequestTimer())
                .then(corsFilter)
                .then(app)
        } else {
            ServerFilters
                .CatchAll()
                .then(CatchLensFailure())
                .then(corsFilter)
                .then(app)
        }

    private val netty = appWithFilters.asServer(Netty(config.port))

    fun start(): Server {
        netty.start()
        return this
    }

    fun stop() {
        netty.stop()
    }

    fun port(): Int = netty.port()
}
