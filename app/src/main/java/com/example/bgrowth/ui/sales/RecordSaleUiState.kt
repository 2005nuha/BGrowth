package com.example.bgrowth.ui.sales

import com.example.bgrowth.data.model.Product

data class TodaySaleItem(
    val id: Int,
    val productId: Int? = null,
    val productName: String,
    val quantity: Int,
    val unitPrice: Double,
    val paymentMethod: String,
    val time: String
) {
    val total: Double
        get() = quantity * unitPrice
}
data class SaleCartItem(
    val product: Product,
    val quantity: Int = 1
)

data class RecordSaleUiState(

    // Existing fields used by RecordSaleScreen
    val productName: String = "",
    val quantity: String = "",
    val unitPrice: String = "",
    val paymentMethod: String = "",
    val cartItems: List<SaleCartItem> = emptyList(),
    val productSearchQuery: String = "",
    val todaySales: List<TodaySaleItem> = emptyList(),

    // Products loaded from Backend
    val products: List<Product> = emptyList(),

    // Selected product
    val selectedProductId: Int? = null,

    // Amount received from customer
    val paidAmount: String = "",

    // Validation
    val productError: String? = null,
    val quantityError: String? = null,
    val unitPriceError: String? = null,
    val paymentMethodError: String? = null,

    // UI states
    val isLoading: Boolean = false,
    val isSaving: Boolean = false,
    val isSuccess: Boolean = false,
    val errorMessage: String? = null

) {
    val total: Double
        get() {
            val quantityValue =
                quantity.toIntOrNull() ?: 0

            val priceValue =
                unitPrice.toDoubleOrNull() ?: 0.0

            return quantityValue * priceValue
        }
    val cartTotal: Double
        get() = cartItems.sumOf { item ->
            (item.product.sellingPrice.toDoubleOrNull() ?: 0.0) *
                    item.quantity
        }
}