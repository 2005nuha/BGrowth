package com.example.bgrowth.ui.debts

data class DebtItemUiState(
    val id: Int = 0,
    val name: String = "",
    val initials: String = "",
    val dueDate: String = "",
    val amount: String = "",
    val status: String = "",
    val isUnpaid: Boolean = true
)

data class DebtsUiState(
    val isLoading: Boolean = false,
    val selectedTab: String = "Owed to Me",
    val debtsList: List<DebtItemUiState> = emptyList(),
    val errorMessage: String? = null
)