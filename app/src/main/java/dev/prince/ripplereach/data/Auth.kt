package dev.prince.ripplereach.data

import java.util.Date

data class Auth(
    val token: String,
    val refreshToken: String,
    val expiresAt: Date,
    val username: String
)