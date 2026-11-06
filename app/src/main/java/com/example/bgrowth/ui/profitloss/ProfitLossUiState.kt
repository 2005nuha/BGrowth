package com.example.bgrowth.ui.profitloss

data class ProfitLossUiState(
    val selectedPeriod: String = "Monthly",
    val periods: List<String> = listOf("Weekly", "Monthly", "Custom"),
    val isEmpty: Boolean = false,
    val showCustomDatePicker: Boolean = false,
    val isLoading: Boolean = false,
    val errorMessage: String? = null,

    val totalSales: String = "0.00",
    val salesChange: String = "+0%",
    val totalExpenses: String = "0.00",
    val expensesChange: String = "0%",

    val netProfit: Double = 0.0,
    val netProfitLabel: String = "THIS MONTH",
    val profitMargin: String = "0%",

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