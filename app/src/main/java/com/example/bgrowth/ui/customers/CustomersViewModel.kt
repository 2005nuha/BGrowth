package com.example.bgrowth.ui.customers

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bgrowth.data.model.CustomerResponse
import com.example.bgrowth.data.repository.CustomerRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CustomersViewModel(
    private val repository: CustomerRepository = CustomerRepository()
) : ViewModel() {

    private val _uiState = MutableStateFlow(CustomersUiState())
    val uiState: StateFlow<CustomersUiState> = _uiState.asStateFlow()

    private var rawCustomersList = listOf<CustomerResponse>()

    init {
        loadCustomers()
    }

    fun loadCustomers() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }

            val result = repository.getCustomers()

            if (result.isSuccess) {
                rawCustomersList = result.getOrDefault(emptyList())
                applyFilterAndUiUpdate(_uiState.value.searchQuery)
            } else {
                _uiState.update { state ->
                    state.copy(
                        isLoading = false,
                        errorMessage = result.exceptionOrNull()?.message
                    )
                }
            }
        }
    }

    fun onSearchQueryChange(query: String) {
        applyFilterAndUiUpdate(query)
    }

    private fun applyFilterAndUiUpdate(query: String) {
        val filteredList = if (query.isBlank()) {
            rawCustomersList
        } else {
            rawCustomersList.filter { customer ->
                customer.name.contains(query, ignoreCase = true) ||
                        (customer.phone?.contains(query) == true)
            }
        }

        // تحويل CustomerResponse القادم من الباك إند إلى CustomerItem الذي تتوقعه الشاشة
        val uiItems = filteredList.map { response ->
            CustomerItem(
                id = response.id,
                initials = getInitials(response.name),
                name = response.name,
                phone = response.phone ?: "",
                balance = "No balance",
                hasBalance = false
            )
        }

        _uiState.update { state ->
            state.copy(
                searchQuery = query,
                customers = uiItems,
                isEmpty = uiItems.isEmpty(),
                isLoading = false
            )
        }
    }

    private fun getInitials(name: String): String {
        val parts = name.trim().split(" ")
        return when {
            parts.size >= 2 -> "${parts[0].take(1)}${parts[1].take(1)}".uppercase()
            parts.isNotEmpty() && parts[0].isNotEmpty() -> parts[0].take(2).uppercase()
            else -> "CU"
        }
    }
}