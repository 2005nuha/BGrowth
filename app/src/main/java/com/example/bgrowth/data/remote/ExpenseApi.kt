package com.example.bgrowth.data.remote

import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

data class ExpenseDto(
    val id: Int,
    val amount: String,
    val category: String,
    val description: String,
    val expense_date: String,
    val created_at: String
)

data class CreateExpenseRequest(
    val amount: String,
    val category: String,
    val description: String = "",
    val expense_date: String
)

interface ExpenseApi {
    @GET("api/business/expenses/")
    suspend fun getExpenses(
        @Query("category") category: String? = null
    ): List<ExpenseDto>

    @POST("api/business/expenses/")
    suspend fun createExpense(@Body request: CreateExpenseRequest): ExpenseDto

    @GET("api/business/expenses/{id}/")
    suspend fun getExpenseDetail(@Path("id") id: Int): ExpenseDto
}