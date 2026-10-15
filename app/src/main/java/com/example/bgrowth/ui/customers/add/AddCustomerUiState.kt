package com.example.bgrowth.ui.customers.add

data class AddCustomerUiState(
    val fullName: String = "",
    val phoneNumber: String = "",
    val notes: String = "",
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)