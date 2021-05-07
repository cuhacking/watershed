package com.cuhacking.watershed.model

import kotlinx.serialization.Serializable

@Serializable
data class User(
    @Transient val uuid: String = "",
    val name: String,
    @Transient val password: String = "",
    val email: String)