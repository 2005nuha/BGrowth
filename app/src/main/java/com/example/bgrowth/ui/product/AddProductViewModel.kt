package com.example.bgrowth.ui.product

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bgrowth.data.model.CreateCategoryRequest
import com.example.bgrowth.data.model.CreateProductRequest
import com.example.bgrowth.data.repository.ProductRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AddProductViewModel(
    private val repository: ProductRepository = ProductRepository()
) : ViewModel() {

    private val _uiState =
        MutableStateFlow(AddProductUiState())

    val uiState: StateFlow<AddProductUiState> =
        _uiState.asStateFlow()

    fun onProductNameChange(value: String) {
        _uiState.update {
            it.copy(
                productName = value,
                productNameError = null,
                errorMessage = null
            )
        }
    }

    fun onCategoryNameChange(value: String) {
        _uiState.update {
            it.copy(
                categoryName = value,
                categoryError = null,
                errorMessage = null
            )
        }
    }

    fun onDescriptionChange(value: String) {
        _uiState.update {
            it.copy(
                description = value,
                errorMessage = null
            )
        }
    }

    fun onPriceChange(value: String) {
        if (
            value.isEmpty() ||
            value.matches(
                Regex("""^\d*\.?\d{0,2}$""")
            )
        ) {
            _uiState.update {
                it.copy(
                    price = value,
                    priceError = null,
                    errorMessage = null
                )
            }
        }
    }

    fun onCostChange(value: String) {
        if (
            value.isEmpty() ||
            value.matches(
                Regex("""^\d*\.?\d{0,2}$""")
            )
        ) {
            _uiState.update {
                it.copy(
                    cost = value,
                    costError = null,
                    errorMessage = null
                )
            }
        }
    }

    fun onOpeningStockChange(value: String) {
        if (
            value.isEmpty() ||
            value.all { char -> char.isDigit() }
        ) {
            _uiState.update {
                it.copy(
                    openingStock = value,
                    openingStockError = null,
                    errorMessage = null
                )
            }
        }
    }

    fun onMinStockLevelChange(value: String) {
        if (
            value.isEmpty() ||
            value.all { char -> char.isDigit() }
        ) {
            _uiState.update {
                it.copy(
                    minStockLevel = value,
                    minStockLevelError = null,
                    errorMessage = null
                )
            }
        }
    }

    fun onTrackStockChange(value: Boolean) {
        _uiState.update {
            it.copy(
                trackStock = value,
                openingStockError = null,
                minStockLevelError = null,
                errorMessage = null
            )
        }
    }

    fun saveProduct() {

        val state = _uiState.value

        val productName =
            state.productName.trim()

        val categoryName =
            state.categoryName.trim()

        val price =
            state.price.toDoubleOrNull()

        val cost =
            if (state.cost.isBlank()) {
                null
            } else {
                state.cost.toDoubleOrNull()
            }

        val openingStock =
            if (state.trackStock) {
                state.openingStock.toIntOrNull()
            } else {
                0
            }

        val minStock =
            if (state.trackStock) {
                state.minStockLevel.toIntOrNull()
            } else {
                0
            }

        val productNameError =
            if (productName.isBlank()) {
                "Product name is required"
            } else {
                null
            }

        val categoryError =
            if (categoryName.isBlank()) {
                "Category is required"
            } else {
                null
            }

        val priceError =
            if (price == null || price < 0) {
                "Enter a valid price"
            } else {
                null
            }

        val costError =
            if (
                state.cost.isNotBlank() &&
                (cost == null || cost < 0)
            ) {
                "Enter a valid cost"
            } else {
                null
            }

        val openingStockError =
            if (
                state.trackStock &&
                (openingStock == null || openingStock < 0)
            ) {
                "Enter a valid opening stock"
            } else {
                null
            }

        val minStockLevelError =
            if (
                state.trackStock &&
                (minStock == null || minStock < 0)
            ) {
                "Enter a valid minimum stock"
            } else {
                null
            }

        if (
            productNameError != null ||
            categoryError != null ||
            priceError != null ||
            costError != null ||
            openingStockError != null ||
            minStockLevelError != null
        ) {

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

        viewModelScope.launch {

            _uiState.update {
                it.copy(
                    isLoading = true,
                    errorMessage = null
                )
            }

            try {

                // 1. Get existing categories
                val categoriesResult =
                    repository.getCategories()

                val categories =
                    categoriesResult.getOrElse { error ->

                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                errorMessage =
                                    error.message
                                        ?: "Failed to load categories"
                            )
                        }

                        return@launch
                    }

                // 2. Check if the typed category already exists
                var categoryId =
                    categories
                        .firstOrNull {
                            it.name.equals(
                                categoryName,
                                ignoreCase = true
                            )
                        }
                        ?.id

                // 3. If not, create it
                if (categoryId == null) {

                    val categoryResult =
                        repository.createCategory(
                            CreateCategoryRequest(
                                name = categoryName
                            )
                        )

                    val createdCategory =
                        categoryResult.getOrElse { error ->

                            _uiState.update {
                                it.copy(
                                    isLoading = false,
                                    errorMessage =
                                        error.message
                                            ?: "Failed to create category"
                                )
                            }

                            return@launch
                        }

                    categoryId =
                        createdCategory.id
                }

                // 4. Create the actual product
                val productRequest =
                    CreateProductRequest(
                        category = categoryId,
                        name = productName,
                        description =
                            state.description.trim(),
                        selling_price =
                            state.price,
                        cost_price =
                            state.cost
                                .takeIf { it.isNotBlank() },
                        minimum_stock =
                            minStock ?: 0,
                        image = "",
                        initial_quantity =
                            openingStock ?: 0
                    )

                val productResult =
                    repository.createProduct(
                        productRequest
                    )

                productResult
                    .onSuccess {

                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                isProductSaved = true,
                                errorMessage = null
                            )
                        }
                    }
                    .onFailure { error ->

                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                errorMessage =
                                    error.message
                                        ?: "Failed to create product"
                            )
                        }
                    }

            } catch (e: Exception) {

                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage =
                            e.message
                                ?: "Something went wrong"
                    )
                }
            }
        }
    }

    fun consumeProductSaved() {
        _uiState.update {
            it.copy(
                isProductSaved = false
            )
        }
    }
}