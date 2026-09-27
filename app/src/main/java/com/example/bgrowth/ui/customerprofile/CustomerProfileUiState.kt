package com.example.bgrowth.ui.customerprofile

data class PurchaseItem(
    val id: Int,
    val name: String,
    val category: String,
    val price: String
)

data class CustomerProfileUiState(
    val initials: String = "SA",
    val name: String = "Sara Ahmed",
    val phone: String = "059 123 4567",
    val outstandingBalance: String = "$45.00",
    val unpaidDebtsCount: Int = 2,
    val selectedTab: String = "Purchases",
    val tabs: List<String> = listOf("Purchases", "Debts", "Invoices"),
    val purchases: List<PurchaseItem> = emptyList()
)