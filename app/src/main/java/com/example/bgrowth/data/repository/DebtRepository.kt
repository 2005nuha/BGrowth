package com.example.bgrowth.data.repository

import com.example.bgrowth.data.model.*
import com.example.bgrowth.data.remote.RetrofitClient

class DebtRepository {
    private val api = RetrofitClient.debtApi

    suspend fun getDebts(): Result<List<DebtResponse>> {
        return try {
            val response = api.getDebts()
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Error fetching debts: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun addDebt(request: AddDebtRequest): Result<DebtResponse> {
        return try {
            val response = api.addDebt(request)
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Error adding debt: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun recordPayment(request: RecordPaymentRequest): Result<DebtPaymentResponse> {
        return try {
            val response = api.recordPayment(request)
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Error recording payment: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}