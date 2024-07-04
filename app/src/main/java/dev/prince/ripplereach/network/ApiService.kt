package dev.prince.ripplereach.network

import dev.prince.ripplereach.data.CategoryResponse
import dev.prince.ripplereach.data.LoginRequestBody
import dev.prince.ripplereach.data.RegisterRequestBody
import dev.prince.ripplereach.data.ResponseData
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
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


}