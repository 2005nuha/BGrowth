package com.example.bgrowth.data.repository

import com.example.bgrowth.data.model.AddExpenseRequest
import com.example.bgrowth.data.model.ExpenseResponse
import com.example.bgrowth.data.remote.RetrofitClient

class ExpenseRepository {
    private val api = RetrofitClient.expenseApi

    suspend fun addExpense(
        businessId: Int,
        amount: Double,
        category: String,
        description: String,
        date: String
    ): Result<ExpenseResponse> {
        return try {
            val request = AddExpenseRequest(
                business = businessId,
                amount = amount,
                category = category,
                description = description,
                expense_date = date
            )

            val response = api.addExpense(request)
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("حدث خطأ أثناء الحفظ: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getExpenses(): Result<List<ExpenseResponse>> {
        return try {
            val response = api.getExpenses()
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("خطأ في جلب البيانات: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

}