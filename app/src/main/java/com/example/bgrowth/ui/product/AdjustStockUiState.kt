package com.example.bgrowth.ui.product

enum class StockMovementType {
    IN,
    OUT,
    ADJUSTMENT
}

data class AdjustStockUiState(
    val productId: Int? = null,
    val productName: String = "",
    val currentQuantity: Int = 0,

    val movementType: StockMovementType = StockMovementType.IN,
    val quantity: String = "",
    val reason: String = "",

    val quantityError: String? = null,

    val isLoading: Boolean = false,
    val isSaving: Boolean = false,
    val errorMessage: String? = null,

    val isStockUpdated: Boolean = false
)