package com.example.bgrowth.ui.invoicedetail

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class InvoiceDetailViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(InvoiceDetailUiState())
    val uiState: StateFlow<InvoiceDetailUiState> = _uiState.asStateFlow()

    fun onShowCancelDialog(show: Boolean) {
        _uiState.update { it.copy(showCancelDialog = show) }
    }

    fun onConfirmCancel() {
        _uiState.update {
            it.copy(
                status = InvoiceStatus.CANCELED,
                showCancelDialog = false,
                cancelDate = "Sep 4" // تاريخ وهمي لتجربة حالة الإلغاء
            )
        }
    }

    fun onMarkAsPaid() {
        _uiState.update { it.copy(status = InvoiceStatus.PAID) }
    }
}