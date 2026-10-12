package com.example.bgrowth.data.model

data class RecordPaymentRequest(
    val debt: Int,
    val amount: Double,
    val note: String = ""
)