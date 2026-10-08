package com.example.bgrowth.data.remote


import com.example.bgrowth.data.model.AddExpenseRequest
import com.example.bgrowth.data.model.ExpenseResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ExpenseApi {

    @GET("api/expenses/") // تأكد من الـ URL الخاص بـ Django
    suspend fun getExpenses(): Response<List<ExpenseResponse>>

    @POST("api/expenses/")
    suspend fun addExpense(
        @Body request: AddExpenseRequest
    ): Response<ExpenseResponse>
}

