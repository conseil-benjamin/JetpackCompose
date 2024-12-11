package com.example.jetpackcompose.data.model

data class Cart(
    val id: Int,
    val products: List<Product>,
    val total: Int,
    val discountedTotal: Int,
    val totalProducts: Int
)