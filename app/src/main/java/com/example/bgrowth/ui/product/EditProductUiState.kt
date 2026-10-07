package com.example.bgrowth.ui.product

data class EditProductUiState(
    val productId: Int? = null,

    val productName: String = "",
    val categoryId: Int? = null,
    val categoryName: String = "",
    val description: String = "",
    val price: String = "",
    val cost: String = "",
    val minStockLevel: String = "",

    val categories: List<CategoryOption> = emptyList(),

    val productNameError: String? = null,
    val priceError: String? = null,
    val costError: String? = null,
    val minStockLevelError: String? = null,

    val isLoading: Boolean = false,
    val isSaving: Boolean = false,
    val errorMessage: String? = null,
    val isProductUpdated: Boolean = false
)

data class CategoryOption(
    val id: Int,
    val name: String
)