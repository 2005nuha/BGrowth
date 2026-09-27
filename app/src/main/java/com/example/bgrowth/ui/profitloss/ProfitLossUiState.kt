package com.example.bgrowth.ui.profitloss

data class ProfitLossUiState(
    val selectedPeriod: String = "Monthly",
    val periods: List<String> = listOf("Weekly", "Monthly", "Custom"),

    val isEmpty: Boolean = false,
    val showCustomDatePicker: Boolean = false,

    val totalSales: String = "330.00",
    val salesChange: String = "+12%",
    val totalExpenses: String = "180.00",
    val expensesChange: String = "-5%",

    val netProfit: Double = 330.00,
    val netProfitLabel: String = "SEPTEMBER",
    val profitMargin: String = "57%",

    val chartData: List<ChartData> = emptyList(),
    val topExpenses: List<ExpenseCategoryData> = emptyList()
)

data class ChartData(
    val label: String,
    val salesValue: Float,
    val expensesValue: Float
)

data class ExpenseCategoryData(
    val name: String,
    val percentage: Int,
    val color: Long
)