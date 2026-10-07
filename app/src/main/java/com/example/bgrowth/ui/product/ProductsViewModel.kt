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

class ProductsViewModel(
    private val repository: ProductRepository =
        ProductRepository()
) : ViewModel() {

    private val _uiState =
        MutableStateFlow(
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

            try {

                val products =
                    repository.getProducts()

                val categories =
                    repository.getCategories()

                val categoryNamesById =
                    categories.associate {
                        it.id to it.name
                    }

                val productItems =
                    products.map { product ->

                        ProductListItem(
                            id = product.id,

                            name =
                                product.name,

                            categoryId =
                                product.category,

                            categoryName =
                                product.category
                                    ?.let {
                                        categoryNamesById[it]
                                    }
                                    .orEmpty(),

                            price =
                                product.sellingPrice
                                    .toDoubleOrNull()
                                    ?: 0.0,

                            stockQuantity =
                                product.quantity,

                            minStockLevel =
                                product.minimumStock
                        )
                    }

                _uiState.update {
                    it.copy(
                        products =
                            productItems,

                        isLoading = false,

                        errorMessage =
                            null
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
                                    "Business was not found."

                                else ->
                                    "Failed to load products."
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
                                ?: "Failed to load products."
                    )
                }
            }
        }
    }

    fun onSearchQueryChange(
        value: String
    ) {

        _uiState.update {
            it.copy(
                searchQuery = value
            )
        }
    }

    fun onCategorySelected(
        category: String?
    ) {

        _uiState.update {
            it.copy(
                selectedCategory =
                    category
            )
        }
    }

    fun openProductMenu(
        productId: Int
    ) {

        _uiState.update {
            it.copy(
                openedMenuProductId =
                    productId
            )
        }
    }

    fun closeProductMenu() {

        _uiState.update {
            it.copy(
                openedMenuProductId =
                    null
            )
        }
    }

    fun requestDeleteProduct(
        product: ProductListItem
    ) {

        _uiState.update {
            it.copy(
                openedMenuProductId =
                    null,

                productPendingDelete =
                    product,

                errorMessage =
                    null
            )
        }
    }

    fun cancelDeleteProduct() {

        _uiState.update {
            it.copy(
                productPendingDelete =
                    null
            )
        }
    }

    fun confirmDeleteProduct() {

        val product =
            _uiState
                .value
                .productPendingDelete
                ?: return

        viewModelScope.launch {

            _uiState.update {
                it.copy(
                    isDeleting = true,
                    errorMessage = null
                )
            }

            try {

                repository.deleteProduct(
                    productId = product.id
                )

                /*
                 * بعد نجاح DELETE
                 * نعيد القراءة من السيرفر.
                 */
                val products =
                    repository.getProducts()

                val categories =
                    repository.getCategories()

                val categoryNamesById =
                    categories.associate {
                        it.id to it.name
                    }

                val productItems =
                    products.map { item ->

                        ProductListItem(
                            id = item.id,

                            name = item.name,

                            categoryId =
                                item.category,

                            categoryName =
                                item.category
                                    ?.let {
                                        categoryNamesById[it]
                                    }
                                    .orEmpty(),

                            price =
                                item.sellingPrice
                                    .toDoubleOrNull()
                                    ?: 0.0,

                            stockQuantity =
                                item.quantity,

                            minStockLevel =
                                item.minimumStock
                        )
                    }

                _uiState.update {
                    it.copy(
                        products =
                            productItems,

                        productPendingDelete =
                            null,

                        isDeleting =
                            false,

                        errorMessage =
                            null
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
                        isDeleting = false,

                        productPendingDelete =
                            null,

                        errorMessage =
                            when (e.code()) {

                                400 ->
                                    "This product cannot be deleted because it has sales."

                                401 ->
                                    "Your session has expired. Please log in again."

                                404 ->
                                    "Product was not found."

                                else ->
                                    "Failed to delete product."
                            }
                    )
                }

            } catch (
                e: IOException
            ) {

                _uiState.update {
                    it.copy(
                        isDeleting = false,

                        errorMessage =
                            "Unable to connect to the server."
                    )
                }

            } catch (
                e: Exception
            ) {

                _uiState.update {
                    it.copy(
                        isDeleting = false,

                        errorMessage =
                            e.message
                                ?: "Failed to delete product."
                    )
                }
            }
        }
    }
}