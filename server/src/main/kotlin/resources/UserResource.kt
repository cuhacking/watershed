package resources

import io.ktor.application.*
import io.ktor.http.*
import io.ktor.response.*
import io.ktor.routing.*
import com.cuhacking.watershed.db.Database
import io.ktor.request.*
import com.cuhacking.watershed.db.User
import javax.inject.Inject

class UserResource @Inject constructor(private val database: Database) {

    fun Routing.routing() {
        route("/user") {
            get("/") {
                val list = database.userQueries.getAll().executeAsList();
                call.respond(list)
            }

            post("/") {
                val user = call.receive<User>()
                database.userQueries.create(user)
            }

            get("/{userId}") {
                val userId = call.parameters["userId"]?.toInt() ?: return@get call.respond(HttpStatusCode.NotFound)
                val user = database.userQueries.get(userId, ::User).executeAsOneOrNull() ?: return@get call.respond(HttpStatusCode.NotFound)
                call.respond(user)
            }
        }
    }

}