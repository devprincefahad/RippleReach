package dev.prince.ripplereach.data

import java.util.Date

data class User(
    val userId: Long,
    val username: String,
    val phone: String,
    val university: University,
    val company: Company,
    val profession: String,
    val isVerified: Boolean,
    val avatar: String,
    val createdAt: Date,
    val updatedAt: Date,
    val deletedAt: Date?
)