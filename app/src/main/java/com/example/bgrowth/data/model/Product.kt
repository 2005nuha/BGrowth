package com.example.bgrowth.data.model

data class Product(
    val id: Int,
    val category: Int?,
    val name: String,
    val description: String,
    val selling_price: String,
    val cost_price: String?,
    val quantity: Int,
    val minimum_stock: Int,
    val image: String,
    val created_at: String,
    val updated_at: String
)