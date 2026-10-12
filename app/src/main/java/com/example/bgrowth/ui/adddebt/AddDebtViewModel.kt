package com.example.bgrowth.ui.adddebt

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bgrowth.data.model.AddDebtRequest
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

    fun onDebtTypeChange(type: String) { _uiState.update { it.copy(debtType = type) } }
    fun onPartyNameChange(name: String) { _uiState.update { it.copy(partyName = name) } }
    fun onAmountChange(amount: String) { _uiState.update { it.copy(amount = amount) } }
    fun onDueDateChange(date: String) { _uiState.update { it.copy(dueDate = date) } }
    fun onNotesChange(notes: String) { _uiState.update { it.copy(notes = notes) } }
    fun toggleDropdown(expanded: Boolean) { _uiState.update { it.copy(isDropdownExpanded = expanded) } }

    fun onSaveDebt(onSuccess: () -> Unit = {}) {
        val state = _uiState.value
        val amountVal = state.amount.toDoubleOrNull() ?: return

        val apiDebtType = if (state.debtType == "Owed to Me") "RECEIVABLE" else "PAYABLE"

        val request = AddDebtRequest(
            debt_type = apiDebtType,
            party_name = state.partyName,
            original_amount = amountVal,
            due_date = state.dueDate.ifEmpty { null },
            notes = state.notes
        )

        viewModelScope.launch {
            val result = repository.addDebt(request)
            if (result.isSuccess) {
                onSuccess()
            }
        }
    }
}