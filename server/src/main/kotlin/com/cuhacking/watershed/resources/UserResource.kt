package com.cuhacking.watershed.resources

import io.ktor.application.*
import io.ktor.http.*
import io.ktor.response.*
import io.ktor.routing.*
import com.cuhacking.watershed.db.Database
import io.ktor.request.*
import com.cuhacking.watershed.db.Users
import com.cuhacking.watershed.model.User
import org.mindrot.jbcrypt.BCrypt
import java.util.*
import javax.inject.Inject

private fun User.toSQLDelight() : Users {
    return Users(uuid, name, password, email)
}

class UserResource @Inject constructor(private val database: Database) {

    fun Routing.routing() {
        route("/user") {
            get("/") {
                val list = database.usersQueries.getAll{ uuid: String, name: String, email: String ->
                    User(uuid=uuid, name=name, email=email)
                }.executeAsList();
                call.respond(list)
            }

            post("/") {
                val user = call.receive<User>()

                // Generate a UUID and hash their password
                val uuid = user.uuid.ifBlank { UUID.randomUUID().toString() }
                val password = BCrypt.hashpw(user.password, BCrypt.gensalt())

                val userToSave = user.copy(uuid=uuid, password=password)
                database.usersQueries.create(userToSave.toSQLDelight())
                call.respond(HttpStatusCode.OK)
            }

            get("/{userId}") {
                val userId = call.parameters["userId"] ?: return@get call.respond(HttpStatusCode.NotFound)
                val user = database.usersQueries.get(userId) { uuid: String, name: String, email: String ->
                    User(uuid = uuid, name = name, email = email)
                }.executeAsOneOrNull() ?: return@get call.respond(HttpStatusCode.NotFound)
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