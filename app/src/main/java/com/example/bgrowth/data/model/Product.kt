package com.example.bgrowth.data.model

import com.google.gson.annotations.SerializedName

data class Product(
    val id: Int,
    val category: Int?,
    val name: String,
    val description: String,

    @SerializedName("selling_price")
    val sellingPrice: String,

    @SerializedName("cost_price")
    val costPrice: String?,

    val quantity: Int,

    @SerializedName("minimum_stock")
    val minimumStock: Int,

    @SerializedName("created_at")
    val createdAt: String,

    @SerializedName("updated_at")
    val updatedAt: String
)