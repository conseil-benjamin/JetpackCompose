package com.example.jetpackcompose.data.repository

import com.example.jetpackcompose.data.model.ApiResponse
import com.example.jetpackcompose.data.model.User
import com.example.jetpackcompose.data.network.ApiService
import com.example.jetpackcompose.data.network.RetrofitClient

class UserRepository(private val apiService: ApiService = RetrofitClient.apiService) {
    suspend fun getUsers(): ApiResponse {
        return apiService.getUsers()
    }
}
