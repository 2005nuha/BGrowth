package com.example.bgrowth.ui.addexpence

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class AddExpenseViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(AddExpenseUiState())
    val uiState: StateFlow<AddExpenseUiState> = _uiState.asStateFlow()

    init {
        loadRecentExpenses()
    }

    private fun loadRecentExpenses() {
        val mockData = listOf(
            RecentExpenseItem(1, "Electricity Bill", "Utilities · Today, 9:15 AM", "-$45.00"),
            RecentExpenseItem(2, "Packaging Supplies", "Supplies · Yesterday", "-$120.00"),
            RecentExpenseItem(3, "Delivery Fuel", "Transport · Yesterday", "-$16.00")
        )
        _uiState.update { it.copy(recentExpenses = mockData) }
    }

    fun onExpenseNameChange(name: String) {
        _uiState.update { it.copy(expenseName = name) }
    }

    fun onCategoryChange(category: String) {
        _uiState.update { it.copy(category = category) }
    }

    fun onPriceChange(price: String) {
        _uiState.update { it.copy(price = price) }
    }

    fun onDateChange(date: String) {
        _uiState.update { it.copy(date = date) }
    }

    fun onPaymentMethodChange(method: String) {
        _uiState.update { it.copy(paymentMethod = method) }
    }

    fun toggleCategoryDropdown() {
        _uiState.update { it.copy(isCategoryDropdownExpanded = !it.isCategoryDropdownExpanded) }
    }

    fun togglePaymentMethodDropdown() {
        _uiState.update { it.copy(isPaymentMethodDropdownExpanded = !it.isPaymentMethodDropdownExpanded) }
    }

    fun onSaveExpense() {
    }
}