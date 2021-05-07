package com.cuhacking.watershed

import com.cuhacking.watershed.model.InputUser
import com.cuhacking.watershed.model.OutputUser
import io.ktor.http.*
import io.ktor.server.testing.*
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.json.Json
import com.cuhacking.watershed.model.User
import org.testcontainers.junit.jupiter.Testcontainers
import kotlin.test.*

fun InputUser.toOutputUser(): OutputUser {
    return OutputUser(uuid!!, name, email)
}

@Testcontainers
class UserTest : AbstractTest() {

    @Test
    fun testCreateUser() = withTestApplication(runApplication(testMain)) {
        val user1 = InputUser("id1", "name1", "password1", "test1@test.com")

        with(handleRequest(HttpMethod.Post, "/user/") {
            addHeader(HttpHeaders.ContentType, ContentType.Application.Json.toString())
            setBody(Json.encodeToString(InputUser.serializer(), user1))
        }) {
            assertEquals(HttpStatusCode.OK, response.status())
        }

        with(handleRequest(HttpMethod.Get, "/user/id1")) {
            assertEquals(HttpStatusCode.OK, response.status())
            assertEquals(user1.toOutputUser(), Json.decodeFromString(OutputUser.serializer(), response.content!!))
        }
    }

    @Test
    fun testCreateMultipleUsers() = withTestApplication(runApplication(testMain)) {
        val user1 = InputUser("id1", "name1", "password1", "test1@test.com")
        val user2 = InputUser("id2", "name1", "password1", "test1@test.com")

        with(handleRequest(HttpMethod.Post, "/user/") {
            addHeader(HttpHeaders.ContentType, ContentType.Application.Json.toString())
            setBody(Json.encodeToString(InputUser.serializer(), user1))
        }) {
            assertEquals(HttpStatusCode.OK, response.status())
        }

        with(handleRequest(HttpMethod.Post, "/user/") {
            addHeader(HttpHeaders.ContentType, ContentType.Application.Json.toString())
            setBody(Json.encodeToString(InputUser.serializer(), user2))
        }) {
            assertEquals(HttpStatusCode.OK, response.status())
        }

        with(handleRequest(HttpMethod.Get, "/user/")) {
            assertEquals(HttpStatusCode.OK, response.status())
            val expectedUser1 = user1.toOutputUser()
            val expectedUser2 = user2.toOutputUser()
            assertEquals(listOf(expectedUser1, expectedUser2), Json.decodeFromString(ListSerializer(OutputUser.serializer()), response.content!!))
        }
    }

    @Test
    fun testDeleteUser() = withTestApplication(runApplication(testMain)) {
        val user1 = InputUser("id1", "name1", "password1", "test1@test.com")
        val user2 = InputUser("id2", "name2", "password2", "test2@test.com")

        // Create users
        with(handleRequest(HttpMethod.Post, "/user/") {
            addHeader(HttpHeaders.ContentType, ContentType.Application.Json.toString())
            setBody(Json.encodeToString(InputUser.serializer(), user1))
        }) {
            assertEquals(HttpStatusCode.OK, response.status())
        }

        with(handleRequest(HttpMethod.Post, "/user/") {
            addHeader(HttpHeaders.ContentType, ContentType.Application.Json.toString())
            setBody(Json.encodeToString(InputUser.serializer(), user2))
        }) {
            assertEquals(HttpStatusCode.OK, response.status())
        }

        // Check get both users
        with(handleRequest(HttpMethod.Get, "/user/")) {
            assertEquals(HttpStatusCode.OK, response.status())
            assertEquals(2, Json.decodeFromString(ListSerializer(OutputUser.serializer()), response.content!!).size)
        }

        // Delete user
        with(handleRequest(HttpMethod.Delete, "/user/id1")) {
            assertEquals(HttpStatusCode.OK, response.status())
        }

        // Check only 1 user remains
        with(handleRequest(HttpMethod.Get, "/user/")) {
            assertEquals(HttpStatusCode.OK, response.status())
            assertEquals(1, Json.decodeFromString(ListSerializer(OutputUser.serializer()), response.content!!).size)
        }

        with(handleRequest(HttpMethod.Get, "/user/id1")) {
            assertEquals(HttpStatusCode.NotFound, response.status())
        }

        with(handleRequest(HttpMethod.Get, "/user/id2")) {
            assertEquals(HttpStatusCode.OK, response.status())
            assertEquals(user2.toOutputUser(), Json.decodeFromString(OutputUser.serializer(), response.content!!))
        }
    }

}