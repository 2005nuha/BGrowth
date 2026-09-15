package com.example.bgrowth.ui.product


data class AddProductUiState(
    val productName: String = "",
    val categoryName: String = "",
    val description: String = "",

    val price: String = "",
    val cost: String = "",
    val openingStock: String = "",
    val minStockLevel: String = "",

    val trackStock: Boolean = true,

    val productNameError: String? = null,
    val categoryError: String? = null,
    val priceError: String? = null,
    val costError: String? = null,
    val openingStockError: String? = null,
    val minStockLevelError: String? = null,

    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val isProductSaved: Boolean = false
)