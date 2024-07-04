package dev.prince.ripplereach.data

import com.google.gson.annotations.SerializedName


data class CategoryResponse(
    @SerializedName("content")
    val content: List<CategoryContent>,
    @SerializedName("page")
    val page: Page
)

data class CategoryContent(
    @SerializedName("category")
    val category: Category
)

data class Category(
    @SerializedName("id")
    val id: Int,
    @SerializedName("name")
    val name: String,
    @SerializedName("description")
    val description: String,
    @SerializedName("communities")
    val communities: List<Community>
)

data class Community(
    @SerializedName("id")
    val id: Int,
    @SerializedName("name")
    val name: String,
    @SerializedName("description")
    val description: String,
    @SerializedName("imageUrl")
    val imageUrl: String
)

data class Page(
    @SerializedName("size")
    val size: Int,
    @SerializedName("number")
    val number: Int,
    @SerializedName("totalElements")
    val totalElements: Int,
    @SerializedName("totalPages")
    val totalPages: Int
)