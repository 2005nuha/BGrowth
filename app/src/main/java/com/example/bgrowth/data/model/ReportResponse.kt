package com.example.bgrowth.data.model

import com.google.gson.annotations.SerializedName

data class ReportSummaryResponse(
    @SerializedName("total_sales") val totalSales: String?,
    @SerializedName("total_expenses") val totalExpenses: String?,
    @SerializedName("net_profit") val netProfit: String?,
    @SerializedName("total_receivables") val totalReceivables: String?,
    @SerializedName("total_payables") val totalPayables: String?
)

data class ChartDataPoint(
    @SerializedName("label") val label: String,
    @SerializedName("sales") val sales: Double,
    @SerializedName("expenses") val expenses: Double
)

data class ReportResponse(
    @SerializedName("summary") val summary: ReportSummaryResponse,
    @SerializedName("chart_data") val chartData: List<ChartDataPoint> = emptyList()
)