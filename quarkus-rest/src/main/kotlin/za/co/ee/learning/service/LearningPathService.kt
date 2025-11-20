package za.co.ee.learning.service

import jakarta.enterprise.context.ApplicationScoped
import org.eclipse.microprofile.rest.client.inject.RestClient
import za.co.ee.learning.client.UrlValidationRequest
import za.co.ee.learning.client.UrlValidatorClient
import za.co.ee.learning.model.KotlinLearningPath

/**
 * Service for managing Kotlin learning paths.
 */
@ApplicationScoped
class LearningPathService {
    @RestClient
    private lateinit var urlValidationClient: UrlValidatorClient

    private val defaultLearningPath =
        KotlinLearningPath(
            name = "Kotlin Learning Resources",
            resources =
                mutableMapOf(
                    "Kotlin Home" to "https://kotlinlang.org/",
                    "Quarkus Home" to "https://quarkus.io/",
                    "Http4k Home" to "https://www.http4k.org/",
                    "Ktor Home" to "https://ktor.io/",
                ),
        )

    private var learningPath: KotlinLearningPath = defaultLearningPath

    /**
     * Get the current learning path.
     *
     * @return The current learning path
     */
    fun getLearningPath(): KotlinLearningPath {
        return learningPath
    }

    /**
     * Update the name of the learning path.
     *
     * @param name The new name for the learning path
     * @return The updated learning path
     */
    fun updateName(name: String): KotlinLearningPath {
        learningPath.name = name
        return learningPath
    }

    /**
     * Add or update a resource in the learning path.
     *
     * @param name The name of the resource
     * @param url The URL of the resource
     * @return The updated learning path
     * @throws IllegalArgumentException if the URL is invalid
     */
    fun addOrUpdateResource(
        name: String,
        url: String,
    ): KotlinLearningPath {
        try {
            val urlValidationResponse = urlValidationClient.validateUrl(UrlValidationRequest(name, url))
            if (urlValidationResponse.result != "Okay") {
                throw InvalidLearningResourceUrl("Not a valid learning resource URL: $url")
            }
            learningPath.resources[name] = url
            return learningPath
        } catch (r: RuntimeException) {
            throw Exception("Error while validating the learning resource: cause - $r : $url")
        }
    }

    /**
     * Remove a resource from the learning path.
     *
     * @param name The name of the resource to remove
     * @return The updated learning path
     */
    fun removeResource(name: String): KotlinLearningPath {
        learningPath.resources.remove(name)
        return learningPath
    }

    /**
     * Reset the learning path to the default values.
     *
     * @return The reset learning path
     */
    fun resetToDefault(): KotlinLearningPath {
        learningPath =
            KotlinLearningPath(
                name = "Kotlin Learning Resources",
                resources =
                    mutableMapOf(
                        "Kotlin Home" to "https://kotlinlang.org/",
                        "Quarkus Home" to "https://quarkus.io/",
                        "Http4k Home" to "https://www.http4k.org/",
                        "Ktor Home" to "https://ktor.io/",
                    ),
            )
        return learningPath
    }
}
