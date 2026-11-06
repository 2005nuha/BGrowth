package com.example.bgrowth.ui.expenses.add

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bgrowth.data.repository.ExpenseRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AddExpenseViewModel(
    private val repository: ExpenseRepository = ExpenseRepository()
) : ViewModel() {

    private val _uiState = MutableStateFlow(AddExpenseUiState())
    val uiState: StateFlow<AddExpenseUiState> = _uiState.asStateFlow()

    fun onExpenseNameChange(name: String) {
        _uiState.update { it.copy(expenseName = name) }
    }

    fun onCategoryChange(category: String) {
        _uiState.update { it.copy(category = category, isCategoryDropdownExpanded = false) }
    }

    fun onPriceChange(price: String) {
        _uiState.update { it.copy(price = price, errorMessage = null) }
    }

    fun onDateChange(date: String) {
        _uiState.update { it.copy(date = date, errorMessage = null) }
    }

    fun onPaymentMethodChange(method: String) {
        _uiState.update { it.copy(paymentMethod = method, isPaymentMethodDropdownExpanded = false) }
    }

    fun toggleCategoryDropdown() {
        _uiState.update { it.copy(isCategoryDropdownExpanded = !it.isCategoryDropdownExpanded) }
    }

    fun togglePaymentMethodDropdown() {
        _uiState.update { it.copy(isPaymentMethodDropdownExpanded = !it.isPaymentMethodDropdownExpanded) }
    }

    fun onSaveExpense(onSuccess: () -> Unit = {}) {
        val currentState = _uiState.value
        if (currentState.price.isBlank()) {
            _uiState.update { it.copy(errorMessage = "Please enter an amount") }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }

            val result = repository.createExpense(
                amount = currentState.price,
                category = currentState.category,
                description = currentState.expenseName,
                date = currentState.date.ifBlank { "2026-11-05" }
            )

            result.onSuccess {
                _uiState.update { it.copy(isLoading = false, isSuccess = true) }
                onSuccess()
            }.onFailure { error ->
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = error.localizedMessage ?: "Failed to save expense"
                    )
                }
            }
        }
    }
}