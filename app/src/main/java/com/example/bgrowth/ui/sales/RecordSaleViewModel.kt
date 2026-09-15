package com.example.bgrowth.ui.sales

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class RecordSaleViewModel : ViewModel() {

    private val _uiState =
        MutableStateFlow(RecordSaleUiState())

    val uiState: StateFlow<RecordSaleUiState> =
        _uiState.asStateFlow()

    fun onProductNameChange(value: String) {
        _uiState.update {
            it.copy(
                productName = value,
                productError = null
            )
        }
    }

    fun onQuantityChange(value: String) {
        if (value.all { it.isDigit() }) {
            _uiState.update {
                it.copy(
                    quantity = value,
                    quantityError = null
                )
            }
        }
    }

    fun onUnitPriceChange(value: String) {

        val validInput =
            value.isEmpty() ||
                    value.matches(
                        Regex("""^\d*\.?\d*$""")
                    )

        if (validInput) {
            _uiState.update {
                it.copy(
                    unitPrice = value,
                    unitPriceError = null
                )
            }
        }
    }

    fun onPaymentMethodChange(value: String) {
        _uiState.update {
            it.copy(
                paymentMethod = value,
                paymentMethodError = null
            )
        }
    }

    fun saveSale() {

        val state = _uiState.value

        val productError =
            if (state.productName.isBlank()) {
                "Product or service name is required"
            } else {
                null
            }

        val quantityNumber =
            state.quantity.toIntOrNull()

        val quantityError =
            if (
                quantityNumber == null ||
                quantityNumber <= 0
            ) {
                "Enter a valid quantity"
            } else {
                null
            }

        val priceNumber =
            state.unitPrice.toDoubleOrNull()

        val unitPriceError =
            if (
                priceNumber == null ||
                priceNumber < 0
            ) {
                "Enter a valid price"
            } else {
                null
            }

        val paymentMethodError =
            if (state.paymentMethod.isBlank()) {
                "Payment method is required"
            } else {
                null
            }

        val hasErrors =
            productError != null ||
                    quantityError != null ||
                    unitPriceError != null ||
                    paymentMethodError != null

        if (hasErrors) {

            _uiState.update {
                it.copy(
                    productError = productError,
                    quantityError = quantityError,
                    unitPriceError = unitPriceError,
                    paymentMethodError = paymentMethodError
                )
            }

            return
        }

        // TODO: Replace with real Create Sale API.

        val newSale =
            TodaySaleItem(
                id = state.todaySales.size + 1,

                // null because this sale is currently entered
                // as free text.
                // Later, if linked to a saved product,
                // use its real product ID here.
                productId = null,

                productName =
                    state.productName.trim(),

                quantity =
                    quantityNumber!!,

                unitPrice =
                    priceNumber!!,

                paymentMethod =
                    state.paymentMethod.trim(),

                time =
                    currentTime()
            )

        _uiState.update {
            it.copy(
                todaySales =
                    listOf(newSale) + it.todaySales,

                productName = "",
                quantity = "",
                unitPrice = "",
                paymentMethod = "",

                productError = null,
                quantityError = null,
                unitPriceError = null,
                paymentMethodError = null
            )
        }
    }

    private fun currentTime(): String {

        return SimpleDateFormat(
            "hh:mm a",
            Locale.getDefault()
        ).format(Date())
    }
}