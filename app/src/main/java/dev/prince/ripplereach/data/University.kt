package dev.prince.ripplereach.data

import java.util.Date

data class University(
    val id: Long,
    val name: String,
    val createdAt: Date,
    val updatedAt: Date
)