package com.cuhacking.watershed.server.resources

import com.cuhacking.watershed.db.Database
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import org.mindrot.jbcrypt.BCrypt
import javax.inject.Inject

data class User(val name: String, val email: String, val password: String)

class UserResource @Inject constructor(application: Application, private val database: Database) : Router(application) {

    override fun Routing.setupRoutes() {
        route("/user") {
            // Get all users
            get {
                val list = database.userQueries.getAll().executeAsList()
                call.respond(list)
            }

            // Get a specific user by ID
            get("/{userId}") {
                val userId = call.parameters["userId"]?.toInt() ?: return@get call.respond(HttpStatusCode.BadRequest)
                val user = database.userQueries.get(userId).executeAsOneOrNull() ?: return@get call.respond(
                    HttpStatusCode.NotFound
                )
                call.respond(user)
            }

            // Create a user
            post {
                val body = call.receive<User>()
                val hashedPassword = BCrypt.hashpw(body.password, BCrypt.gensalt())

                database.userQueries.create(body.name, body.email, hashedPassword)
                call.respond(HttpStatusCode.OK)
            }
        }
    }
}
