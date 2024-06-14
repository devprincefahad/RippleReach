package dev.prince.ripplereach.data

data class RegisterResponse(
    val message: String,
    val user: User,
    val auth: Auth
)