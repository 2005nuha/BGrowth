package com.example.bgrowth.data.model

data class AddCustomerRequest(
    val business: Int,
    val name: String,
    val phone: String = "",
    val address: String = ""
)