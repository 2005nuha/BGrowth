package com.example.bgrowth.ui.customerprofile

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bgrowth.data.repository.CustomerRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CustomerProfileViewModel(
    private val repository: CustomerRepository = CustomerRepository(),
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _uiState = MutableStateFlow(CustomerProfileUiState())
    val uiState: StateFlow<CustomerProfileUiState> = _uiState.asStateFlow()

    // استلام الـ customerId الممرر عبر الـ Navigation (مثلاً customer_profile/{customerId})
    private val customerId: Int? = savedStateHandle.get<Int>("customerId")

    init {
        customerId?.let { id ->
            loadCustomerProfile(id)
        } ?: run {
            // بيانات وهمية تجريبية في حال عدم تمرير ID أثناء التطوير
            _uiState.update {
                it.copy(
                    purchases = listOf(
                        PurchaseItem(1, "Thermal cup", "Cups & Mugs", "$90.00"),
                        PurchaseItem(2, "Potato Chips", "Snacks", "$25.00")
                    )
                )
            }
        }
    }

    fun loadCustomerProfile(id: Int) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }

            val result = repository.getCustomerById(id)

            if (result.isSuccess) {
                val customerData = result.getOrNull()
                _uiState.update { state ->
                    state.copy(
                        customer = customerData,
                        initials = getInitials(customerData?.name ?: ""),
                        name = customerData?.name ?: "Unknown",
                        phone = customerData?.phone ?: "No phone",
                        isLoading = false,
                        purchases = listOf(
                            PurchaseItem(1, "Thermal cup", "Cups & Mugs", "$90.00"),
                            PurchaseItem(2, "Potato Chips", "Snacks", "$25.00")
                        )
                    )
                }
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

    fun onTabSelected(tab: String) {
        _uiState.update { it.copy(selectedTab = tab) }
    }

    private fun getInitials(name: String): String {
        val parts = name.trim().split(" ")
        return when {
            parts.size >= 2 -> "${parts[0].take(1)}${parts[1].take(1)}".uppercase()
            parts.isNotEmpty() && parts[0].isNotEmpty() -> parts[0].take(2).uppercase()
            else -> "--"
        }
    }
}