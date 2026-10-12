package com.example.bgrowth.data.model

data class DebtPaymentResponse(
    val id: Int,
    val debt: Int,
    val amount: String,
    val note: String?,
    val created_at: String?
)