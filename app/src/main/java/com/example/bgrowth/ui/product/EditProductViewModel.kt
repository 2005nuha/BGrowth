package com.example.bgrowth.ui.product

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bgrowth.data.model.UpdateProductRequest
import com.example.bgrowth.data.repository.ProductRepository
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

class EditProductViewModel(
    private val repository: ProductRepository =
        ProductRepository()
) : ViewModel() {

    private val _uiState =
        MutableStateFlow(
            EditProductUiState()
        )

    val uiState: StateFlow<EditProductUiState> =
        _uiState.asStateFlow()

    private var loadedProductId: Int? = null

    fun loadProduct(
        productId: Int
    ) {

        if (
            loadedProductId == productId &&
            _uiState.value.productId == productId
        ) {
            return
        }

        loadedProductId = productId

        viewModelScope.launch {

            _uiState.update {
                it.copy(
                    isLoading = true,
                    errorMessage = null
                )
            }

            try {

                val product =
                    repository.getProduct(
                        productId = productId
                    )

                val categories =
                    repository.getCategories()

                val categoryOptions =
                    categories.map {
                        CategoryOption(
                            id = it.id,
                            name = it.name
                        )
                    }

                val selectedCategory =
                    product.category?.let { id ->
                        categories.firstOrNull {
                            it.id == id
                        }
                    }

                _uiState.update {
                    it.copy(
                        productId = product.id,
                        productName = product.name,
                        categoryId = product.category,
                        categoryName =
                            selectedCategory?.name.orEmpty(),
                        description = product.description,
                        price = product.sellingPrice,
                        cost = product.costPrice.orEmpty(),
                        minStockLevel =
                            product.minimumStock.toString(),
                        categories = categoryOptions,
                        isLoading = false,
                        errorMessage = null
                    )
                }

            } catch (
                e: CancellationException
            ) {

                throw e

            } catch (
                e: HttpException
            ) {

                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage =
                            when (e.code()) {

                                401 ->
                                    "Your session has expired. Please log in again."

                                404 ->
                                    "Product was not found."

                                else ->
                                    "Failed to load product."
                            }
                    )
                }

            } catch (
                e: IOException
            ) {

                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage =
                            "Unable to connect to the server."
                    )
                }

            } catch (
                e: Exception
            ) {

                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage =
                            e.message
                                ?: "Failed to load product."
                    )
                }
            }
        }
    }

    fun onProductNameChange(
        value: String
    ) {

        _uiState.update {
            it.copy(
                productName = value,
                productNameError = null,
                errorMessage = null
            )
        }
    }

    fun onCategorySelected(
        category: CategoryOption?
    ) {

        _uiState.update {
            it.copy(
                categoryId = category?.id,
                categoryName = category?.name.orEmpty(),
                errorMessage = null
            )
        }
    }

    fun onDescriptionChange(
        value: String
    ) {

        _uiState.update {
            it.copy(
                description = value,
                errorMessage = null
            )
        }
    }

    fun onPriceChange(
        value: String
    ) {

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

    fun onCostChange(
        value: String
    ) {

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

    fun onMinStockLevelChange(
        value: String
    ) {

        if (
            value.isEmpty() ||
            value.all { char ->
                char.isDigit()
            }
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

    fun updateProduct() {

        val state =
            _uiState.value

        val productId =
            state.productId
                ?: return

        val productName =
            state.productName.trim()

        val price =
            state.price.toDoubleOrNull()

        val cost =
            state.cost
                .takeIf {
                    it.isNotBlank()
                }
                ?.toDoubleOrNull()

        val minStock =
            state.minStockLevel
                .takeIf {
                    it.isNotBlank()
                }
                ?.toIntOrNull()
                ?: 0

        val productNameError =
            if (productName.isBlank()) {
                "Product name is required"
            } else {
                null
            }

        val priceError =
            if (
                price == null ||
                price < 0
            ) {
                "Enter a valid price"
            } else {
                null
            }

        val costError =
            if (
                state.cost.isNotBlank() &&
                (
                        cost == null ||
                                cost < 0
                        )
            ) {
                "Enter a valid cost"
            } else {
                null
            }

        val minStockLevelError =
            if (
                state.minStockLevel.isNotBlank() &&
                state.minStockLevel.toIntOrNull() == null
            ) {
                "Enter a valid minimum stock"
            } else {
                null
            }

        if (
            productNameError != null ||
            priceError != null ||
            costError != null ||
            minStockLevelError != null
        ) {

            _uiState.update {
                it.copy(
                    productNameError =
                        productNameError,

                    priceError =
                        priceError,

                    costError =
                        costError,

                    minStockLevelError =
                        minStockLevelError
                )
            }

            return
        }

        viewModelScope.launch {

            _uiState.update {
                it.copy(
                    isSaving = true,
                    errorMessage = null
                )
            }

            try {

                val request =
                    UpdateProductRequest(
                        name = productName,

                        sellingPrice =
                            state.price,

                        category =
                            state.categoryId,

                        description =
                            state.description.trim(),

                        costPrice =
                            state.cost.takeIf {
                                it.isNotBlank()
                            },

                        minimumStock =
                            minStock
                    )

                repository.updateProduct(
                    productId = productId,
                    request = request
                )

                _uiState.update {
                    it.copy(
                        isSaving = false,
                        isProductUpdated = true,
                        errorMessage = null
                    )
                }

            } catch (
                e: CancellationException
            ) {

                throw e

            } catch (
                e: HttpException
            ) {

                _uiState.update {
                    it.copy(
                        isSaving = false,
                        errorMessage =
                            when (e.code()) {

                                400 ->
                                    "Please check the product information."

                                401 ->
                                    "Your session has expired. Please log in again."

                                404 ->
                                    "Product was not found."

                                else ->
                                    "Failed to update product."
                            }
                    )
                }

            } catch (
                e: IOException
            ) {

                _uiState.update {
                    it.copy(
                        isSaving = false,
                        errorMessage =
                            "Unable to connect to the server."
                    )
                }

            } catch (
                e: Exception
            ) {

                _uiState.update {
                    it.copy(
                        isSaving = false,
                        errorMessage =
                            e.message
                                ?: "Failed to update product."
                    )
                }
            }
        }
    }

    fun consumeProductUpdated() {

        _uiState.update {
            it.copy(
                isProductUpdated = false
            )
        }
    }
}