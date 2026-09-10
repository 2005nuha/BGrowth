package com.example.bgrowth.ui.dashboard

import androidx.compose.runtime.Immutable

@Immutable
data class DashboardUiState(
    val userName: String = "Ahmad",
    val businessName: String = "",
    val hasBusinessData: Boolean = false,
    val todaySales: Double = 0.0,
    val todayExpenses: Double = 0.0,
    val netProfit: Double = 0.0,
    val salesTrendPercent: Double? = null,
    val expensesTrendPercent: Double? = null,
    val lowStockCount: Int = 0,
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)
