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
            _uiState.update { it.copy(isLoading = true) }

            // استدعاء جلب البيانات من الـ Repository
            val result = repository.getExpenses()

            if (result.isSuccess) {
                val rawExpenses = result.getOrDefault(emptyList())

                // تحويل القائمة القادمة من الباك إند إلى ExpenseGroup لعرضها
                val groupedExpenses = rawExpenses
                    .groupBy { it.expense_date } // تجميع حسب التاريخ
                    .map { (date, items) ->
                        ExpenseGroup(
                            dateHeader = date,
                            totalAmount = "-$${items.sumOf { it.amount.toDoubleOrNull() ?: 0.0 }}",
                            items = items.map { expense ->
                                ExpenseItem(
                                    title = expense.description ?: expense.category,
                                    subtitle = "${expense.category} · ${expense.expense_date}",
                                    amount = "-$${expense.amount}"
                                )
                            }
                        )
                    }

                _uiState.update { state ->
                    state.copy(
                        expenseGroups = groupedExpenses,
                        isLoading = false
                    )
                }
            } else {
                _uiState.update { state ->
                    state.copy(
                        isLoading = false
                        // يمكنك إضافة حقل errorMessage في الـ UiState لإظهار الخطأ
                    )
                }
            }
        }
    }

    fun onSearchQueryChange(newQuery: String) {
        _uiState.update { it.copy(searchQuery = newQuery) }
    }

    fun onDateFilterSelected(dateOption: String) {
        _uiState.update { it.copy(selectedDate = dateOption) }
    }

    fun onCategoryFilterSelected(categoryOption: String) {
        _uiState.update { it.copy(selectedCategory = categoryOption) }
    }
}