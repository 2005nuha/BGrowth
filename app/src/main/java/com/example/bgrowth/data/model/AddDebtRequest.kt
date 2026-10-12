package com.example.bgrowth.data.model

data class AddDebtRequest(
    val business: Int = 1,
    val debt_type: String = "RECEIVABLE",
    val party_name: String,
    val customer: Int? = null,
    val sale: Int? = null,
    val original_amount: Double,
    val due_date: String? = null,
    val notes: String = ""
)