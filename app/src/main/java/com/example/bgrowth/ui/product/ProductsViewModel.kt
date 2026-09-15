package com.example.bgrowth.ui.product

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class ProductsViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(
        ProductsUiState(
            products = listOf(
                ProductListItem(
                    id = 1,
                    name = "Potato Chips",
                    category = "Snacks",
                    price = 1.80,
                    trackStock = true,
                    stockQuantity = 24,
                    minStockLevel = 10
                ),
                ProductListItem(
                    id = 2,
                    name = "Arabic Coffee",
                    category = "Coffee & Tea",
                    price = 25.00,
                    trackStock = true,
                    stockQuantity = 32,
                    minStockLevel = 10
                ),
                ProductListItem(
                    id = 3,
                    name = "Thermal Cup",
                    category = "Cups & Mugs",
                    price = 18.00,
                    trackStock = true,
                    stockQuantity = 4,
                    minStockLevel = 5
                ),
                ProductListItem(
                    id = 4,
                    name = "Turkish Coffee",
                    category = "Coffee & Tea",
                    price = 21.00,
                    trackStock = true,
                    stockQuantity = 0,
                    minStockLevel = 5
                )
            )
        )
    )

    val uiState: StateFlow<ProductsUiState> =
        _uiState.asStateFlow()

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

    fun requestDeleteProduct(product: ProductListItem) {
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

        _uiState.update {
            it.copy(
                products = it.products.filter { item ->
                    item.id != product.id
                },
                productPendingDelete = null
            )
        }

        // TODO: Replace with Delete Product API.
    }
}