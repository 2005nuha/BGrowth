package com.example.bgrowth.ui.reports

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bgrowth.data.repository.ReportRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ReportsViewModel(
    private val repository: ReportRepository = ReportRepository()
) : ViewModel() {

    private val _uiState = MutableStateFlow(ReportsUiState())
    val uiState: StateFlow<ReportsUiState> = _uiState.asStateFlow()

    init {
        loadReportData(_uiState.value.selectedPeriod)
    }

    fun onPeriodSelectorClick(show: Boolean) {
        _uiState.update {
            it.copy(
                showPeriodSelector = show,
                bottomSheetSelectedPeriod = it.selectedPeriod
            )
        }
    }

    fun onBottomSheetPeriodSelected(period: ReportPeriod) {
        _uiState.update { it.copy(bottomSheetSelectedPeriod = period) }
    }

    fun onApplyPeriod() {
        val newPeriod = _uiState.value.bottomSheetSelectedPeriod
        _uiState.update {
            it.copy(
                selectedPeriod = newPeriod,
                showPeriodSelector = false
            )
        }
        loadReportData(newPeriod)
    }

    private fun loadReportData(period: ReportPeriod) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }

            val result = repository.getReportSummary(period.apiKey)
            if (result.isSuccess) {
                val data = result.getOrNull()?.summary
                if (data != null) {
                    _uiState.update { state ->
                        state.copy(
                            totalSales = "$${data.total_sales}",
                            totalExpenses = "$${data.total_expenses}",
                            netProfit = "$${data.net_profit}",
                            owedToMe = "$${data.total_receivables}",
                            iOwe = "$${data.total_payables}",
                            isLoading = false
                        )
                    }
                } else {
                    _uiState.update { it.copy(isLoading = false) }
                }
            } else {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = result.exceptionOrNull()?.message
                    )
                }
            }
        }
    }
}