package dev.prince.ripplereach.network

import dev.prince.ripplereach.data.Auth
import dev.prince.ripplereach.data.CategoryResponse
import dev.prince.ripplereach.data.Comment
import dev.prince.ripplereach.data.CommentRequest
import dev.prince.ripplereach.data.CommentResponse
import dev.prince.ripplereach.data.Community
import dev.prince.ripplereach.data.CommunityDetailResponse
import dev.prince.ripplereach.data.LoginRequestBody
import dev.prince.ripplereach.data.Post
import dev.prince.ripplereach.data.PostExchangeTokenRequest
import dev.prince.ripplereach.data.PostResponse
import dev.prince.ripplereach.data.RegisterRequestBody
import dev.prince.ripplereach.data.ResponseData
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    @GET("users/generate-usernames")
    suspend fun getUsernames(): List<String>

    @POST("auth/register")
    suspend fun register(
        @Body requestBody: RegisterRequestBody
    ): ResponseData

    @POST("auth/login")
    suspend fun login(
        @Body requestBody: LoginRequestBody
    ): ResponseData

    @GET("categories")
    suspend fun getCategories(
        @Query("limit") limit: Int,
        @Query("offset") offset: Int
    ): CategoryResponse

    @GET("posts")
    suspend fun getPosts(
        @Header("Authorization") authToken: String?,
        @Query("limit") limit: Int,
        @Query("offset") offset: Int,
        @Query("sort_by") sortBy: String,
        @Query("search") searchQuery: String? = null
    ): PostResponse

    @POST("auth/refresh/token")
    suspend fun exchangeToken(
        @Body request: PostExchangeTokenRequest
    ): Auth

    @GET("posts/communities/{communityId}")
    suspend fun getPostsByCommunityId(
        @Header("Authorization") authToken: String?,
        @Path("communityId") communityId: Int,
        @Query("limit") limit: Int = 10,
        @Query("offset") offset: Int = 0,
        @Query("sort_by") sortBy: String = "createdAt,desc",
        @Query("search") searchQuery: String? = "null"
    ): CommunityDetailResponse

    @POST("upvotes/posts/{postId}/users/{userId}")
    suspend fun upvotePost(
        @Header("Authorization") authToken: String?,
        @Path("postId") postId: String,
        @Path("userId") userId: String
    )

    @DELETE("upvotes/posts/{postId}/users/{userId}")
    suspend fun deleteUpvote(
        @Header("Authorization") authToken: String?,
        @Path("postId") postId: String,
        @Path("userId") userId: String
    )

    @GET("posts/{postId}")
    suspend fun getPostById(
        @Header("Authorization") authToken: String?,
        @Path("postId") postId: String
    ): Post

    @GET("comments/posts/{postId}")
    suspend fun getComments(
        @Header("Authorization") authToken: String?,
        @Path("postId") postId: String,
        @Query("limit") limit: Int = 10,
        @Query("offset") offset: Int = 0,
        @Query("sort_by") sortBy: String = "createdAt,desc"
    ): CommentResponse

    @POST("comments")
    suspend fun postComment(
        @Header("Authorization") authToken: String?,
        @Body commentRequest: CommentRequest
    )

    @DELETE("comments/{commentId}")
    suspend fun deleteComment(
        @Header("Authorization") authToken: String?,
        @Path("commentId") commentId: Int
    )

    @PUT("comments/{commentId}")
    suspend fun updateComment(
        @Header("Authorization") authToken: String?,
        @Path("commentId") commentId: Int,
        @Body newContent: String
    )

    @Multipart
    @POST("/posts")
    suspend fun createPost(
        @Part("authorId") authorId: RequestBody,
        @Part("communityId") communityId: RequestBody,
        @Part("content") content: RequestBody,
        @Part("title") title: RequestBody,
        @Part attachments: List<MultipartBody.Part>?,
        @Part("link") link: RequestBody
    ): Post

    @GET("communities/{communityId}")
    suspend fun getCommunityById(
        @Path("communityId") commentId: String,
    ): Community

}