package dev.prince.ripplereach.network

import retrofit2.http.GET

interface ApiService {

    @GET("api/users/generate-usernames")
    suspend fun getUsernames(): List<String>

}