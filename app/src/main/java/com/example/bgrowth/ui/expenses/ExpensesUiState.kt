package com.example.bgrowth.ui.expenses

data class ExpensesUiState(
    val searchQuery: String = "",
    val selectedDate: String = "This week",
    val selectedCategory: String = "All",
    val dateOptions: List<String> = listOf("This week", "Today", "This month"),
    val categoryOptions: List<String> = listOf("All", "Rent", "Supplies", "Utilities"),
    val totalExpenses: Double = 612.50,
    val avgExpense: Double = 22.50,
    val expenseGroups: List<ExpenseGroup> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)

data class ExpenseItem(
    val title: String,
    val subtitle: String,
    val amount: String
)

data class ExpenseGroup(
    val dateHeader: String,
    val totalAmount: String,
    val items: List<ExpenseItem>
)