package za.co.ee.learning.resource

import io.quarkus.test.junit.QuarkusTest
import io.restassured.RestAssured.given
import io.restassured.http.ContentType
import org.hamcrest.CoreMatchers.containsString
import org.hamcrest.CoreMatchers.`is`
import org.junit.jupiter.api.Test

@QuarkusTest
class LearningPathResourceTest {
    @Test
    fun testGetLearningPath() {
        given()
            .`when`().get("/learning-path")
            .then()
            .statusCode(200)
            .contentType(ContentType.JSON)
            .body("name", `is`("Kotlin Learning Resources"))
            .body("resources.size()", `is`(4))
            .body("resources.'Kotlin Home'", `is`("https://kotlinlang.org/"))
            .body("resources.'Quarkus Home'", `is`("https://quarkus.io/"))
            .body("resources.'Http4k Home'", `is`("https://www.http4k.org/"))
            .body("resources.'Ktor Home'", `is`("https://ktor.io/"))
    }

    @Test
    fun testUpdateName() {
        val newName = "Updated Learning Path Name"

        given()
            .`when`().put("/learning-path/name/$newName")
            .then()
            .statusCode(200)
            .contentType(ContentType.JSON)
            .body("name", `is`(newName))
            .body("resources.size()", `is`(4))

        // Reset to default for other tests
        given()
            .`when`().post("/learning-path/reset")
            .then()
            .statusCode(200)
    }

    @Test
    fun testAddValidResource() {
        given()
            .queryParam("name", "New Resource Name")
            .queryParam("url", "http://valid.new.resource")
            .`when`().put("/learning-path/resource")
            .then()
            .statusCode(200)
            .contentType(ContentType.JSON)
            .body("resources.'New Resource Name'", `is`("http://valid.new.resource"))

        // Reset to default for other tests
        given()
            .`when`().post("/learning-path/reset")
            .then()
            .statusCode(200)
    }

    @Test
    fun testAddInvalidResource() {
        given()
            .queryParam("name", "New Resource Name")
            .queryParam("url", "http://invalid.new.resource")
            .`when`().put("/learning-path/resource")
            .then()
            .statusCode(400)
            .body(containsString("Not a valid learning resource URL"))
    }

    @Test
    fun testAddResourceWithMissingParameters() {
        // Test with missing name
        given()
            .queryParam("url", "http://valid.new.resource")
            .`when`().put("/learning-path/resource")
            .then()
            .statusCode(400)
            .body(containsString("Both name and url parameters are required"))

        // Test with missing url
        given()
            .queryParam("name", "New Resource Name")
            .`when`().put("/learning-path/resource")
            .then()
            .statusCode(400)
            .body(containsString("Both name and url parameters are required"))
    }

    @Test
    fun testRemoveResource() {
        // First add a resource
        given()
            .queryParam("name", "New Resource Name")
            .queryParam("url", "http://valid.new.resource")
            .`when`().put("/learning-path/resource")
            .then()
            .statusCode(200)

        // Then remove it
        given()
            .`when`().delete("/learning-path/resource/New Resource Name")
            .then()
            .statusCode(200)
            .contentType(ContentType.JSON)
            .body("resources.containsKey('New Resource Name')", `is`(false))

        // Reset to default for other tests
        given()
            .`when`().post("/learning-path/reset")
            .then()
            .statusCode(200)
    }

    @Test
    fun testResetToDefault() {
        // First modify the learning path
        given()
            .`when`().put("/learning-path/name/Modified Name")
            .then()
            .statusCode(200)

        given()
            .queryParam("name", "New Resource Name")
            .queryParam("url", "http://valid.new.resource")
            .`when`().put("/learning-path/resource")
            .then()
            .statusCode(200)

        // Then reset it
        given()
            .`when`().post("/learning-path/reset")
            .then()
            .statusCode(200)
            .contentType(ContentType.JSON)
            .body("name", `is`("Kotlin Learning Resources"))
            .body("resources.size()", `is`(4))
            .body("resources.'Kotlin Home'", `is`("https://kotlinlang.org/"))
            .body("resources.'Quarkus Home'", `is`("https://quarkus.io/"))
            .body("resources.'Http4k Home'", `is`("https://www.http4k.org/"))
            .body("resources.'Ktor Home'", `is`("https://ktor.io/"))
    }

    @Test
    fun testAddResourceWithServerError() {
        given()
            .queryParam("name", "New Resource Name")
            .queryParam("url", "http://server.error.resource")
            .`when`().put("/learning-path/resource")
            .then()
            .statusCode(500)
            .body(containsString("Error while validating the learning resource"))
    }
}
