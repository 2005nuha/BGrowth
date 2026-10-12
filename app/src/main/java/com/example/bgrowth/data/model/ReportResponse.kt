package com.example.bgrowth.data.model

data class ReportSummaryResponse(
    val total_sales: String,
    val total_expenses: String,
    val net_profit: String,
    val total_receivables: String,
    val total_payables: String
)

data class ChartDataPoint(
    val label: String,
    val sales: Double,
    val expenses: Double
)

data class ReportResponse(
    val summary: ReportSummaryResponse,
    val chart_data: List<ChartDataPoint>
)