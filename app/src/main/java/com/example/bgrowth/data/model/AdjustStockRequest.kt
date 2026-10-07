package com.example.bgrowth.data.model

import com.google.gson.annotations.SerializedName

data class AdjustStockRequest(
    val quantity: Int,

    @SerializedName("movement_type")
    val movementType: String,

    val reason: String? = null
)