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
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            debtId = dto.id,
                            partyName = dto.party_name,
                            originalAmount = "$${dto.original_amount}",
                            paidAmount = "$${dto.paid_amount}",
                            remainingAmount = "$${dto.remaining_amount}",
                            status = dto.status,
                            debtType = dto.debt_type,
                            dueDate = dto.due_date ?: "N/A",
                            notes = dto.notes
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

    fun onRecordPayment(debtId: Int, amount: String, note: String, onSuccess: () -> Unit) {
        if (amount.isBlank()) return

        viewModelScope.launch {
            _uiState.update { it.copy(isSavingPayment = true) }

            repository.addPayment(debtId, amount, note)
                .onSuccess {
                    _uiState.update { it.copy(isSavingPayment = false) }
                    loadDebtDetail(debtId)
                    onSuccess()
                }
                .onFailure { error ->
                    _uiState.update {
                        it.copy(
                            isSavingPayment = false,
                            errorMessage = error.message ?: "Failed to record payment"
                        )
                    }
                }
        }
    }
}