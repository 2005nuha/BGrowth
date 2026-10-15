package com.example.bgrowth.ui.sales

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bgrowth.data.model.CreateSaleItemRequest
import com.example.bgrowth.data.model.CreateSaleRequest
import com.example.bgrowth.data.model.Product
import com.example.bgrowth.data.remote.RetrofitClient
import com.example.bgrowth.data.repository.ProductRepository
import com.example.bgrowth.data.repository.SaleRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.math.BigDecimal
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class RecordSaleViewModel : ViewModel() {

    private val productRepository = ProductRepository()
    private val saleRepository = SaleRepository(RetrofitClient.saleApi)

    private val _uiState = MutableStateFlow(RecordSaleUiState())
    val uiState: StateFlow<RecordSaleUiState> = _uiState.asStateFlow()

    init {
        loadProducts()
    }

    fun loadProducts() {
        viewModelScope.launch {
            _uiState.update {
                it.copy(isLoading = true, errorMessage = null)
            }

            try {
                val products = productRepository.getProducts()

                _uiState.update {
                    it.copy(
                        products = products,
                        isLoading = false
                    )
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = e.message ?: "Failed to load products"
                    )
                }
            }
        }
    }

    fun onProductSelected(product: Product) {
        if (_uiState.value.isSaving) return

        _uiState.update {
            it.copy(
                selectedProductId = product.id,
                productName = product.name,
                unitPrice = product.sellingPrice,
                productError = null,
                unitPriceError = null,
                errorMessage = null
            )
        }
    }

    // Kept for compatibility with the existing screen.
    // Typing a product name does not select a backend product.
    fun onProductNameChange(value: String) {
        _uiState.update {
            it.copy(
                productName = value,
                selectedProductId = null,
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

    // Kept for compatibility. The backend uses the saved product price.
    fun onUnitPriceChange(value: String) {
        if (value.isEmpty() || value.matches(Regex("""^\d*\.?\d*$"""))) {
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

    fun onPaidAmountChange(value: String) {
        if (value.isEmpty() || value.matches(Regex("""^\d*\.?\d*$"""))) {
            _uiState.update {
                it.copy(paidAmount = value, errorMessage = null)
            }
        }
    }
    fun saveSale() {
        val state = _uiState.value

        if (state.isSaving) return

        if (state.cartItems.isEmpty()) {
            _uiState.update {
                it.copy(errorMessage = "Please add at least one product")
            }
            return
        }

        val invalidItem = state.cartItems.firstOrNull {
            it.quantity <= 0 || it.quantity > it.product.quantity
        }

        if (invalidItem != null) {
            _uiState.update {
                it.copy(
                    errorMessage = "Invalid quantity or insufficient stock: ${invalidItem.product.name}"
                )
            }
            return
        }

        val total = state.cartItems.fold(BigDecimal.ZERO) { sum, item ->
            val price = item.product.sellingPrice.toBigDecimalOrNull()

            if (price == null || price < BigDecimal.ZERO) {
                _uiState.update {
                    it.copy(errorMessage = "Invalid price: ${item.product.name}")
                }
                return
            }

            sum.add(
                price.multiply(
                    BigDecimal.valueOf(item.quantity.toLong())
                )
            )
        }

        val paid = if (state.paidAmount.isBlank()) {
            BigDecimal.ZERO
        } else {
            state.paidAmount.toBigDecimalOrNull()
        }

        if (paid == null || paid < BigDecimal.ZERO || paid > total) {
            _uiState.update {
                it.copy(
                    errorMessage = "Paid amount must be between 0 and total"
                )
            }
            return
        }

        val request = CreateSaleRequest(
            items = state.cartItems.map { item ->
                CreateSaleItemRequest(
                    product = item.product.id,
                    quantity = item.quantity
                )
            },
            paidAmount = paid.toPlainString()
        )

        viewModelScope.launch {
            _uiState.update {
                it.copy(isSaving = true, errorMessage = null)
            }

            try {
                val savedSale = saleRepository.createSale(request)

                val newSales = state.cartItems.map { item ->
                    TodaySaleItem(
                        id = savedSale.id,
                        productId = item.product.id,
                        productName = item.product.name,
                        quantity = item.quantity,
                        unitPrice = item.product.sellingPrice.toDoubleOrNull() ?: 0.0,
                        paymentMethod = if (paid > BigDecimal.ZERO) "Cash" else "Unpaid",
                        time = currentTime()
                    )
                }

                _uiState.update {
                    it.copy(
                        todaySales = newSales + it.todaySales,
                        cartItems = emptyList(),
                        productSearchQuery = "",
                        productName = "",
                        selectedProductId = null,
                        quantity = "",
                        unitPrice = "",
                        paymentMethod = "",
                        paidAmount = "",
                        isSaving = false,
                        isSuccess = true,
                        errorMessage = null
                    )
                }

                loadProducts()

            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isSaving = false,
                        errorMessage = e.message ?: "Failed to save sale"
                    )
                }
            }
        }
    }


    fun onProductSearchChange(value: String) {
        _uiState.update {
            it.copy(productSearchQuery = value)
        }
    }

    fun addProductToCart(product: Product) {
        _uiState.update { state ->
            if (state.isSaving || product.quantity <= 0) {
                return@update state
            }

            val existingItem = state.cartItems.find {
                it.product.id == product.id
            }

            val updatedCart = if (existingItem == null) {
                state.cartItems + SaleCartItem(
                    product = product,
                    quantity = 1
                )
            } else {
                state.cartItems.map { item ->
                    if (item.product.id == product.id &&
                        item.quantity < product.quantity
                    ) {
                        item.copy(quantity = item.quantity + 1)
                    } else {
                        item
                    }
                }
            }

            state.copy(
                cartItems = updatedCart,
                errorMessage = null
            )
        }
    }

    fun updateCartQuantity(productId: Int, quantity: Int) {
        _uiState.update { state ->
            if (state.isSaving) return@update state

            val item = state.cartItems.find {
                it.product.id == productId
            } ?: return@update state

            if (quantity < 1 || quantity > item.product.quantity) {
                return@update state.copy(
                    errorMessage = "Invalid quantity or insufficient stock"
                )
            }

            state.copy(
                cartItems = state.cartItems.map {
                    if (it.product.id == productId) {
                        it.copy(quantity = quantity)
                    } else {
                        it
                    }
                },
                errorMessage = null
            )
        }
    }

    fun removeProductFromCart(productId: Int) {
        _uiState.update { state ->
            if (state.isSaving) return@update state

            state.copy(
                cartItems = state.cartItems.filterNot {
                    it.product.id == productId
                },
                errorMessage = null
            )
        }
    }

    private fun currentTime(): String =
        SimpleDateFormat(
            "hh:mm a",
            Locale.getDefault()
        ).format(Date())
}