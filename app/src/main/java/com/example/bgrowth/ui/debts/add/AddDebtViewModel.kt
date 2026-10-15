package com.example.bgrowth.ui.debts.add

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bgrowth.data.repository.DebtRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AddDebtViewModel(
    private val repository: DebtRepository = DebtRepository()
) : ViewModel() {

    private val _uiState = MutableStateFlow(AddDebtUiState())
    val uiState: StateFlow<AddDebtUiState> = _uiState.asStateFlow()

    fun onPartyNameChange(name: String) {
        _uiState.update { it.copy(partyName = name, errorMessage = null) }
    }

    fun onAmountChange(amount: String) {
        _uiState.update { it.copy(amount = amount, errorMessage = null) }
    }

    fun onDebtTypeChange(isReceivable: Boolean) {
        _uiState.update { it.copy(isReceivable = isReceivable) }
    }

    fun onDueDateChange(date: String) {
        _uiState.update { it.copy(dueDate = date) }
    }

    fun onNotesChange(notes: String) {
        _uiState.update { it.copy(notes = notes) }
    }

    fun onSaveDebt(onSuccess: () -> Unit) {
        val currentState = _uiState.value
        if (currentState.partyName.isBlank() || currentState.amount.isBlank()) {
            _uiState.update { it.copy(errorMessage = "Please fill in all required fields") }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }

            val debtType = if (currentState.isReceivable) "RECEIVABLE" else "PAYABLE"

            repository.createDebt(
                debtType = debtType,
                partyName = currentState.partyName,
                amount = currentState.amount,
                dueDate = currentState.dueDate.ifBlank { null },
                notes = currentState.notes
            ).onSuccess {
                _uiState.update { it.copy(isLoading = false) }
                onSuccess()
            }.onFailure { error ->
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = error.message ?: "Failed to save debt"
                    )
                }
            }
        }
    }
}