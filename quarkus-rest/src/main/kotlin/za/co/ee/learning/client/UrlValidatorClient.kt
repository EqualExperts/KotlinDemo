package za.co.ee.learning.client

import jakarta.ws.rs.Consumes
import jakarta.ws.rs.POST
import jakarta.ws.rs.Path
import jakarta.ws.rs.Produces
import jakarta.ws.rs.core.MediaType
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient

data class UrlValidationRequest(val name: String, val url: String)

data class UrlValidationResponse(val name: String, val result: String)

@Path("/validate-url")
@RegisterRestClient(configKey = "validator-service")
interface UrlValidatorClient {
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    fun validateUrl(urlValidationRequest: UrlValidationRequest): UrlValidationResponse
}
