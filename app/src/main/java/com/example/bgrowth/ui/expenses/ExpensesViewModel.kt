package com.example.bgrowth.ui.expenses

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class ExpensesViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(ExpensesUiState())
    val uiState: StateFlow<ExpensesUiState> = _uiState.asStateFlow()

    init {
        loadExpenses()
    }

    private fun loadExpenses() {
        val mockGroups = listOf(
            ExpenseGroup(
                dateHeader = "Today ,sep7",
                totalAmount = "-$45.00",
                items = listOf(
                    ExpenseItem("Electricity Bill", "Utilities · Today, 9:15 AM", "-$45.00")
                )
            ),
            ExpenseGroup(
                dateHeader = "Yesterday ,sep6",
                totalAmount = "-$120.00",
                items = listOf(
                    ExpenseItem("Packaging Supplies", "Supplies · Yesterday", "-$120.00"),
                    ExpenseItem("Delivery Fuel", "Transport · Yesterday", "-$16.00")
                )
            )
        )

        _uiState.update { state ->
            state.copy(
                expenseGroups = mockGroups,
                isLoading = false
            )
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