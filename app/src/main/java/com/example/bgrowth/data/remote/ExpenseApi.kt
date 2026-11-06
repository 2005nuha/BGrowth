package com.example.bgrowth.data.remote

import com.example.bgrowth.data.model.CreateExpenseRequest
import com.example.bgrowth.data.model.ExpenseResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface ExpenseApi {
    @GET("api/business/expenses/")
    suspend fun getExpenses(
        @Query("category") category: String? = null
    ): List<ExpenseResponse>

    @POST("api/business/expenses/")
    suspend fun createExpense(
        @Body request: CreateExpenseRequest
    ): ExpenseResponse

    @GET("api/business/expenses/{id}/")
    suspend fun getExpenseDetail(
        @Path("id") id: Int
    ): ExpenseResponse
}