package com.example.bgrowth.ui.recordpayment

data class RecordPaymentUiState(
    val partyName: String = "Rana Nasser",
    val remainingBalance: Double = 72.00,
    val paymentAmount: String = "72.00",
    val selectedAmountType: AmountType = AmountType.FULL, // FULL, HALF, CUSTOM
    val selectedDate: String = "Today",
    val paymentMethod: String = "Cash",
    val isPaymentMethodDropdownExpanded: Boolean = false,
    val paymentMethodOptions: List<String> = listOf("Cash", "Bank Transfer", "Credit Card")
)

enum class AmountType {
    FULL, HALF, CUSTOM
}