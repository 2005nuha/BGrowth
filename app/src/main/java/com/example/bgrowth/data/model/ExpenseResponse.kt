package com.example.bgrowth.data.model

import com.google.gson.annotations.SerializedName

data class ExpenseResponse(
    @SerializedName("id") val id: Int,
    @SerializedName("business") val businessId: Int? = null,
    @SerializedName("amount") val amount: String?,
    @SerializedName("category") val category: String?,
    @SerializedName("description") val description: String?,
    @SerializedName("expense_date") val expenseDate: String?,
    @SerializedName("created_at") val createdAt: String?
)

data class CreateExpenseRequest(
    @SerializedName("amount") val amount: String,
    @SerializedName("category") val category: String,
    @SerializedName("description") val description: String = "",
    @SerializedName("expense_date") val expenseDate: String
)