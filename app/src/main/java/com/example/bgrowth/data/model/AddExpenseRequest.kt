package com.example.bgrowth.data.model

data class AddExpenseRequest (
    val business: Int,
    val amount: Double,
    val category: String,
    val description: String,
    val expense_date: String
)