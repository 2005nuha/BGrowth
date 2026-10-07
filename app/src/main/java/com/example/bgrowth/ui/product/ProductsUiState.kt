package com.example.bgrowth.ui.product

data class ProductListItem(
    val id: Int,
    val name: String,
    val categoryId: Int?,
    val categoryName: String,
    val price: Double,
    val stockQuantity: Int,
    val minStockLevel: Int
) {

    val isOutOfStock: Boolean
        get() =
            stockQuantity == 0

    val isLowStock: Boolean
        get() =
            stockQuantity > 0 &&
                    stockQuantity <= minStockLevel
}

data class ProductsUiState(
    val products: List<ProductListItem> = emptyList(),

    val searchQuery: String = "",

    val selectedCategory: String? = null,

    val isLoading: Boolean = false,

    val isDeleting: Boolean = false,

    val errorMessage: String? = null,

    val openedMenuProductId: Int? = null,

    val productPendingDelete: ProductListItem? = null
) {

    val categories: List<String>
        get() =
            products
                .map { it.categoryName }
                .filter { it.isNotBlank() }
                .distinct()

    val filteredProducts: List<ProductListItem>
        get() {

            var result = products

            if (!selectedCategory.isNullOrBlank()) {

                result =
                    result.filter {
                        it.categoryName.equals(
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
                                it.categoryName.contains(
                                    searchQuery,
                                    ignoreCase = true
                                )
                    }
            }

            return result
        }
}