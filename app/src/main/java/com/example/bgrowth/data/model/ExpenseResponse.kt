package com.example.bgrowth.data.model

data class ExpenseResponse(
    val id: Int,
    val business: Int,
    val amount: String, // Django برجع DecimalField كـ String
    val category: String,
    val description: String?,
    val expense_date: String,
    val created_at: String
)