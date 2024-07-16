package dev.prince.ripplereach.data

data class CommentResponse(
    val content: List<Comment>
)

data class Comment(
    val id: Int,
    val content: String,
    val author: User,
    val post: Post,
    val totalUpvotes: Int,
    val createdAt: String,
    val updatedAt: String,
    val upvotedByUser: Boolean
)