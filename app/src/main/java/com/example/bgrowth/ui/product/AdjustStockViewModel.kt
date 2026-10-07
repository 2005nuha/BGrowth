package com.example.bgrowth.ui.product

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bgrowth.data.repository.ProductRepository
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

class AdjustStockViewModel(
    private val repository: ProductRepository = ProductRepository()
) : ViewModel() {

    private val _uiState =
        MutableStateFlow(AdjustStockUiState())

    val uiState: StateFlow<AdjustStockUiState> =
        _uiState.asStateFlow()

    private var loadedProductId: Int? = null

    fun loadProduct(productId: Int) {

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
                    repository.getProduct(productId)

                _uiState.update {
                    it.copy(
                        productId = product.id,
                        productName = product.name,
                        currentQuantity = product.quantity,
                        isLoading = false,
                        errorMessage = null
                    )
                }

            } catch (e: CancellationException) {

                throw e

            } catch (e: HttpException) {

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

            } catch (e: IOException) {

                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage =
                            "Unable to connect to the server."
                    )
                }

            } catch (e: Exception) {

                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage =
                            e.message ?: "Failed to load product."
                    )
                }
            }
        }
    }

    fun onMovementTypeChange(
        movementType: StockMovementType
    ) {

        _uiState.update {
            it.copy(
                movementType = movementType,
                quantityError = null,
                errorMessage = null
            )
        }
    }

    fun onQuantityChange(value: String) {

        if (
            value.isEmpty() ||
            value.all { it.isDigit() }
        ) {

            _uiState.update {
                it.copy(
                    quantity = value,
                    quantityError = null,
                    errorMessage = null
                )
            }
        }
    }

    fun onReasonChange(value: String) {

        // Backend allows a maximum of 255 characters.
        if (value.length <= 255) {

            _uiState.update {
                it.copy(
                    reason = value,
                    errorMessage = null
                )
            }
        }
    }

    fun adjustStock() {

        val state = _uiState.value

        val productId =
            state.productId ?: return

        val quantity =
            state.quantity.toIntOrNull()

        if (quantity == null || quantity < 0) {

            _uiState.update {
                it.copy(
                    quantityError =
                        "Enter a valid quantity"
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

                repository.adjustStock(
                    productId = productId,
                    quantity = quantity,
                    movementType =
                        state.movementType.name,
                    reason =
                        state.reason
                            .trim()
                            .takeIf {
                                it.isNotEmpty()
                            }
                )

                /*
                 * Re-read the product from the backend.
                 *
                 * We do not calculate the new stock locally.
                 * The backend is the source of truth.
                 */
                val refreshedProduct =
                    repository.getProduct(productId)

                _uiState.update {
                    it.copy(
                        currentQuantity =
                            refreshedProduct.quantity,
                        quantity = "",
                        reason = "",
                        isSaving = false,
                        isStockUpdated = true,
                        errorMessage = null
                    )
                }

            } catch (e: CancellationException) {

                throw e

            } catch (e: HttpException) {

                _uiState.update {
                    it.copy(
                        isSaving = false,
                        errorMessage =
                            when (e.code()) {

                                400 ->
                                    "Stock adjustment could not be completed. Check the quantity and available stock."

                                401 ->
                                    "Your session has expired. Please log in again."

                                404 ->
                                    "Product was not found."

                                else ->
                                    "Failed to update stock."
                            }
                    )
                }

            } catch (e: IOException) {

                _uiState.update {
                    it.copy(
                        isSaving = false,
                        errorMessage =
                            "Unable to connect to the server."
                    )
                }

            } catch (e: Exception) {

                _uiState.update {
                    it.copy(
                        isSaving = false,
                        errorMessage =
                            e.message ?: "Failed to update stock."
                    )
                }
            }
        }
    }

    fun consumeStockUpdated() {

        _uiState.update {
            it.copy(
                isStockUpdated = false
            )
        }
    }
}