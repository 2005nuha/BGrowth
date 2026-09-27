package com.example.bgrowth.ui.customers

data class CustomerItem(
    val id: Int,
    val initials: String,
    val name: String,
    val phone: String,
    val balance: String,
    val hasBalance: Boolean
)

data class CustomersUiState(
    val isEmpty: Boolean = false,
    val searchQuery: String = "",
    val customers: List<CustomerItem> = emptyList()
)