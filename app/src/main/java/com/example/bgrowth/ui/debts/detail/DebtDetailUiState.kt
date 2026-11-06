package com.example.bgrowth.ui.debts.detail
data class PaymentHistoryItem(
    val id: Int,
    val dateAndMethod: String,
    val amount: String
)

data class DebtDetailUiState(
    val isLoading: Boolean = false,
    val isSavingPayment: Boolean = false,
    val debtId: Int = 0,
    val partyName: String = "",
    val initials: String = "SA",
    val debtTypeAndDate: String = "Customer Debt • Due Sep 20",
    val totalAmount: String = "$150.00",
    val paidAmountText: String = "$45.00",
    val remainingAmountText: String = "$105.00",
    val progress: Float = 0.3f,
    val statusText: String = "Partially Paid",
    val isUnpaid: Boolean = false,
    val dueWarningMessage: String? = null,
    val paymentHistory: List<PaymentHistoryItem> = emptyList(),
    val errorMessage: String? = null
)