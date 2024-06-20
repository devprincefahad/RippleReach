package dev.prince.ripplereach.data

import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable

@Serializable
data class Auth(
    val token: String,
    val refreshToken: String,
    val expiresAt: String,
    val username: String
)