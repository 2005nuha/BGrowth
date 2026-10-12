package com.example.bgrowth.data.remote

import com.example.bgrowth.data.model.*
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface DebtApi {

    @GET("api/debts/")
    suspend fun getDebts(): Response<List<DebtResponse>>

    @GET("api/debts/{id}/")
    suspend fun getDebtById(@Path("id") id: Int): Response<DebtResponse>

    @POST("api/debts/")
    suspend fun addDebt(@Body request: AddDebtRequest): Response<DebtResponse>

    @GET("api/debts/{id}/payments/")
    suspend fun getDebtPayments(@Path("id") debtId: Int): Response<List<DebtPaymentResponse>>

    @POST("api/debts/payments/")
    suspend fun recordPayment(@Body request: RecordPaymentRequest): Response<DebtPaymentResponse>
}