package dev.prince.ripplereach.data

data class RegisterRequestBody(
    val idToken: String,
    val phone: String,
    val username: String,
    val company: String?,
    val university: String?,
    val profession: String?
)
