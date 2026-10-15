package com.example.bgrowth.ui.customers

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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

    init {
        fetchCustomers()
    }

    fun fetchCustomers(query: String? = null) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }

            repository.getCustomers(query)
                .onSuccess { dtoList ->
                    val customerItems = dtoList.map { dto ->
                        // استخراج أول حرفين للـ Initials
                        val initials = dto.name.trim()
                            .split(" ")
                            .take(2)
                            .mapNotNull { namePart -> namePart.firstOrNull()?.uppercaseChar() }
                            .joinToString("")
                            .ifEmpty { "C" }

                        CustomerItem(
                            id = dto.id,
                            initials = initials,
                            name = dto.name,
                            phone = dto.phone.ifBlank { "No phone number" },
                            dueText = if (dto.address.isNotBlank()) dto.address else "No address",
                            hasDebt = false
                        )
                    }

                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            isEmpty = customerItems.isEmpty(),
                            customers = customerItems
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

    fun refresh() {
        fetchCustomers(_uiState.value.searchQuery.ifBlank { null })
    }
}