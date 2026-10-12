package com.example.bgrowth.data.model

data class DebtResponse(
    val id: Int,
    val business: Int,
    val debt_type: String,
    val party_name: String,
    val customer: Int?,
    val sale: Int?,
    val original_amount: String,
    val paid_amount: String,
    val remaining_amount: String?,
    val due_date: String?,
    val status: String,
    val notes: String?,
    val created_at: String?,
    val updated_at: String?
)