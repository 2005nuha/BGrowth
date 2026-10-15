package com.example.bgrowth.ui.customers.profile

data class CustomerProfileUiState(
    val isLoading: Boolean = false,
    val customerName: String = "",
    val customerPhone: String = "",
    val customerAddress: String = "",
    val selectedTab: String = "Purchases",
    val purchases: List<PurchaseItem> = emptyList(),
    val errorMessage: String? = null
)

data class PurchaseItem(
    val id: Int,
    val name: String,
    val category: String,
    val price: String
)