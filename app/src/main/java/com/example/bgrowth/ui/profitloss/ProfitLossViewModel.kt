package com.example.bgrowth.ui.profitloss

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class ProfitLossViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(ProfitLossUiState())
    val uiState: StateFlow<ProfitLossUiState> = _uiState.asStateFlow()

    init {
        loadMonthlyData()
    }

    private fun loadMonthlyData() {
        val mockChartData = listOf(
            ChartData("W1", 0.6f, 0.3f),
            ChartData("W2", 0.7f, 0.4f),
            ChartData("W3", 0.4f, 0.3f),
            ChartData("W4", 0.8f, 0.3f)
        )
        val mockTopExpenses = listOf(
            ExpenseCategoryData("Rent", 48, 0xFFA05252),
            ExpenseCategoryData("Supplies", 28, 0xFFB89255),
            ExpenseCategoryData("Utilities", 16, 0xFF5C479D),
            ExpenseCategoryData("Transport", 8, 0xFF9E47A5)
        )
        _uiState.update {
            it.copy(
                chartData = mockChartData,
                topExpenses = mockTopExpenses,
                netProfitLabel = "SEPTEMBER"
            )
        }
    }

    private fun loadWeeklyData() {
        val mockChartData = listOf(
            ChartData("S", 0.6f, 0.3f),
            ChartData("M", 0.7f, 0.4f),
            ChartData("T", 0.4f, 0.3f),
            ChartData("W", 0.8f, 0.3f),
            ChartData("T", 0.5f, 0.2f),
            ChartData("F", 0.9f, 0.5f),
            ChartData("S", 0.2f, 0.1f)
        )
        _uiState.update {
            it.copy(
                chartData = mockChartData,
                netProfitLabel = "THIS WEEK"
            )
        }
    }

    fun onPeriodSelected(period: String) {
        if (period == "Custom") {
            _uiState.update { it.copy(showCustomDatePicker = true) }
        } else {
            _uiState.update { it.copy(selectedPeriod = period) }
            if (period == "Weekly") loadWeeklyData() else loadMonthlyData()
        }
    }

    fun onDismissDatePicker() {
        _uiState.update { it.copy(showCustomDatePicker = false) }
    }

    fun onApplyDateRange() {
        _uiState.update { it.copy(selectedPeriod = "Custom", showCustomDatePicker = false) }
    }
}