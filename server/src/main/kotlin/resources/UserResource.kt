package resources

import io.ktor.application.*
import io.ktor.http.*
import io.ktor.response.*
import io.ktor.routing.*
import com.cuhacking.watershed.db.Database
import io.ktor.request.*
import com.cuhacking.watershed.db.Users
import javax.inject.Inject

class UserResource @Inject constructor(private val database: Database) {

    fun Routing.routing() {
        route("/user") {
            get("/") {
                val list = database.usersQueries.getAll().executeAsList();
                call.respond(list)
            }

            post("/") {
                val user = call.receive<Users>()
                database.usersQueries.create(user)
                call.respond(HttpStatusCode.OK)
            }

            get("/{userId}") {
                val userId = call.parameters["userId"]?.toInt() ?: return@get call.respond(HttpStatusCode.NotFound)
                val user = database.usersQueries.get(userId, ::Users).executeAsOneOrNull() ?: return@get call.respond(HttpStatusCode.NotFound)
                call.respond(user)
            }

            delete("/{userId}") {
                val userId = call.parameters["userId"]?.toInt() ?: return@delete call.respond(HttpStatusCode.NotFound)
                database.usersQueries.delete(userId)
                call.respond(HttpStatusCode.OK)
            }
        }
    }

}