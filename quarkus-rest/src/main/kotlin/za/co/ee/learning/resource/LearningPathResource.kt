package za.co.ee.learning.resource

import io.smallrye.common.annotation.RunOnVirtualThread
import jakarta.inject.Inject
import jakarta.ws.rs.Consumes
import jakarta.ws.rs.DELETE
import jakarta.ws.rs.GET
import jakarta.ws.rs.POST
import jakarta.ws.rs.PUT
import jakarta.ws.rs.Path
import jakarta.ws.rs.PathParam
import jakarta.ws.rs.Produces
import jakarta.ws.rs.QueryParam
import jakarta.ws.rs.core.MediaType
import jakarta.ws.rs.core.Response
import za.co.ee.learning.model.KotlinLearningPath
import za.co.ee.learning.service.InvalidLearningResourceUrl
import za.co.ee.learning.service.LearningPathService

/**
 * REST resource for managing Kotlin learning paths.
 */
@Path("/learning-path")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON, MediaType.APPLICATION_FORM_URLENCODED)
class LearningPathResource {
    @Inject
    lateinit var learningPathService: LearningPathService

    /**
     * Get the current learning path.
     *
     * @return The current learning path
     */
    @GET
    @RunOnVirtualThread
    fun getLearningPath(): KotlinLearningPath {
        return learningPathService.getLearningPath()
    }

    /**
     * Update the name of the learning path.
     *
     * @param name The new name for the learning path
     * @return The updated learning path
     */
    @PUT
    @Path("/name/{name}")
    @RunOnVirtualThread
    fun updateName(
        @PathParam("name") name: String,
    ): KotlinLearningPath {
        return learningPathService.updateName(name)
    }

    /**
     * Add or update a resource in the learning path.
     *
     * @param name The name of the resource
     * @param url The URL of the resource
     * @return The updated learning path
     */
    @PUT
    @Path("/resource")
    @RunOnVirtualThread
    fun addOrUpdateResource(
        @QueryParam("name") name: String?,
        @QueryParam("url") url: String?,
    ): Response {
        if (name.isNullOrBlank() || url.isNullOrBlank()) {
            return Response.status(Response.Status.BAD_REQUEST)
                .entity("Both name and url parameters are required")
                .build()
        }

        return try {
            Response.ok(learningPathService.addOrUpdateResource(name, url)).build()
        } catch (e: InvalidLearningResourceUrl) {
            Response.status(Response.Status.BAD_REQUEST)
                .entity(e.message)
                .build()
        }
    }

    /**
     * Remove a resource from the learning path.
     *
     * @param name The name of the resource to remove
     * @return The updated learning path
     */
    @DELETE
    @Path("/resource/{name}")
    @RunOnVirtualThread
    fun removeResource(
        @PathParam("name") name: String,
    ): KotlinLearningPath {
        return learningPathService.removeResource(name)
    }

    /**
     * Reset the learning path to the default values.
     *
     * @return The reset learning path
     */
    @POST
    @Path("/reset")
    @RunOnVirtualThread
    fun resetToDefault(): KotlinLearningPath {
        return learningPathService.resetToDefault()
    }
}
