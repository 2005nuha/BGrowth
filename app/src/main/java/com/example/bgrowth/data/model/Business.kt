package com.example.bgrowth.data.model

import com.google.gson.annotations.SerializedName

data class Business(
    val id: Int,

    val name: String,

    @SerializedName("business_type")
    val businessType: String,

    val currency: String,

    val phone: String,

    val address: String,

    @SerializedName("created_at")
    val createdAt: String,

    @SerializedName("updated_at")
    val updatedAt: String
)