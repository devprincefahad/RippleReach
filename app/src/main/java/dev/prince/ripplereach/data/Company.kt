package dev.prince.ripplereach.data

import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable

@Serializable
data class Company(
    val id: Long,
    val name: String,
    val createdAt: String,
    val updatedAt: String
)