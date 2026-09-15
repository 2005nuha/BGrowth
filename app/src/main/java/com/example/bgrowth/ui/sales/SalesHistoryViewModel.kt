package com.example.bgrowth.ui.sales

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class SalesHistoryViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(
        SalesHistoryUiState(
            sales = listOf(
                SalesHistoryItem(
                    id = 1,
                    productName = "Arabic coffee 250g",
                    quantity = 2,
                    unitPrice = 45.0,
                    paymentMethod = "Card",
                    time = "10:24 AM",
                    dateLabel = "Today, Sep 7"
                ),
                SalesHistoryItem(
                    id = 2,
                    productName = "Thermal cup",
                    quantity = 1,
                    unitPrice = 25.0,
                    paymentMethod = "Cash",
                    time = "09:50 AM",
                    dateLabel = "Today, Sep 7"
                ),
                SalesHistoryItem(
                    id = 3,
                    productName = "Turkish coffee 200g",
                    quantity = 3,
                    unitPrice = 21.0,
                    paymentMethod = "Credit",
                    time = "09:12 AM",
                    dateLabel = "Today, Sep 7"
                ),
                SalesHistoryItem(
                    id = 4,
                    productName = "Potato Chips",
                    quantity = 5,
                    unitPrice = 1.99,
                    paymentMethod = "Card",
                    time = "10:24 AM",
                    dateLabel = "Yesterday, Sep 6"
                ),
                SalesHistoryItem(
                    id = 5,
                    productName = "Fresh Milk",
                    quantity = 4,
                    unitPrice = 6.25,
                    paymentMethod = "Cash",
                    time = "09:50 AM",
                    dateLabel = "Yesterday, Sep 6"
                ),
                SalesHistoryItem(
                    id = 6,
                    productName = "Chocolate",
                    quantity = 1,
                    unitPrice = 12.60,
                    paymentMethod = "Card",
                    time = "10:24 AM",
                    dateLabel = "Sep 5"
                )
            )
        )
    )

    val uiState: StateFlow<SalesHistoryUiState> =
        _uiState.asStateFlow()

    fun onSearchQueryChange(value: String) {
        _uiState.update {
            it.copy(
                searchQuery = value
            )
        }
    }

    fun onPeriodSelected(period: SalesPeriodFilter) {
        _uiState.update {
            it.copy(
                selectedPeriod = period
            )
        }
    }
}