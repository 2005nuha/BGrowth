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
        loadExpenses()
    }

    fun loadExpenses() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }

            repository.fetchExpenses()
                .onSuccess { items ->
                    val total = items.sumOf {
                        it.amount.replace("$", "").replace("-", "").toDoubleOrNull() ?: 0.0
                    }
                    val avg = if (items.isNotEmpty()) total / items.size else 0.0

                    val groups = if (items.isNotEmpty()) {
                        listOf(
                            ExpenseGroup(
                                dateHeader = "Recent Expenses",
                                totalAmount = "-$$total",
                                items = items
                            )
                        )
                    } else emptyList()

                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            totalExpenses = total,
                            avgExpense = avg,
                            expenseGroups = groups
                        )
                    }
                }
                .onFailure { error ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = error.localizedMessage ?: "Failed to load expenses"
                        )
                    }
                }
        }
    }

    fun onSearchQueryChange(query: String) {
        _uiState.update { it.copy(searchQuery = query) }
    }

    // الدالة الأولى المفقودة
    fun onDateFilterSelected(date: String) {
        _uiState.update { it.copy(selectedDate = date) }
    }

    // الدالة الثانية المفقودة
    fun onCategoryFilterSelected(category: String) {
        _uiState.update { it.copy(selectedCategory = category) }
        loadExpenses() // إعادة تحميل المصاريف بناءً على التصنيف المختار
    }
}