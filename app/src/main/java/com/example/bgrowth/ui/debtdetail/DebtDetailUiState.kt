package com.example.bgrowth.ui.debtdetail

data class PaymentHistoryItem(
    val id: Int,
    val dateAndMethod: String,
    val amount: String
)

data class DebtDetailUiState(
    val partyName: String = "Rana Nasser",
    val debtTypeAndDate: String = "Owed to Me · Due Sep 15",
    val statusText: String = "Partially Paid",
    val totalAmount: String = "$120.00",
    val paidAmountText: String = "$48.00 paid",
    val remainingAmountText: String = "$72.00 remaining",
    val progress: Float = 0.4f, // يمثل نسبة 48 مقسومة على 120 لملء شريط التقدم
    val dueWarningMessage: String = "Due in 3 days",
    val paymentHistory: List<PaymentHistoryItem> = listOf(
        PaymentHistoryItem(1, "Sep 8 — Cash", "+$48.00")
    )
)