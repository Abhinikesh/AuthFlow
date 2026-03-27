package com.example.authflow.network

import com.example.authflow.models.User
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService {
    @GET("users/{id}")
    suspend fun getUserProfile(@Path("id") userId: String): User

    companion object {
        const val BASE_URL = "https://jsonplaceholder.typicode.com/"
    }
}
