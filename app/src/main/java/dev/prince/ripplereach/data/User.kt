package dev.prince.ripplereach.data

import kotlinx.serialization.Serializable

@Serializable
data class User(
    val userId: Long,
    val username: String,
    val phone: String,
    val university: University,
    val company: Company,
    val profession: String,
    val isVerified: Boolean,
    val avatar: String,
    val createdAt: String,
    val updatedAt: String,
    val deletedAt: String?
)