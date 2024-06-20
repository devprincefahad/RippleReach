package dev.prince.ripplereach.data

import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable

@Serializable
data class University(
    val id: Long,
    val name: String,
    val createdAt: String,
    val updatedAt: String
)