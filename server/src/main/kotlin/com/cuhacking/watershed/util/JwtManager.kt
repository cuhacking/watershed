package com.cuhacking.watershed.util

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.cuhacking.watershed.config.Config
import com.cuhacking.watershed.db.Database
import io.ktor.auth.jwt.*
import java.time.Duration
import java.util.*
import javax.inject.Inject

class JwtManager @Inject constructor(private val config: Config, private val database: Database){

    companion object {
        val JWT_AUTH= "jwt-auth"
    }

    val algorithm: Algorithm = Algorithm.HMAC512(config.auth.secret)
    private val tokenExpiry = Duration.ofHours(1)

    fun createJWT(uuid: String) = JWT.create()
        .withSubject("Authentication")
        .withIssuer(config.auth.issuer)
        .withClaim("uuid", uuid)
        .withExpiresAt(getExpiration())
        .sign(algorithm)

    private fun getExpiration() = Date(System.currentTimeMillis() + tokenExpiry.toMillis())

    fun validate(credential: JWTCredential): JWTPrincipal? {
        val userId = credential.payload.getClaim("uuid").asString()
        return if (database.usersQueries.getByUuid(userId).executeAsOneOrNull() != null)
            JWTPrincipal(credential.payload)
        else
            null
    }
}
