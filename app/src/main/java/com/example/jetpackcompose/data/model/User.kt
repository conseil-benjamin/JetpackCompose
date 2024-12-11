package com.example.jetpackcompose.data.model

data class User(
    val id: Int,
    val firstName: String,
    val email: String
)

data class ApiResponse(
    val users: List<User>
)