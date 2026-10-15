package com.example.bgrowth.data.remote

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

data class ProfitLossResponseDto(
    val total_revenue: String = "0.00",
    val total_cogs: String = "0.00",
    val gross_profit: String = "0.00",
    val total_expenses: String = "0.00",
    val net_profit: String = "0.00",
    val period: String = "month"
)

data class ReportSummaryDto(
    val total_sales: String = "0.00",
    val total_expenses: String = "0.00",
    val net_profit: String = "0.00",
    val total_receivables: String = "0.00",
    val total_payables: String = "0.00"
)

data class ReportSummaryResponseDto(
    val summary: ReportSummaryDto
)

interface ReportApi {
    @GET("api/business/reports/profit-loss/")
    suspend fun getProfitLossReport(
        @Query("period") period: String = "month"
    ): Response<ProfitLossResponseDto>

    @GET("api/business/reports/summary/")
    suspend fun getReportSummary(
        @Query("period") period: String = "this_month"
    ): Response<ReportSummaryResponseDto>
}