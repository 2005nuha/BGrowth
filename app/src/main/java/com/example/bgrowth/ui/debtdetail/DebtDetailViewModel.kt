package com.example.bgrowth.ui.debtdetail

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class DebtDetailViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(DebtDetailUiState())
    val uiState: StateFlow<DebtDetailUiState> = _uiState.asStateFlow()

    fun onRecordPayment() {
        // سيتم برمجة عملية إضافة دفعة جديدة هنا لاحقاً
    }
}