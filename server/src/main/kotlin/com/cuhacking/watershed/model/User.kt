package com.cuhacking.watershed.model

import kotlinx.serialization.Serializable

@Serializable
data class User(
    val uuid: String,
    val name: String,
    val password: String,
    val email: String)

@Serializable
data class InputUser(
    val uuid: String?,
    val name: String,
    val password: String,
    val email: String
)

@Serializable
data class OutputUser(
    val uuid: String,
    val name: String,
    val email: String
)