package com.example.bgrowth.ui.debts

data class DebtItem(
    val id: Int,
    val initials: String,
    val name: String,
    val dueDate: String,
    val amount: String,
    val isUnpaid: Boolean, // لتحديد هل النص أحمر أم برتقالي
    val statusText: String // "Unpaid" or "Partially Paid"
)

data class DebtsUiState(
    val selectedTab: String = "Owed to Me",
    val totalOwedToMe: String = "$285.00",
    val totalIOwe: String = "$150.00",
    val debtsList: List<DebtItem> = emptyList(),
    val isEmpty: Boolean = false
)