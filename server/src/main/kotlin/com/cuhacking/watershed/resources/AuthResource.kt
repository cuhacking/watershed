package com.cuhacking.watershed.resources

import com.cuhacking.watershed.db.Database
import com.cuhacking.watershed.util.JwtManager
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import kotlinx.serialization.Serializable
import org.mindrot.jbcrypt.BCrypt
import javax.inject.Inject

@Serializable
data class InputCredentials(val email: String, val password: String)

class AuthResource @Inject constructor(private val database: Database,
                                       private val dispatcher: CoroutineDispatcher,
                                       private val jwtManager: JwtManager) {
    fun Routing.routing() {
        route("/login") {
            post("/") {
                withContext(dispatcher) {
                    val credentials = call.receive<InputCredentials>()
                    val user = database.usersQueries.getByEmail(credentials.email).executeAsOneOrNull()
                        ?: return@withContext call.respond(HttpStatusCode.Forbidden)

                    if (!BCrypt.checkpw(credentials.password, user.password))
                        return@withContext call.respond(HttpStatusCode.Forbidden)

                    call.respond(HttpStatusCode.OK, jwtManager.createJWT(user.uuid))
                }
            }
        }

        route("/private") {
            authenticate("auth-jwt") {
                get("/") {
                    call.respond("Authenticated!")
                }
            }

        }
    }
}