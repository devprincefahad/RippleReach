package dev.prince.ripplereach.data

import com.google.gson.annotations.SerializedName

data class CommunityDetailResponse(
    @SerializedName("community")
    val community: Community,
    @SerializedName("posts")
    val posts: Posts
)

data class Posts(
    @SerializedName("content")
    val content: List<Post>,
    @SerializedName("page")
    val page: Page
)

data class Category(
    @SerializedName("id")
    val id: Int,
    @SerializedName("name")
    val name: String,
    @SerializedName("description")
    val description: String,
    @SerializedName("createdAt")
    val createdAt: String,
    @SerializedName("updatedAt")
    val updatedAt: String,
    @SerializedName("slug")
    val slug: String,
    @SerializedName("communities")
    val communities: List<Community>
)

