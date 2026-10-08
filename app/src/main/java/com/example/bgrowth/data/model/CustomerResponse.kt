package com.example.bgrowth.data.model

data class CustomerResponse(
    val id: Int,
    val business: Int,
    val name: String,
    val phone: String?,
    val address: String?,
    val created_at: String?,
    val updated_at: String?
)