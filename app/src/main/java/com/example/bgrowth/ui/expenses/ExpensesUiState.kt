package com.example.bgrowth.ui.expenses

data class ExpensesUiState(
    val isLoading: Boolean = false,
    val isEmpty: Boolean = false,
    val selectedCategory: String? = null,
    val expenses: List<ExpenseItem> = emptyList(),
    val errorMessage: String? = null
)

data class ExpenseItem(
    val id: Int,
    val category: String,
    val description: String,
    val amount: String,
    val date: String
)