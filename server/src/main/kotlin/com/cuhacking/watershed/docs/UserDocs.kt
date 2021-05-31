package com.cuhacking.watershed.docs

import com.cuhacking.watershed.model.User
import io.bkbn.kompendium.models.meta.MethodInfo
import io.bkbn.kompendium.models.meta.ResponseInfo
import io.ktor.http.*

object UserDocs {

    val getUserById = MethodInfo.GetInfo<Unit, User>(
        summary = "Get User by ID",
        description = "Gets a specific user by their UUID",
        responseInfo = ResponseInfo(
            status = HttpStatusCode.OK,
            description = "Success",
            examples = mapOf("response" to User("uuid", "name", "password", "email"))
        ),
        canThrow = setOf(Exception::class)
    )

}