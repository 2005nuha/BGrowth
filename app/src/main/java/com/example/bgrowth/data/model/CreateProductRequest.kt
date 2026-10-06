package com.example.bgrowth.data.model

data class CreateProductRequest(
    val category: Int? = null,
    val name: String,
    val description: String = "",
    val selling_price: String,
    val cost_price: String? = null,
    val minimum_stock: Int = 0,
    val image: String = "",
    val initial_quantity: Int = 0
)