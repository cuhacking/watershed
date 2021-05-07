package com.cuhacking.watershed.resources

import io.ktor.application.*
import io.ktor.http.*
import io.ktor.response.*
import io.ktor.routing.*
import com.cuhacking.watershed.db.Database
import io.ktor.request.*
import com.cuhacking.watershed.db.Users
import com.cuhacking.watershed.model.InputUser
import com.cuhacking.watershed.model.OutputUser
import com.cuhacking.watershed.model.User
import org.mindrot.jbcrypt.BCrypt
import java.util.*
import javax.inject.Inject

private fun InputUser.toSQLDelight() : Users {
    // Generate a UUID and hash their password
    val newUuid = uuid ?: UUID.randomUUID().toString()
    val newPassword = BCrypt.hashpw(password, BCrypt.gensalt())

    return Users(newUuid, name, newPassword, email)
}

class UserResource @Inject constructor(private val database: Database) {

    fun Routing.routing() {
        route("/user") {
            get("/") {
                val list = database.usersQueries.getAll(::OutputUser).executeAsList();
                call.respond(list)
            }

            post("/") {
                val user = call.receive<InputUser>()
                database.usersQueries.create(user.toSQLDelight())
                call.respond(HttpStatusCode.OK)
            }

            get("/{userId}") {
                val userId = call.parameters["userId"] ?: return@get call.respond(HttpStatusCode.NotFound)
                val user = database.usersQueries.get(userId, ::OutputUser).executeAsOneOrNull() ?: return@get call.respond(HttpStatusCode.NotFound)
                call.respond(user)
            }

            delete("/{userId}") {
                val userId = call.parameters["userId"] ?: return@delete call.respond(HttpStatusCode.NotFound)
                database.usersQueries.delete(userId)
                call.respond(HttpStatusCode.OK)
            }
        }
    }

}