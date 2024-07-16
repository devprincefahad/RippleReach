package dev.prince.ripplereach.data

data class CommentRequest(
    val content: String,
    val postId: Int,
    val userId: Int
)