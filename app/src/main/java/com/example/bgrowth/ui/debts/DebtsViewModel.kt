package com.example.bgrowth.ui.debts

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bgrowth.data.repository.DebtRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class DebtsViewModel(
    private val repository: DebtRepository = DebtRepository()
) : ViewModel() {

    private val _uiState = MutableStateFlow(DebtsUiState())
    val uiState: StateFlow<DebtsUiState> = _uiState.asStateFlow()

    init {
        fetchDebts()
    }

    fun fetchDebts(debtType: String? = null, status: String? = null) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }

            repository.getDebts(debtType = debtType, status = status)
                .onSuccess { dtoList ->
                    val debtItems = dtoList.map { dto ->
                        val isReceivable = dto.debt_type == "RECEIVABLE"
                        DebtItem(
                            id = dto.id,
                            title = dto.party_name,
                            subtitle = if (isReceivable) "You owe" else "Owes you",
                            amount = "$${dto.remaining_amount}",
                            dueDate = dto.due_date ?: "No due date",
                            isPaid = dto.status == "PAID"
                        )
                    }

                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            debts = debtItems,
                            isEmpty = debtItems.isEmpty()
                        )
                    }
                }
                .onFailure { error ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = error.message ?: "Failed to load debts"
                        )
                    }
                }
        }
    }

    fun onTabSelected(tabIndex: Int) {
        val selectedType = when (tabIndex) {
            1 -> "RECEIVABLE"
            2 -> "PAYABLE"
            else -> null
        }
        _uiState.update { it.copy(selectedTabIndex = tabIndex) }
        fetchDebts(debtType = selectedType)
    }
}