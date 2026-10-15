package com.example.bgrowth.data.repository

import com.example.bgrowth.data.remote.CreateExpenseRequest
import com.example.bgrowth.data.remote.ExpenseApi
import com.example.bgrowth.data.remote.ExpenseDto
import com.example.bgrowth.data.remote.RetrofitClient

class ExpenseRepository(
    private val api: ExpenseApi = RetrofitClient.expenseApi
) {
    suspend fun getExpenses(category: String? = null): Result<List<ExpenseDto>> {
        return runCatching {
            api.getExpenses(category)
        }
    }

    suspend fun createExpense(
        amount: String,
        category: String,
        description: String = "",
        date: String
    ): Result<ExpenseDto> {
        return runCatching {
            api.createExpense(
                CreateExpenseRequest(
                    amount = amount,
                    category = category,
                    description = description,
                    expense_date = date
                )
            )
        }
    }

    suspend fun getExpenseDetail(id: Int): Result<ExpenseDto> {
        return runCatching {
            api.getExpenseDetail(id)
        }
    }
}