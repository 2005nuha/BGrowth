package com.example.bgrowth.ui.adddebt

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class AddDebtViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(AddDebtUiState())
    val uiState: StateFlow<AddDebtUiState> = _uiState.asStateFlow()

    fun onDebtTypeChange(type: String) {
        _uiState.update { it.copy(debtType = type) }
    }

    fun onPartyNameChange(name: String) {
        _uiState.update { it.copy(partyName = name) }
    }

    fun onAmountChange(amount: String) {
        _uiState.update { it.copy(amount = amount) }
    }

    fun onDueDateChange(date: String) {
        _uiState.update { it.copy(dueDate = date) }
    }

    fun onNotesChange(notes: String) {
        _uiState.update { it.copy(notes = notes) }
    }

    fun toggleDropdown(expanded: Boolean) {
        _uiState.update { it.copy(isDropdownExpanded = expanded) }
    }

    fun onSaveDebt() {
        // سيتم إضافة كود الحفظ هنا لاحقاً
    }
}