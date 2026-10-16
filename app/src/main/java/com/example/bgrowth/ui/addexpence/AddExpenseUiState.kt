package com.example.bgrowth.ui.addexpence

data class AddExpenseUiState(
    val expenseName: String = "",
    val category: String = "Utilities",
    val price: String = "45",
    val date: String = "Today",
    val paymentMethod: String = "Cash",
    val recentExpenses: List<RecentExpenseItem> = emptyList(),
    val isCategoryDropdownExpanded: Boolean = false,
    val isPaymentMethodDropdownExpanded: Boolean = false
)

data class RecentExpenseItem(
    val id: Int,
    val title: String,
    val subtitle: String,
    val amount: String
)