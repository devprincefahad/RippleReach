package dev.prince.ripplereach.data

import kotlinx.serialization.Serializable

@Serializable
data class ResponseData(
    val message: String,
    val user: User,
    val auth: Auth
)