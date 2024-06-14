package dev.prince.ripplereach.data

import java.util.Date

data class Company(
    val id: Long,
    val name: String,
    val createdAt: Date,
    val updatedAt: Date
)