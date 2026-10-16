package com.example.bgrowth.ui.selectcustomer

data class CustomerSelectionItem(
    val id: String,
    val name: String,
    val phone: String,
    val initials: String
)

data class SelectCustomerUiState(
    val searchQuery: String = "",
    val customers: List<CustomerSelectionItem> = emptyList(),
    val selectedCustomerId: String? = null
)