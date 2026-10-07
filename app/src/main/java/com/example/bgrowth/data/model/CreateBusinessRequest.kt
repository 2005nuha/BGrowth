package com.example.bgrowth.data.model

import com.google.gson.annotations.SerializedName

data class CreateBusinessRequest(
    val name: String,

    @SerializedName("business_type")
    val businessType: String,

    val currency: String,

    val phone: String = "",

    val address: String = ""
)