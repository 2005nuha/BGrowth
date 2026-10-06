package com.example.bgrowth.ui.product

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bgrowth.data.repository.ProductRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ProductsViewModel(
    private val repository: ProductRepository = ProductRepository()
) : ViewModel() {

    private val _uiState = MutableStateFlow(
        ProductsUiState()
    )

    val uiState: StateFlow<ProductsUiState> =
        _uiState.asStateFlow()

    init {
        loadProducts()
    }

    fun loadProducts() {

        viewModelScope.launch {

            _uiState.update {
                it.copy(
                    isLoading = true,
                    errorMessage = null
                )
            }

            repository.getProducts()
                .onSuccess { products ->

                    val productItems = products.map { product ->

                        ProductListItem(
                            id = product.id,
                            name = product.name,
                            category = product.category?.toString() ?: "",
                            price = product.selling_price
                                .toDoubleOrNull() ?: 0.0,
                            trackStock = true,
                            stockQuantity = product.quantity,
                            minStockLevel = product.minimum_stock
                        )
                    }

                    _uiState.update {
                        it.copy(
                            products = productItems,
                            isLoading = false,
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
                                    ?: "Failed to load products."
                        )
                    }
                }
        }
    }

    fun onSearchQueryChange(value: String) {

        _uiState.update {
            it.copy(
                searchQuery = value
            )
        }
    }

    fun onCategorySelected(category: String?) {

        _uiState.update {
            it.copy(
                selectedCategory = category
            )
        }
    }

    fun openProductMenu(productId: Int) {

        _uiState.update {
            it.copy(
                openedMenuProductId = productId
            )
        }
    }

    fun closeProductMenu() {

        _uiState.update {
            it.copy(
                openedMenuProductId = null
            )
        }
    }

    fun requestDeleteProduct(
        product: ProductListItem
    ) {

        _uiState.update {
            it.copy(
                openedMenuProductId = null,
                productPendingDelete = product
            )
        }
    }

    fun cancelDeleteProduct() {

        _uiState.update {
            it.copy(
                productPendingDelete = null
            )
        }
    }

    fun confirmDeleteProduct() {

        val product =
            _uiState.value.productPendingDelete
                ?: return

        // مؤقتًا نحذف من الواجهة فقط.
        // لاحقًا نربطه مع Delete Product API.

        _uiState.update {

            it.copy(
                products = it.products.filter { item ->
                    item.id != product.id
                },
                productPendingDelete = null
            )
        }
    }
}