package com.example.bgrowth.ui.expenses

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bgrowth.data.repository.ExpenseRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ExpensesViewModel(
    private val repository: ExpenseRepository = ExpenseRepository()
) : ViewModel() {

    private val _uiState = MutableStateFlow(ExpensesUiState())
    val uiState: StateFlow<ExpensesUiState> = _uiState.asStateFlow()

    init {
        fetchExpenses()
    }

    fun fetchExpenses(category: String? = null) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }

            repository.getExpenses(category)
                .onSuccess { dtoList ->
                    val expenseItems = dtoList.map { dto ->
                        ExpenseItem(
                            id = dto.id,
                            category = dto.category,
                            description = dto.description.ifBlank { "No description" },
                            amount = "$${dto.amount}",
                            date = dto.expense_date
                        )
                    }

                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            expenses = expenseItems,
                            isEmpty = expenseItems.isEmpty()
                        )
                    }
                }
                .onFailure { error ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = error.message ?: "Failed to load expenses"
                        )
                    }
                }
        }
    }
}