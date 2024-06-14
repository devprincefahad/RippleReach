package dev.prince.ripplereach.network

import dev.prince.ripplereach.data.RegisterRequestBody
import dev.prince.ripplereach.data.RegisterResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiService {

    @GET("users/generate-usernames")
    suspend fun getUsernames(): List<String>

    @POST("auth/register")
    suspend fun register(
        @Body requestBody: RegisterRequestBody
    ): RegisterResponse
}