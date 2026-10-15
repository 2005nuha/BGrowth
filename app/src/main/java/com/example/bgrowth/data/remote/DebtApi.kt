package com.example.bgrowth.data.remote

import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

data class DebtDto(
    val id: Int,
    val debt_type: String, // "RECEIVABLE" or "PAYABLE"
    val party_name: String,
    val customer: Int? = null,
    val sale: Int? = null,
    val original_amount: String,
    val paid_amount: String,
    val remaining_amount: String,
    val due_date: String? = null,
    val status: String, // "UNPAID", "PARTIAL", "PAID"
    val notes: String = ""
)

data class CreateDebtRequest(
    val debt_type: String,
    val party_name: String,
    val original_amount: String,
    val customer: Int? = null,
    val due_date: String? = null,
    val notes: String = ""
)

data class RecordDebtPaymentRequest(
    val amount: String,
    val note: String = ""
)

interface DebtApi {
    @GET("api/business/debts/")
    suspend fun getDebts(
        @Query("debt_type") type: String? = null,
        @Query("status") status: String? = null
    ): List<DebtDto>

    @POST("api/business/debts/")
    suspend fun createDebt(@Body request: CreateDebtRequest): DebtDto

    @GET("api/business/debts/{id}/")
    suspend fun getDebtDetail(@Path("id") id: Int): DebtDto

    @POST("api/business/debts/{id}/payments/")
    suspend fun addPayment(
        @Path("id") id: Int,
        @Body request: RecordDebtPaymentRequest
    ): DebtDto
}