package za.co.ee.learning.service

import io.mockk.every
import io.mockk.verify
import io.quarkiverse.test.junit.mockk.InjectMock
import io.quarkus.test.junit.QuarkusTest
import jakarta.inject.Inject
import org.eclipse.microprofile.rest.client.inject.RestClient
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import za.co.ee.learning.client.UrlValidationRequest
import za.co.ee.learning.client.UrlValidationResponse
import za.co.ee.learning.client.UrlValidatorClient

@QuarkusTest
class LearningPathServiceTest {
    @Inject
    private lateinit var learningPathService: LearningPathService

    @InjectMock
    @RestClient
    private lateinit var urlValidatorClient: UrlValidatorClient

    @BeforeEach
    fun setup() {
        // Reset to default before each test to ensure a clean state
        learningPathService.resetToDefault()
    }

    @Test
    fun testGetLearningPath() {
        val learningPath = learningPathService.getLearningPath()

        assertEquals("Kotlin Learning Resources", learningPath.name)
        assertEquals(4, learningPath.resources.size)
        assertEquals("https://kotlinlang.org/", learningPath.resources["Kotlin Home"])
        assertEquals("https://quarkus.io/", learningPath.resources["Quarkus Home"])
        assertEquals("https://www.http4k.org/", learningPath.resources["Http4k Home"])
        assertEquals("https://ktor.io/", learningPath.resources["Ktor Home"])
    }

    @Test
    fun testUpdateName() {
        val newName = "Updated Learning Path"
        val updatedPath = learningPathService.updateName(newName)

        assertEquals(newName, updatedPath.name)
        assertEquals(4, updatedPath.resources.size)
    }

    @Test
    fun testAddOrUpdateResourceSuccess() {
        // Mock the URL validator to return a successful response
        val resourceName = "Test Resource"
        val resourceUrl = "https://test.resource.com"

        every {
            urlValidatorClient.validateUrl(UrlValidationRequest(resourceName, resourceUrl))
        } returns UrlValidationResponse(resourceName, "Okay")

        val updatedPath = learningPathService.addOrUpdateResource(resourceName, resourceUrl)

        assertEquals(5, updatedPath.resources.size)
        assertEquals(resourceUrl, updatedPath.resources[resourceName])

        // Verify the mock was called with the correct parameters
        verify {
            urlValidatorClient.validateUrl(UrlValidationRequest(resourceName, resourceUrl))
        }
    }

    @Test
    fun testAddOrUpdateResourceInvalidUrl() {
        // Mock the URL validator to return a failure response
        val resourceName = "Invalid Resource"
        val resourceUrl = "invalid-url"

        every {
            urlValidatorClient.validateUrl(UrlValidationRequest(resourceName, resourceUrl))
        } returns UrlValidationResponse(resourceName, "Invalid URL")

        // The service should throw an InvalidLearningResourceUrl exception
        val exception =
            assertThrows(InvalidLearningResourceUrl::class.java) {
                learningPathService.addOrUpdateResource(resourceName, resourceUrl)
            }

        assertTrue(exception.message!!.contains("Not a valid learning resource URL"))

        // Verify the mock was called
        verify {
            urlValidatorClient.validateUrl(UrlValidationRequest(resourceName, resourceUrl))
        }
    }

    @Test
    fun testAddOrUpdateResourceServerError() {
        // Mock the URL validator to throw a RuntimeException
        val resourceName = "Error Resource"
        val resourceUrl = "http://error.resource"

        every {
            urlValidatorClient.validateUrl(UrlValidationRequest(resourceName, resourceUrl))
        } throws RuntimeException("Server error")

        // The service should wrap the RuntimeException in a general Exception
        val exception =
            assertThrows(Exception::class.java) {
                learningPathService.addOrUpdateResource(resourceName, resourceUrl)
            }

        assertTrue(exception.message!!.contains("Error while validating the learning resource"))

        // Verify the mock was called
        verify {
            urlValidatorClient.validateUrl(UrlValidationRequest(resourceName, resourceUrl))
        }
    }

    @Test
    fun testRemoveResource() {
        // First add a resource
        val resourceName = "Test Resource"
        val resourceUrl = "https://test.resource.com"

        every {
            urlValidatorClient.validateUrl(UrlValidationRequest(resourceName, resourceUrl))
        } returns UrlValidationResponse(resourceName, "Okay")

        learningPathService.addOrUpdateResource(resourceName, resourceUrl)

        // Then remove it
        val updatedPath = learningPathService.removeResource(resourceName)

        assertEquals(4, updatedPath.resources.size)
        assertNull(updatedPath.resources[resourceName])
    }

    @Test
    fun testResetToDefault() {
        // First modify the learning path
        learningPathService.updateName("Modified Name")

        val resourceName = "Test Resource"
        val resourceUrl = "https://test.resource.com"

        every {
            urlValidatorClient.validateUrl(UrlValidationRequest(resourceName, resourceUrl))
        } returns UrlValidationResponse(resourceName, "Okay")

        learningPathService.addOrUpdateResource(resourceName, resourceUrl)

        // Then reset it
        val resetPath = learningPathService.resetToDefault()

        assertEquals("Kotlin Learning Resources", resetPath.name)
        assertEquals(4, resetPath.resources.size)
        assertEquals("https://kotlinlang.org/", resetPath.resources["Kotlin Home"])
        assertEquals("https://quarkus.io/", resetPath.resources["Quarkus Home"])
        assertEquals("https://www.http4k.org/", resetPath.resources["Http4k Home"])
        assertEquals("https://ktor.io/", resetPath.resources["Ktor Home"])
        assertNull(resetPath.resources[resourceName])
    }
}
