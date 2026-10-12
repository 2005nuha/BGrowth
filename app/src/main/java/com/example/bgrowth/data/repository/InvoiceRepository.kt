package com.example.bgrowth.data.repository

import com.example.bgrowth.data.model.InvoiceResponse
import com.example.bgrowth.data.remote.RetrofitClient

class InvoiceRepository {
    private val api = RetrofitClient.invoiceApi

    suspend fun getInvoices(): Result<List<InvoiceResponse>> {
        return try {
            val response = api.getInvoices()
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Error fetching invoices: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getInvoiceById(id: Int): Result<InvoiceResponse> {
        return try {
            val response = api.getInvoiceById(id)
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Error fetching invoice: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun cancelInvoice(id: Int): Result<InvoiceResponse> {
        return try {
            val response = api.cancelInvoice(id)
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Error cancelling invoice: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}