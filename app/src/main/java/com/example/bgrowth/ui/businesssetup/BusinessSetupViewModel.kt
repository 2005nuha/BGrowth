package com.example.bgrowth.ui.businesssetup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import java.util.concurrent.CancellationException
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class BusinessSetupViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(BusinessSetupUiState())
    val uiState: StateFlow<BusinessSetupUiState> = _uiState.asStateFlow()

    fun onBusinessNameChange(value: String) {
        if (_uiState.value.isLoading) return

        _uiState.update { state ->
            state.copy(
                businessName = value,
                businessNameError = if (state.businessNameError != null) {
                    validateBusinessName(value)
                } else {
                    null
                },
                errorMessage = null,
                isBusinessSetupSuccessful = false
            )
        }
    }

    fun onBusinessTypeSelected(value: String) {
        if (_uiState.value.isLoading) return

        _uiState.update {
            it.copy(
                selectedBusinessType = value,
                businessTypeError = validateBusinessType(value),
                errorMessage = null,
                isBusinessSetupSuccessful = false
            )
        }
    }

    fun onCurrencySelected(value: String) {
        if (_uiState.value.isLoading) return

        _uiState.update {
            it.copy(
                selectedCurrency = value,
                currencyError = validateCurrency(value),
                errorMessage = null,
                isBusinessSetupSuccessful = false
            )
        }
    }

    fun onLogoSelected(uri: String?) {
        if (_uiState.value.isLoading) return

        _uiState.update {
            it.copy(
                selectedLogoUri = uri,
                errorMessage = null,
                isBusinessSetupSuccessful = false
            )
        }
    }

    fun saveAndContinue() {
        val currentState = _uiState.value
        if (currentState.isLoading) return

        val businessNameError = validateBusinessName(currentState.businessName)
        val businessTypeError = validateBusinessType(currentState.selectedBusinessType)
        val currencyError = validateCurrency(currentState.selectedCurrency)

        if (
            businessNameError != null ||
            businessTypeError != null ||
            currencyError != null
        ) {
            _uiState.update {
                it.copy(
                    businessNameError = businessNameError,
                    businessTypeError = businessTypeError,
                    currencyError = currencyError,
                    errorMessage = null,
                    isBusinessSetupSuccessful = false
                )
            }
            return
        }

        _uiState.update {
            it.copy(
                businessName = it.businessName.trim(),
                isLoading = true,
                businessNameError = null,
                businessTypeError = null,
                currencyError = null,
                errorMessage = null,
                isBusinessSetupSuccessful = false
            )
        }

        viewModelScope.launch {
            try {
                // TODO: Replace with real business setup API
                delay(LOCAL_REQUEST_DELAY_MILLIS)
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        isBusinessSetupSuccessful = true
                    )
                }
            } catch (exception: CancellationException) {
                throw exception
            } catch (exception: Throwable) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = "Unable to save your business. Please try again."
                    )
                }
            }
        }
    }

    fun consumeBusinessSetupSuccess() {
        _uiState.update { it.copy(isBusinessSetupSuccessful = false) }
    }

    private fun validateBusinessName(value: String): String? =
        if (value.isBlank()) "Business name is required." else null

    private fun validateBusinessType(value: String): String? =
        if (value.isBlank()) "Business type is required." else null

    private fun validateCurrency(value: String): String? =
        if (value.isBlank()) "Currency is required." else null

    private companion object {
        const val LOCAL_REQUEST_DELAY_MILLIS = 600L
    }
}
