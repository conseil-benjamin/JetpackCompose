package com.example.jetpackcompose.data.network

import com.example.jetpackcompose.data.model.ApiResponse
import com.example.jetpackcompose.data.model.Cart
import retrofit2.Call
import com.example.jetpackcompose.data.model.User
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService {
    @GET("users")
    suspend fun getUsers(): ApiResponse

    @GET("carts/{cartId}")
    suspend fun getCart(@Path("cartId") cartId: String): Response<Cart>
}