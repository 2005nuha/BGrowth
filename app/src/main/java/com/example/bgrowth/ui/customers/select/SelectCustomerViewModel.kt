package com.example.bgrowth.ui.customers.select

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bgrowth.data.repository.CustomerRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SelectCustomerViewModel(
    private val repository: CustomerRepository = CustomerRepository()
) : ViewModel() {

    private val _uiState = MutableStateFlow(SelectCustomerUiState())
    val uiState: StateFlow<SelectCustomerUiState> = _uiState.asStateFlow()

    init {
        fetchCustomers()
    }

    fun fetchCustomers(query: String? = null) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            repository.getCustomers(query)
                .onSuccess { dtoList ->
                    val selectionItems = dtoList.map { dto ->
                        val initials = dto.name.trim()
                            .split(" ")
                            .take(2)
                            .mapNotNull { it.firstOrNull()?.uppercaseChar() }
                            .joinToString("")
                            .ifEmpty { "C" }

                        CustomerSelectionItem(
                            id = dto.id.toString(),
                            name = dto.name,
                            phone = dto.phone.ifBlank { "No phone number" },
                            initials = initials
                        )
                    }

                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            customers = selectionItems
                        )
                    }
                }
                .onFailure { error ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = error.message ?: "Failed to load customers"
                        )
                    }
                }
        }
    }

    fun onSearchQueryChange(query: String) {
        _uiState.update { it.copy(searchQuery = query) }
        fetchCustomers(query.ifBlank { null })
    }

    fun onCustomerSelected(id: String) {
        _uiState.update { it.copy(selectedCustomerId = id) }
    }
}