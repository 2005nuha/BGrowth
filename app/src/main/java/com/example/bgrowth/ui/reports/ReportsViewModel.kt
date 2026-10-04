package com.example.bgrowth.ui.reports

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class ReportsViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(ReportsUiState())
    val uiState: StateFlow<ReportsUiState> = _uiState.asStateFlow()

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
        _uiState.update {
            it.copy(
                selectedPeriod = it.bottomSheetSelectedPeriod,
                showPeriodSelector = false
            )
        }
    }
}