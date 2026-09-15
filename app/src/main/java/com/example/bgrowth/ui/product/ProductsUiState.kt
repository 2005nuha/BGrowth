package com.example.bgrowth.ui.product

data class ProductListItem(
    val id: Int,
    val name: String,
    val category: String,
    val price: Double,

    val trackStock: Boolean = true,
    val stockQuantity: Int? = null,
    val minStockLevel: Int? = null
) {

    val isOutOfStock: Boolean
        get() =
            trackStock &&
                    stockQuantity != null &&
                    stockQuantity <= 0

    val isLowStock: Boolean
        get() =
            trackStock &&
                    stockQuantity != null &&
                    minStockLevel != null &&
                    stockQuantity > 0 &&
                    stockQuantity <= minStockLevel
}

data class ProductsUiState(
    val products: List<ProductListItem> = emptyList(),

    val searchQuery: String = "",

    val selectedCategory: String? = null,

    val isLoading: Boolean = false,

    val errorMessage: String? = null,

    val openedMenuProductId: Int? = null,

    val productPendingDelete: ProductListItem? = null,
) {

    val categories: List<String>
        get() =
            products
                .map { it.category }
                .filter { it.isNotBlank() }
                .distinct()

    val filteredProducts: List<ProductListItem>
        get() {

            var result = products

            if (!selectedCategory.isNullOrBlank()) {
                result =
                    result.filter {
                        it.category.equals(
                            selectedCategory,
                            ignoreCase = true
                        )
                    }
            }

            if (searchQuery.isNotBlank()) {
                result =
                    result.filter {
                        it.name.contains(
                            searchQuery,
                            ignoreCase = true
                        ) ||
                                it.category.contains(
                                    searchQuery,
                                    ignoreCase = true
                                )
                    }
            }

            return result
        }
}