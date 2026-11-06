package com.example.bgrowth.data.repository

import com.example.bgrowth.data.model.CreateExpenseRequest
import com.example.bgrowth.data.model.ExpenseResponse
import com.example.bgrowth.data.remote.ExpenseApi
import com.example.bgrowth.data.remote.RetrofitClient
import com.example.bgrowth.ui.expenses.ExpenseItem

class ExpenseRepository(
    private val api: ExpenseApi = RetrofitClient.expenseApi
) {
    suspend fun fetchExpenses(category: String? = null): Result<List<ExpenseItem>> {
        return runCatching {
            val responseList = api.getExpenses(category)
            responseList.map { dto ->
                ExpenseItem(
                    id = dto.id,
                    category = dto.category ?: "General",
                    description = if (!dto.description.isNullOrBlank()) dto.description else "No description",
                    amount = dto.amount?.let { "-$$it" } ?: "-$0.00",
                    date = dto.expenseDate ?: dto.createdAt ?: ""
                )
            }
        }
    }

    suspend fun createExpense(
        amount: String,
        category: String,
        description: String = "",
        date: String
    ): Result<ExpenseResponse> {
        return runCatching {
            api.createExpense(
                CreateExpenseRequest(
                    amount = amount,
                    category = category,
                    description = description,
                    expenseDate = date
                )
            )
        }
    }
}