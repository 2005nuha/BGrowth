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
            val periodParam = when (_uiState.value.selectedPeriod.lowercase()) {
                "weekly" -> "week"
                "monthly" -> "month"
                else -> "month"
            }

            val result = repository.getProfitLossReport(periodParam)
            if (result.isSuccess) {
                val data = result.getOrNull()
                val sales = data?.total_revenue?.toDoubleOrNull() ?: 0.0
                val expenses = data?.total_expenses?.toDoubleOrNull() ?: 0.0
                val net = data?.net_profit?.toDoubleOrNull() ?: 0.0

                _uiState.update {
                    it.copy(
                        totalSales = "%.2f".format(sales),
                        totalExpenses = "%.2f".format(expenses),
                        netProfit = net,
                        isEmpty = (sales == 0.0 && expenses == 0.0)
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