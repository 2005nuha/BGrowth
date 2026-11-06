package com.example.bgrowth.ui.expenses.add

import com.example.bgrowth.ui.expenses.ExpenseItem

data class AddExpenseUiState(
    val expenseName: String = "",
    val category: String = "Utilities",
    val price: String = "",
    val date: String = "",
    val paymentMethod: String = "Cash",
    val isCategoryDropdownExpanded: Boolean = false,
    val isPaymentMethodDropdownExpanded: Boolean = false,
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val errorMessage: String? = null,
    val recentExpenses: List<ExpenseItem> = emptyList()
)