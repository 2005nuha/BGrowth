package com.example.bgrowth.ui.debts.detail

data class DebtDetailUiState(
    val isLoading: Boolean = false,
    val isSavingPayment: Boolean = false,
    val debtId: Int = 0,
    val partyName: String = "",
    val originalAmount: String = "",
    val paidAmount: String = "",
    val remainingAmount: String = "",
    val status: String = "",
    val debtType: String = "",
    val dueDate: String = "",
    val notes: String = "",
    val errorMessage: String? = null
)