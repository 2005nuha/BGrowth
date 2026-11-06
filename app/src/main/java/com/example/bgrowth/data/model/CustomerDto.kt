package com.example.bgrowth.data.model

data class CustomerDto(
    val id: Int,
    val name: String,
    val phone: String,
    val address: String,
    val created_at: String
)

data class CreateCustomerRequest(
    val name: String,
    val phone: String = "",
    val address: String = ""
)