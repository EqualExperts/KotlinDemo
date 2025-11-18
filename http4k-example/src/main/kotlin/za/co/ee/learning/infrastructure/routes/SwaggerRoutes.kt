package za.co.ee.learning.infrastructure.routes

import org.http4k.contract.ui.swagger.swaggerUiWebjar
import org.http4k.routing.bind
import org.http4k.routing.routes

class SwaggerRoutes {
    val routes =
        routes(
            "/swagger" bind
                swaggerUiWebjar {
                    url = "/openapi.json"
                    pageTitle = "Http4k Example API"
                    displayOperationId = true
                },
        )
}
