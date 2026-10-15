package com.example.bgrowth.ui.debts

data class DebtsUiState(
    val isLoading: Boolean = false,
    val isEmpty: Boolean = false,
    val selectedTabIndex: Int = 0,
    val debts: List<DebtItem> = emptyList(),
    val errorMessage: String? = null
)

data class DebtItem(
    val id: Int,
    val title: String,
    val subtitle: String,
    val amount: String,
    val dueDate: String,
    val isPaid: Boolean
)