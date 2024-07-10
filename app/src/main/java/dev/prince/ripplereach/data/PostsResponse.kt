package dev.prince.ripplereach.data

import com.google.gson.annotations.SerializedName

data class PostResponse(
    @SerializedName("content") val content: List<Post>,
    @SerializedName("page") val page: Page
)

data class Post(
    @SerializedName("id") val id: Int,
    @SerializedName("title") val title: String,
    @SerializedName("content") val content: String,
    @SerializedName("author") val author: Author,
    @SerializedName("community") val postCommunity: Community,
    @SerializedName("attachments") val attachments: List<Any>,
    @SerializedName("totalUpvotes") val totalUpvotes: Int,
    @SerializedName("totalComments") val totalComments: Int,
    @SerializedName("link") val link: String?,
    @SerializedName("createdAt") val createdAt: String,
    @SerializedName("updatedAt") val updatedAt: String
)

data class Author(
    @SerializedName("userId") val userId: Int,
    @SerializedName("username") val username: String,
    @SerializedName("phone") val phone: String,
    @SerializedName("university") val university: String?,
    @SerializedName("company") val company: Company,
    @SerializedName("profession") val profession: String,
    @SerializedName("isVerified") val isVerified: Boolean,
    @SerializedName("avatar") val avatar: String,
    @SerializedName("roles") val roles: List<Role>,
    @SerializedName("createdAt") val createdAt: String,
    @SerializedName("updatedAt") val updatedAt: String,
    @SerializedName("deletedAt") val deletedAt: String?
)
//
//data class PostCompany(
//    @SerializedName("id") val id: Int,
//    @SerializedName("name") val name: String,
//    @SerializedName("createdAt") val createdAt: String,
//    @SerializedName("updatedAt") val updatedAt: String
//)

data class Role(
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String
)

data class Community(
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String,
    @SerializedName("description") val description: String,
    @SerializedName("imageUrl") val imageUrl: String,
    @SerializedName("createdAt") val createdAt: String,
    @SerializedName("updatedAt") val updatedAt: String,
    @SerializedName("category") val category: Category?,
    @SerializedName("slug") val slug: String
)

//data class Category(
//    @SerializedName("id") val id: Int,
//    @SerializedName("name") val name: String,
//    @SerializedName("description") val description: String,
//    @SerializedName("createdAt") val createdAt: String,
//    @SerializedName("updatedAt") val updatedAt: String,
//    @SerializedName("slug") val slug: String,
//    @SerializedName("communities") val communities: List<Community>
//)

data class Page(
    @SerializedName("size") val size: Int,
    @SerializedName("number") val number: Int,
    @SerializedName("totalElements") val totalElements: Int,
    @SerializedName("totalPages") val totalPages: Int
)
