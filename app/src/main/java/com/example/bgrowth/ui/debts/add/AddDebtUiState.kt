package com.example.bgrowth.ui.debts.add

data class AddDebtUiState(
    val partyName: String = "",
    val amount: String = "",
    val isReceivable: Boolean = true,
    val dueDate: String = "",
    val notes: String = "",
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)