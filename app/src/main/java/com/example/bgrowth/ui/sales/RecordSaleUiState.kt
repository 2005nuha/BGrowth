package com.example.bgrowth.ui.sales

data class TodaySaleItem(
    val id: Int,

    // Optional:
    // If this sale belongs to an existing saved product,
    // productId can be used later for stock updates.
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

data class RecordSaleUiState(

    // Free text instead of forcing the user
    // to choose from a fixed dropdown.
    val productName: String = "",

    val quantity: String = "",
    val unitPrice: String = "",

    // Free text so every business can enter
    // the payment method that fits its work.
    val paymentMethod: String = "",

    val todaySales: List<TodaySaleItem> = emptyList(),

    val productError: String? = null,
    val quantityError: String? = null,
    val unitPriceError: String? = null,
    val paymentMethodError: String? = null,

    val isLoading: Boolean = false,
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
}