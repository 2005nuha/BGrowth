package com.example.bgrowth.data.repository

import com.example.bgrowth.data.remote.ProfitLossResponseDto
import com.example.bgrowth.data.remote.ReportApi
import com.example.bgrowth.data.remote.ReportSummaryResponseDto
import com.example.bgrowth.data.remote.RetrofitClient

class ReportRepository(
    private val api: ReportApi = RetrofitClient.reportApi
) {
    suspend fun getProfitLossReport(period: String = "month"): Result<ProfitLossResponseDto> {
        return try {
            val response = api.getProfitLossReport(period)
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Error fetching profit/loss report: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getReportSummary(period: String = "this_month"): Result<ReportSummaryResponseDto> {
        return try {
            val response = api.getReportSummary(period)
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Error fetching report summary: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}