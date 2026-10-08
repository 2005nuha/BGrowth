package com.example.bgrowth.ui.customers

import com.example.bgrowth.data.model.CustomerResponse

data class CustomerItem(
    val id: Int,
    val initials: String,
    val name: String,
    val phone: String,
    val balance: String = "No balance",
    val hasBalance: Boolean = false
)

data class CustomersUiState(
    val customers: List<CustomerItem> = emptyList(),
    val searchQuery: String = "",
    val isEmpty: Boolean = false,
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)