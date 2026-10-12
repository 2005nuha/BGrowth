package com.example.bgrowth.ui.reports

data class ReportsUiState(
    val selectedPeriod: ReportPeriod = ReportPeriod.THIS_MONTH,
    val showPeriodSelector: Boolean = false,
    val bottomSheetSelectedPeriod: ReportPeriod = ReportPeriod.THIS_WEEK,
    val bottomSheetMonth: String = "September 2026",
    val totalSales: String = "$3,240.00",
    val totalExpenses: String = "$1,390.00",
    val netProfit: String = "$1,850.00",
    val owedToMe: String = "$285.00",
    val iOwe: String = "$150.00",
    val bestMonthText: String = "September — $3,240.00 in sales",
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)

enum class ReportPeriod(val title: String, val displayLabel: String, val apiKey: String) {
    THIS_WEEK("This Week", "This Week", "this_week"),
    THIS_MONTH("This Month", "This Month (Sep 2026)", "this_month"),
    THIS_YEAR("This Year", "This Year (2026)", "this_year"),
    CUSTOM_RANGE("Custom Range", "Custom Range", "custom")
}