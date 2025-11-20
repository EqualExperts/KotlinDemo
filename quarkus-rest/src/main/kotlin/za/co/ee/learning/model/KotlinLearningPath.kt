package za.co.ee.learning.model

/**
 * Represents a learning path for Kotlin with a name and a collection of learning resources.
 *
 * @property name The name of the learning path
 * @property resources A map of resource names to their URLs
 */
data class KotlinLearningPath(
    var name: String,
    var resources: MutableMap<String, String> = mutableMapOf(),
)
