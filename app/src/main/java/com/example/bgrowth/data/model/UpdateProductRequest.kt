package com.example.bgrowth.data.model

import com.google.gson.annotations.SerializedName

data class UpdateProductRequest(
    val name: String? = null,

    @SerializedName("selling_price")
    val sellingPrice: String? = null,

    val category: Int? = null,

    val description: String? = null,

    @SerializedName("cost_price")
    val costPrice: String? = null,

    @SerializedName("minimum_stock")
    val minimumStock: Int? = null
)