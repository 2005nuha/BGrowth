package com.example.bgrowth.data.repository

import com.example.bgrowth.data.model.ReportResponse
import com.example.bgrowth.data.remote.RetrofitClient

class ReportRepository {
    private val api = RetrofitClient.reportApi

    suspend fun getReportSummary(period: String): Result<ReportResponse> {
        return try {
            val response = api.getReportSummary(period)
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Error fetching reports: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}