package com.example.bgrowth.ui.profitloss

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bgrowth.data.repository.ReportRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ProfitLossViewModel(
    private val repository: ReportRepository = ReportRepository()
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProfitLossUiState())
    val uiState: StateFlow<ProfitLossUiState> = _uiState.asStateFlow()

    init {
        loadData()
    }

    private fun loadData() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }

            val periodParam = when (_uiState.value.selectedPeriod.lowercase()) {
                "weekly" -> "week"
                "monthly" -> "month"
                else -> "month"
            }

            val result = repository.getProfitLossReport(periodParam)
            result.onSuccess { data ->
                // القراءة بالحقول الصحيحة من ProfitLossResponseDto
                val sales = data.total_revenue.toDoubleOrNull() ?: 0.0
                val expenses = data.total_expenses.toDoubleOrNull() ?: 0.0
                val net = data.net_profit.toDoubleOrNull() ?: (sales - expenses)

                val isDataEmpty = (sales == 0.0 && expenses == 0.0)
                val margin = if (sales > 0) "%.0f%%".format((net / sales) * 100) else "0%"

                _uiState.update {
                    it.copy(
                        isLoading = false,
                        totalSales = "%.2f".format(sales),
                        totalExpenses = "%.2f".format(expenses),
                        netProfit = net,
                        profitMargin = margin,
                        isEmpty = isDataEmpty,
                        netProfitLabel = if (periodParam == "week") "THIS WEEK" else "THIS MONTH"
                    )
                }
            }.onFailure { error ->
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = error.localizedMessage ?: "Failed to load profit loss report"
                    )
                }
            }
        }
    }

    fun onPeriodSelected(period: String) {
        if (period == "Custom") {
            _uiState.update { it.copy(showCustomDatePicker = true) }
        } else {
            _uiState.update { it.copy(selectedPeriod = period) }
            loadData()
        }
    }

    fun onDismissDatePicker() {
        _uiState.update { it.copy(showCustomDatePicker = false) }
    }

    fun onApplyDateRange() {
        _uiState.update { it.copy(selectedPeriod = "Custom", showCustomDatePicker = false) }
        loadData()
    }
}