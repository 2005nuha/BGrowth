package com.example.bgrowth.ui.businesssetup

import androidx.compose.runtime.Immutable

@Immutable
data class BusinessSetupUiState(
    val businessName: String = "",
    val selectedBusinessType: String = "",
    val selectedCurrency: String = "NIS ₪",
    val selectedLogoUri: String? = null,
    val businessNameError: String? = null,
    val businessTypeError: String? = null,
    val currencyError: String? = null,
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val isBusinessSetupSuccessful: Boolean = false
)
