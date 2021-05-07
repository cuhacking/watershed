package com.cuhacking.watershed

import io.ktor.http.*
import io.ktor.server.testing.*
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.json.Json
import com.cuhacking.watershed.model.User
import org.testcontainers.junit.jupiter.Testcontainers
import kotlin.test.*

@Testcontainers
class UserTest : AbstractTest() {

    @Test
    fun testCreateUser() = withTestApplication(runApplication(testMain)) {
        val user1 = User("id1", "name1", "password1", "test1@test.com")

        with(handleRequest(HttpMethod.Post, "/user/") {
            addHeader(HttpHeaders.ContentType, ContentType.Application.Json.toString())
            setBody(Json.encodeToString(User.serializer(), user1))
        }) {
            assertEquals(HttpStatusCode.OK, response.status())
        }

        with(handleRequest(HttpMethod.Get, "/user/id1")) {
            assertEquals(HttpStatusCode.OK, response.status())
            val expectedUser1 = user1.copy(password="")
            assertEquals(expectedUser1, Json.decodeFromString(User.serializer(), response.content!!))
        }
    }

    @Test
    fun testCreateMultipleUsers() = withTestApplication(runApplication(testMain)) {
        val user1 = User("id1", "name1", "password1", "test1@test.com")
        val user2 = User("id2", "name1", "password1", "test1@test.com")

        with(handleRequest(HttpMethod.Post, "/user/") {
            addHeader(HttpHeaders.ContentType, ContentType.Application.Json.toString())
            setBody(Json.encodeToString(User.serializer(), user1))
        }) {
            assertEquals(HttpStatusCode.OK, response.status())
        }

        with(handleRequest(HttpMethod.Post, "/user/") {
            addHeader(HttpHeaders.ContentType, ContentType.Application.Json.toString())
            setBody(Json.encodeToString(User.serializer(), user2))
        }) {
            assertEquals(HttpStatusCode.OK, response.status())
        }

        with(handleRequest(HttpMethod.Get, "/user/")) {
            assertEquals(HttpStatusCode.OK, response.status())
            val expectedUser1 = user1.copy(password="")
            val expectedUser2 = user2.copy(password="")
            assertEquals(listOf(expectedUser1, expectedUser2), Json.decodeFromString(ListSerializer(User.serializer()), response.content!!))
        }
    }

    @Test
    fun testDeleteUser() = withTestApplication(runApplication(testMain)) {
        val user1 = User("id1", "name1", "password1", "test1@test.com")
        val user2 = User("id2", "name2", "password2", "test2@test.com")

        // Create users
        with(handleRequest(HttpMethod.Post, "/user/") {
            addHeader(HttpHeaders.ContentType, ContentType.Application.Json.toString())
            setBody(Json.encodeToString(User.serializer(), user1))
        }) {
            assertEquals(HttpStatusCode.OK, response.status())
        }

        with(handleRequest(HttpMethod.Post, "/user/") {
            addHeader(HttpHeaders.ContentType, ContentType.Application.Json.toString())
            setBody(Json.encodeToString(User.serializer(), user2))
        }) {
            assertEquals(HttpStatusCode.OK, response.status())
        }

        // Check get both users
        with(handleRequest(HttpMethod.Get, "/user/")) {
            assertEquals(HttpStatusCode.OK, response.status())
            assertEquals(2, Json.decodeFromString(ListSerializer(User.serializer()), response.content!!).size)
        }

        // Delete user
        with(handleRequest(HttpMethod.Delete, "/user/id1")) {
            assertEquals(HttpStatusCode.OK, response.status())
        }

        // Check only 1 user remains
        with(handleRequest(HttpMethod.Get, "/user/")) {
            assertEquals(HttpStatusCode.OK, response.status())
            assertEquals(1, Json.decodeFromString(ListSerializer(User.serializer()), response.content!!).size)
        }

        with(handleRequest(HttpMethod.Get, "/user/id1")) {
            assertEquals(HttpStatusCode.NotFound, response.status())
        }

        with(handleRequest(HttpMethod.Get, "/user/id2")) {
            assertEquals(HttpStatusCode.OK, response.status())
            val expectedUser2 = user2.copy(password="")
            assertEquals(expectedUser2, Json.decodeFromString(User.serializer(), response.content!!))
        }
    }

}