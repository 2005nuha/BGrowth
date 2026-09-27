package com.example.bgrowth.ui.customers

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class CustomersViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(CustomersUiState())
    val uiState: StateFlow<CustomersUiState> = _uiState.asStateFlow()

    private val allCustomers = listOf(
        CustomerItem(1, "SA", "Sara Ahmed", "059 123 4567", "$45.00 due", true),
        CustomerItem(2, "MK", "Mohammed Khalil", "059 123 4567", "No balance", false),
        CustomerItem(3, "RN", "Rana Nasser", "059 123 4567", "$120.00 due", true)
    )

    init {
        // يمكنك تغيير isEmpty إلى true لرؤية الشاشة الفارغة
        _uiState.update {
            it.copy(
                isEmpty = false,
                customers = allCustomers
            )
        }
    }

    fun onSearchQueryChange(query: String) {
        val filtered = if (query.isBlank()) {
            allCustomers
        } else {
            allCustomers.filter { it.name.contains(query, ignoreCase = true) }
        }

        _uiState.update {
            it.copy(
                searchQuery = query,
                customers = filtered
            )
        }
    }
}