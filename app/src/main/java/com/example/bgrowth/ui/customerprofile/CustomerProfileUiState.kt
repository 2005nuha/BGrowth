package com.example.bgrowth.ui.customerprofile

import com.example.bgrowth.data.model.CustomerResponse

data class PurchaseItem(
    val id: Int,
    val name: String,
    val category: String,
    val price: String
)

data class CustomerProfileUiState(
    val customer: CustomerResponse? = null,
    val initials: String = "--",
    val name: String = "Loading...",
    val phone: String = "",
    val outstandingBalance: String = "$0.00",
    val unpaidDebtsCount: Int = 0,
    val selectedTab: String = "Purchases",
    val tabs: List<String> = listOf("Purchases", "Debts", "Invoices"),
    val purchases: List<PurchaseItem> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)