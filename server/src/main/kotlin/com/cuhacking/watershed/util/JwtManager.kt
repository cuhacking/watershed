package com.cuhacking.watershed.util

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.cuhacking.watershed.config.Config
import com.cuhacking.watershed.db.Database
import io.ktor.auth.jwt.*
import java.util.*
import javax.inject.Inject

class JwtManager @Inject constructor(private val config: Config, private val database: Database){

    val algorithm: Algorithm = Algorithm.HMAC512(config.auth.secret)
    private val TOKEN_EXPIRY = 60*60*1000 // 1 hour

    fun createJWT(uuid: String) = JWT.create()
        .withSubject("Authentication")
        .withIssuer(config.auth.issuer)
        .withClaim("uuid", uuid)
        .withExpiresAt(getExpiration())
        .sign(algorithm)

    private fun getExpiration() = Date(System.currentTimeMillis() + TOKEN_EXPIRY)

    fun validate(credential: JWTCredential): JWTPrincipal? {
        val userId = credential.payload.getClaim("uuid").asString()
        return if (database.usersQueries.getByUuid(userId).executeAsOneOrNull() != null)
            JWTPrincipal(credential.payload)
        else
            null
    }
}