package com.example.bgrowth.ui.expenses.add

data class AddExpenseUiState(
    val amount: String = "",
    val category: String = "",
    val description: String = "",
    val expenseDate: String = "",
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)