package com.example.bgrowth.ui.addcustomer

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bgrowth.data.model.AddCustomerRequest
import com.example.bgrowth.data.repository.CustomerRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AddCustomerViewModel(
    private val repository: CustomerRepository = CustomerRepository()
) : ViewModel() {

    private val _uiState = MutableStateFlow(AddCustomerUiState())
    val uiState: StateFlow<AddCustomerUiState> = _uiState.asStateFlow()

    fun onFullNameChange(name: String) {
        _uiState.update { it.copy(fullName = name, errorMessage = null) }
    }

    fun onPhoneNumberChange(phone: String) {
        _uiState.update { it.copy(phoneNumber = phone, errorMessage = null) }
    }

    fun onNotesChange(notes: String) {
        _uiState.update { it.copy(notes = notes, errorMessage = null) }
    }

    fun onSaveCustomer(businessId: Int = 1) {
        val currentState = _uiState.value

        if (currentState.fullName.isBlank()) {
            _uiState.update { it.copy(errorMessage = "Full Name is required") }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }

            val request = AddCustomerRequest(
                business = businessId,
                name = currentState.fullName,
                phone = currentState.phoneNumber,
                address = currentState.notes
            )

            val result = repository.addCustomer(request)

            if (result.isSuccess) {
                _uiState.update { it.copy(isLoading = false, isSuccess = true) }
            } else {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = result.exceptionOrNull()?.message ?: "Failed to add customer"
                    )
                }
            }
        }
    }
}