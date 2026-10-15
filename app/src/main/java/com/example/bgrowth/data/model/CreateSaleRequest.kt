package com.example.bgrowth.data.model

import com.google.gson.annotations.SerializedName

data class CreateSaleRequest(
    val items: List<CreateSaleItemRequest>,
    val customer: Int? = null,

    @SerializedName("paid_amount")
    val paidAmount: String = "0.00"
)

data class CreateSaleItemRequest(
    val product: Int,
    val quantity: Int
)