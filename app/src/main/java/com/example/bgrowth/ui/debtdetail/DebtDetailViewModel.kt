package com.example.bgrowth.ui.debtdetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bgrowth.data.repository.DebtRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.Locale

class DebtDetailViewModel(
    private val repository: DebtRepository = DebtRepository()
) : ViewModel() {

    private val _uiState = MutableStateFlow(DebtDetailUiState())
    val uiState: StateFlow<DebtDetailUiState> = _uiState.asStateFlow()

    fun loadDebtDetails(debtId: Int) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }

            val result = repository.getDebts()
            if (result.isSuccess) {
                val debt = result.getOrNull()?.find { it.id == debtId }

                if (debt != null) {
                    val partyName = debt.party_name
                    val initials = partyName.trim()
                        .split(" ")
                        .take(2)
                        .mapNotNull { part -> part.firstOrNull()?.uppercaseChar() }
                        .joinToString("")
                        .ifEmpty { "D" }

                    val orig = debt.original_amount.toDoubleOrNull() ?: 0.0
                    val paid = debt.paid_amount.toDoubleOrNull() ?: 0.0
                    val rem = debt.remaining_amount?.toDoubleOrNull() ?: (orig - paid)
                    val progressVal = if (orig > 0) (paid / orig).toFloat() else 0f

                    val debtTypeStr = if (debt.debt_type == "RECEIVABLE") "Customer Debt" else "Supplier Debt"
                    val dateStr = debt.due_date?.let { "• Due $it" } ?: ""

                    val isUnpaid = debt.status.uppercase() == "UNPAID"
                    val statusText = when (debt.status.uppercase()) {
                        "UNPAID" -> "Unpaid"
                        "PARTIAL" -> "Partially Paid"
                        "PAID" -> "Paid"
                        else -> debt.status
                    }

                    _uiState.update { state ->
                        state.copy(
                            debtId = debt.id,
                            partyName = partyName,
                            initials = initials,
                            debtTypeAndDate = "$debtTypeStr $dateStr",
                            totalAmount = "$${String.format(Locale.US, "%.2f", orig)}",
                            paidAmountText = "$${String.format(Locale.US, "%.2f", paid)}",
                            remainingAmountText = "$${String.format(Locale.US, "%.2f", rem)}",
                            progress = progressVal,
                            statusText = statusText,
                            isUnpaid = isUnpaid,
                            notes = debt.notes ?: "",
                            isLoading = false
                        )
                    }
                } else {
                    _uiState.update { it.copy(isLoading = false, errorMessage = "Debt not found") }
                }
            } else {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = result.exceptionOrNull()?.message ?: "Error loading details"
                    )
                }
            }
        }
    }

    fun onRecordPayment() {
        // سيتم ربط نافذة تسجيل الدفعة هنا
    }
}