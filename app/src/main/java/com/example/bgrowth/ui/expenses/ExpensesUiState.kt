package com.example.bgrowth.ui.expenses

data class ExpensesUiState(
    val isLoading: Boolean = false,
    val searchQuery: String = "",
    val selectedCategory: String = "All",
    val selectedDate: String = "All",
    val dateOptions: List<String> = listOf("All", "Today", "This week", "This month"),
    val categoryOptions: List<String> = listOf("All", "Rent", "Supplies", "Utilities", "General"),
    val totalExpenses: Double = 0.0,
    val avgExpense: Double = 0.0,
    val expenseGroups: List<ExpenseGroup> = emptyList(),
    val errorMessage: String? = null
)

data class ExpenseGroup(
    val dateHeader: String,
    val totalAmount: String,
    val items: List<ExpenseItem>
)

data class ExpenseItem(
    val id: Int,
    val category: String,
    val description: String,
    val amount: String,
    val date: String
)