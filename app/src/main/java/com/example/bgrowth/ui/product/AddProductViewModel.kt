package com.example.bgrowth.ui.product

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class AddProductViewModel : ViewModel() {

    private val _uiState =
        MutableStateFlow(AddProductUiState())

    val uiState: StateFlow<AddProductUiState> =
        _uiState.asStateFlow()

    fun onProductNameChange(value: String) {
        _uiState.update {
            it.copy(
                productName = value,
                productNameError = null
            )
        }
    }

    fun onCategoryNameChange(value: String) {
        _uiState.update {
            it.copy(
                categoryName = value,
                categoryError = null
            )
        }
    }

    fun onDescriptionChange(value: String) {
        _uiState.update {
            it.copy(
                description = value
            )
        }
    }

    fun onPriceChange(value: String) {
        if (isValidDecimalInput(value)) {
            _uiState.update {
                it.copy(
                    price = value,
                    priceError = null
                )
            }
        }
    }

    fun onCostChange(value: String) {
        if (isValidDecimalInput(value)) {
            _uiState.update {
                it.copy(
                    cost = value,
                    costError = null
                )
            }
        }
    }

    fun onOpeningStockChange(value: String) {
        if (value.all { it.isDigit() }) {
            _uiState.update {
                it.copy(
                    openingStock = value,
                    openingStockError = null
                )
            }
        }
    }

    fun onMinStockLevelChange(value: String) {
        if (value.all { it.isDigit() }) {
            _uiState.update {
                it.copy(
                    minStockLevel = value,
                    minStockLevelError = null
                )
            }
        }
    }

    fun onTrackStockChange(enabled: Boolean) {
        _uiState.update {
            it.copy(
                trackStock = enabled,

                openingStockError =
                    if (enabled) it.openingStockError else null,

                minStockLevelError =
                    if (enabled) it.minStockLevelError else null
            )
        }
    }

    fun saveProduct() {

        val state = _uiState.value

        val productNameError =
            if (state.productName.isBlank()) {
                "Product name is required"
            } else {
                null
            }

        val categoryError =
            if (state.categoryName.isBlank()) {
                "Category is required"
            } else {
                null
            }

        val priceValue =
            state.price.toDoubleOrNull()

        val priceError =
            if (
                priceValue == null ||
                priceValue < 0
            ) {
                "Enter a valid price"
            } else {
                null
            }

        val costValue =
            state.cost
                .takeIf { it.isNotBlank() }
                ?.toDoubleOrNull()

        val costError =
            if (
                state.cost.isNotBlank() &&
                (costValue == null || costValue < 0)
            ) {
                "Enter a valid cost"
            } else {
                null
            }

        val openingStockValue =
            state.openingStock.toIntOrNull()

        val openingStockError =
            if (
                state.trackStock &&
                (
                        openingStockValue == null ||
                                openingStockValue < 0
                        )
            ) {
                "Enter valid opening stock"
            } else {
                null
            }

        val minStockLevelValue =
            state.minStockLevel.toIntOrNull()

        val minStockLevelError =
            if (
                state.trackStock &&
                (
                        minStockLevelValue == null ||
                                minStockLevelValue < 0
                        )
            ) {
                "Enter valid minimum stock"
            } else {
                null
            }

        val hasErrors =
            productNameError != null ||
                    categoryError != null ||
                    priceError != null ||
                    costError != null ||
                    openingStockError != null ||
                    minStockLevelError != null

        if (hasErrors) {

            _uiState.update {
                it.copy(
                    productNameError = productNameError,
                    categoryError = categoryError,
                    priceError = priceError,
                    costError = costError,
                    openingStockError = openingStockError,
                    minStockLevelError = minStockLevelError
                )
            }

            return
        }

        // TODO: Replace with real Add Product API.

        _uiState.update {
            it.copy(
                isProductSaved = true
            )
        }
    }

    fun consumeProductSaved() {
        _uiState.update {
            it.copy(
                isProductSaved = false
            )
        }
    }

    private fun isValidDecimalInput(
        value: String
    ): Boolean {

        return value.isEmpty() ||
                value.matches(
                    Regex("""^\d*\.?\d*$""")
                )
    }
}