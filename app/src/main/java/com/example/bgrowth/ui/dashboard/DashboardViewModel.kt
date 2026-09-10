package com.example.bgrowth.ui.dashboard

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class DashboardViewModel : ViewModel() {

    // TODO: Replace with real dashboard API
    private val _uiState = MutableStateFlow(emptyDashboardState())
    val uiState: StateFlow<DashboardUiState> = _uiState.asStateFlow()

    /** Temporary helpers for previews and local testing until dashboard data is available. */
    fun showEmptyDashboard() {
        // TODO: Replace with real dashboard API
        _uiState.value = emptyDashboardState()
    }

    fun showPopulatedDashboard() {
        // TODO: Replace with real dashboard API
        _uiState.value = populatedDashboardState()
    }

    companion object {
        fun emptyDashboardState() = DashboardUiState(
            userName = "Ahmad",
            businessName = "Ahmad's Business",
            hasBusinessData = false
        )

        fun populatedDashboardState() = DashboardUiState(
            userName = "Ahmad",
            businessName = "Ahmad's Business",
            hasBusinessData = true,
            todaySales = 450.0,
            todayExpenses = 180.0,
            netProfit = 330.0,
            salesTrendPercent = 12.0,
            expensesTrendPercent = -5.0,
            lowStockCount = 2
        )
    }
}
