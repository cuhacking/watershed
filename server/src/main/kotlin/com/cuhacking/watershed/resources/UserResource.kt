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
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import kotlinx.serialization.SerializationException
import org.mindrot.jbcrypt.BCrypt
import java.util.*
import javax.inject.Inject

private fun InputUser.toSQLDelight() : Users {
    // Generate a UUID and hash their password
    val newUuid = uuid ?: UUID.randomUUID().toString()
    val newPassword = BCrypt.hashpw(password, BCrypt.gensalt())

    return Users(newUuid, name, newPassword, email)
}

class UserResource @Inject constructor(private val database: Database, private val dispatcher: CoroutineDispatcher) {

    fun Routing.routing() {
        route("/user") {
            get("/") {
                withContext(dispatcher) {
                    val list = database.usersQueries.getAll(::OutputUser).executeAsList();
                    call.respond(list)
                }
            }

            post("/") {
                withContext(dispatcher) {
                    try {
                        val user = call.receive<InputUser>()
                        database.usersQueries.create(user.toSQLDelight())
                        call.respond(HttpStatusCode.OK)
                    } catch (e: SerializationException) {
                        call.respond(HttpStatusCode.BadRequest, "Input is invalid")
                    }
                }
            }

            get("/{userId}") {
                return@get withContext(dispatcher) {
                    val userId = call.parameters["userId"] ?: return@withContext call.respond(HttpStatusCode.BadRequest)
                    val user = database.usersQueries.getByUuid(userId, ::OutputUser).executeAsOneOrNull()
                        ?: return@withContext call.respond(HttpStatusCode.NotFound)
                    call.respond(user)
                }
            }

            delete("/{userId}") {
                return@delete withContext(dispatcher) {
                    val userId = call.parameters["userId"] ?: return@withContext call.respond(HttpStatusCode.BadRequest)
                    database.usersQueries.getByUuid(userId, ::OutputUser).executeAsOneOrNull()
                        ?: return@withContext call.respond(HttpStatusCode.NotFound)
                    database.usersQueries.delete(userId)
                    call.respond(HttpStatusCode.OK)
                }
            }
        }
    }

}