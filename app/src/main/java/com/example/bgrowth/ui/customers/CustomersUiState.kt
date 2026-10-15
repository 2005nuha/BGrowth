package com.example.bgrowth.ui.customers

data class CustomersUiState(
    val isLoading: Boolean = false,
    val isEmpty: Boolean = false,
    val searchQuery: String = "",
    val customers: List<CustomerItem> = emptyList(),
    val errorMessage: String? = null
)

data class CustomerItem(
    val id: Int,
    val initials: String,
    val name: String,
    val phone: String,
    val dueText: String,
    val hasDebt: Boolean
)