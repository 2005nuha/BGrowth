package com.example.bgrowth.ui.debts.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bgrowth.data.repository.DebtRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class DebtDetailViewModel(
    private val repository: DebtRepository = DebtRepository()
) : ViewModel() {

    private val _uiState = MutableStateFlow(DebtDetailUiState())
    val uiState: StateFlow<DebtDetailUiState> = _uiState.asStateFlow()

    fun loadDebtDetail(debtId: Int) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }

            repository.getDebtDetail(debtId)
                .onSuccess { dto ->
                    val initials = dto.party_name.trim()
                        .split(" ")
                        .take(2)
                        .mapNotNull { it.firstOrNull()?.uppercaseChar() }
                        .joinToString("")
                        .ifEmpty { "D" }

                    val isUnpaidStatus = dto.status == "UNPAID"

                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            debtId = dto.id,
                            partyName = dto.party_name,
                            initials = initials,
                            debtTypeAndDate = "${dto.debt_type} • Due ${dto.due_date ?: "N/A"}",
                            totalAmount = "$${dto.original_amount}",
                            paidAmountText = "$${dto.paid_amount}",
                            remainingAmountText = "$${dto.remaining_amount}",
                            statusText = dto.status,
                            isUnpaid = isUnpaidStatus
                        )
                    }
                }
                .onFailure { error ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = error.message ?: "Failed to load debt details"
                        )
                    }
                }
        }
    }

    fun onRecordPayment() {
        // الإجراء الخاص بتسجيل الدفع
    }
}