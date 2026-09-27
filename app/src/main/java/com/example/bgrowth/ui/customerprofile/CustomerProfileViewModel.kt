package com.example.bgrowth.ui.customerprofile

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class CustomerProfileViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(CustomerProfileUiState())
    val uiState: StateFlow<CustomerProfileUiState> = _uiState.asStateFlow()

    init {
        _uiState.update {
            it.copy(
                purchases = listOf(
                    PurchaseItem(1, "Thermal cup", "Cups & Mugs", "$90.00"),
                    PurchaseItem(2, "Potato Chips", "Snacks", "$25.00")
                )
            )
        }
    }

    fun onTabSelected(tab: String) {
        _uiState.update { it.copy(selectedTab = tab) }
    }
}