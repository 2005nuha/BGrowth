package com.example.bgrowth.ui.reports

data class ReportsUiState(
    val selectedPeriod: ReportPeriod = ReportPeriod.THIS_MONTH, // الشاشة تبدأ بشهر
    val showPeriodSelector: Boolean = false,
    val bottomSheetSelectedPeriod: ReportPeriod = ReportPeriod.THIS_WEEK,
    val bottomSheetMonth: String = "September 2026"
)

enum class ReportPeriod(val title: String, val displayLabel: String) {
    THIS_WEEK("This Week", "This Week"),
    THIS_MONTH("This Month", "This Month (Sep 2026)"),
    THIS_YEAR("This Year", "This Year (2026)"),
    CUSTOM_RANGE("Custom Range", "Custom Range")
}