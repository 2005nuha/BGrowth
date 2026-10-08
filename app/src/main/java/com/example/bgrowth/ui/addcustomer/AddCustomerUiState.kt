package com.example.bgrowth.ui.addcustomer

data class AddCustomerUiState(
    val fullName: String = "",
    val phoneNumber: String = "",
    val notes: String = "",
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val errorMessage: String? = null
)