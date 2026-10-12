package com.example.bgrowth.ui.debtdetail

data class PaymentHistoryItem(
    val id: Int,
    val dateAndMethod: String,
    val amount: String
)

data class DebtDetailUiState(
    val debtId: Int = 0,
    val partyName: String = "",
    val initials: String = "",
    val debtTypeAndDate: String = "",
    val totalAmount: String = "$0.00",
    val paidAmountText: String = "$0.00",
    val remainingAmountText: String = "$0.00",
    val progress: Float = 0f,
    val statusText: String = "Unpaid",
    val isUnpaid: Boolean = true,
    val dueWarningMessage: String? = null,
    val notes: String = "",
    val paymentHistory: List<PaymentHistoryItem> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)