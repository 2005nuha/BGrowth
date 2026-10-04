package com.example.bgrowth.ui.recordpayment

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class RecordPaymentViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(RecordPaymentUiState())
    val uiState: StateFlow<RecordPaymentUiState> = _uiState.asStateFlow()

    fun onPaymentAmountChange(amount: String) {
        _uiState.update {
            it.copy(
                paymentAmount = amount,
                selectedAmountType = AmountType.CUSTOM
            )
        }
    }

    fun onAmountTypeSelected(type: AmountType) {
        val currentBalance = _uiState.value.remainingBalance
        val newAmount = when (type) {
            AmountType.FULL -> String.format("%.2f", currentBalance)
            AmountType.HALF -> String.format("%.2f", currentBalance / 2)
            AmountType.CUSTOM -> _uiState.value.paymentAmount
        }
        _uiState.update { it.copy(selectedAmountType = type, paymentAmount = newAmount) }
    }

    fun onPaymentMethodDropdownExpanded(expanded: Boolean) {
        _uiState.update { it.copy(isPaymentMethodDropdownExpanded = expanded) }
    }

    fun onPaymentMethodSelected(method: String) {
        _uiState.update {
            it.copy(
                paymentMethod = method,
                isPaymentMethodDropdownExpanded = false
            )
        }
    }

    fun onConfirmPayment() {
        // سيتم برمجة عملية حفظ الدفعة في قاعدة البيانات هنا لاحقاً
    }
}