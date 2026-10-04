package com.example.bgrowth.ui.selectcustomer

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class SelectCustomerViewModel : ViewModel() {

    // قائمة العملاء الأصلية
    private val allCustomers = listOf(
        CustomerSelectionItem("1", "Sara Ahmed", "059 123 4567", "SA"),
        CustomerSelectionItem("2", "Mohammed Khalil", "059 987 6543", "MK"),
        CustomerSelectionItem("3", "Rana Nasser", "059 123 4567", "RN")
    )

    private val _uiState = MutableStateFlow(
        SelectCustomerUiState(
            customers = allCustomers,
            selectedCustomerId = "1"
        )
    )
    val uiState: StateFlow<SelectCustomerUiState> = _uiState.asStateFlow()

    fun onSearchQueryChange(query: String) {
        // فلترة القائمة بناءً على النص المدخل
        val filteredList = if (query.isBlank()) {
            allCustomers
        } else {
            allCustomers.filter {
                it.name.contains(query, ignoreCase = true) || it.phone.contains(query)
            }
        }

        _uiState.update {
            it.copy(
                searchQuery = query,
                customers = filteredList
            )
        }
    }

    fun onCustomerSelected(id: String) {
        _uiState.update { it.copy(selectedCustomerId = id) }
    }
}