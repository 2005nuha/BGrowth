package com.example.bgrowth.ui.businesssetup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bgrowth.data.repository.BusinessRepository
import java.io.IOException
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import retrofit2.HttpException

class BusinessSetupViewModel(
    private val businessRepository: BusinessRepository =
        BusinessRepository()
) : ViewModel() {

    private val _uiState =
        MutableStateFlow(BusinessSetupUiState())

    val uiState: StateFlow<BusinessSetupUiState> =
        _uiState.asStateFlow()

    fun onBusinessNameChange(value: String) {

        if (_uiState.value.isLoading) {
            return
        }

        _uiState.update { state ->
            state.copy(
                businessName = value,
                businessNameError =
                    if (state.businessNameError != null) {
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

        if (_uiState.value.isLoading) {
            return
        }

        _uiState.update {
            it.copy(
                selectedBusinessType = value,
                businessTypeError =
                    validateBusinessType(value),
                errorMessage = null,
                isBusinessSetupSuccessful = false
            )
        }
    }

    fun onCurrencySelected(value: String) {

        if (_uiState.value.isLoading) {
            return
        }

        _uiState.update {
            it.copy(
                selectedCurrency = value,
                currencyError =
                    validateCurrency(value),
                errorMessage = null,
                isBusinessSetupSuccessful = false
            )
        }
    }

    fun saveAndContinue() {

        val currentState =
            _uiState.value

        if (currentState.isLoading) {
            return
        }

        val businessNameError =
            validateBusinessName(
                currentState.businessName
            )

        val businessTypeError =
            validateBusinessType(
                currentState.selectedBusinessType
            )

        val currencyError =
            validateCurrency(
                currentState.selectedCurrency
            )

        if (
            businessNameError != null ||
            businessTypeError != null ||
            currencyError != null
        ) {

            _uiState.update {
                it.copy(
                    businessNameError =
                        businessNameError,
                    businessTypeError =
                        businessTypeError,
                    currencyError =
                        currencyError,
                    errorMessage = null,
                    isBusinessSetupSuccessful = false
                )
            }

            return
        }

        _uiState.update {
            it.copy(
                businessName =
                    it.businessName.trim(),
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

                businessRepository.createBusiness(
                    name =
                        currentState.businessName,

                    businessType =
                        currentState.selectedBusinessType,

                    currency =
                        normalizeCurrency(
                            currentState.selectedCurrency
                        )
                )

                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = null,
                        isBusinessSetupSuccessful = true
                    )
                }

            } catch (
                exception: CancellationException
            ) {

                throw exception

            } catch (
                exception: HttpException
            ) {

                val errorBody =
                    try {

                        exception
                            .response()
                            ?.errorBody()
                            ?.string()

                    } catch (
                        _: Exception
                    ) {

                        null
                    }

                val message =
                    when (exception.code()) {

                        400 ->
                            errorBody
                                ?.takeIf(
                                    String::isNotBlank
                                )
                                ?: "Please check your business information."

                        401 ->
                            "Your session has expired. Please log in again."

                        404 ->
                            "Business service was not found."

                        in 500..599 ->
                            "Server error. Please try again later."

                        else ->
                            errorBody
                                ?.takeIf(
                                    String::isNotBlank
                                )
                                ?: "Unable to save your business."
                    }

                _uiState.update {
                    it.copy(
                        isLoading = false,
                        isBusinessSetupSuccessful = false,
                        errorMessage = message
                    )
                }

            } catch (
                exception: IOException
            ) {

                _uiState.update {
                    it.copy(
                        isLoading = false,
                        isBusinessSetupSuccessful = false,
                        errorMessage =
                            "Cannot reach the server. Check your connection."
                    )
                }

            } catch (
                exception: Exception
            ) {

                _uiState.update {
                    it.copy(
                        isLoading = false,
                        isBusinessSetupSuccessful = false,
                        errorMessage =
                            exception.message
                                ?.takeIf(
                                    String::isNotBlank
                                )
                                ?: "Unable to save your business. Please try again."
                    )
                }
            }
        }
    }

    fun consumeBusinessSetupSuccess() {

        _uiState.update {
            it.copy(
                isBusinessSetupSuccessful = false
            )
        }
    }

    private fun validateBusinessName(
        value: String
    ): String? {

        return if (value.isBlank()) {
            "Business name is required."
        } else {
            null
        }
    }

    private fun validateBusinessType(
        value: String
    ): String? {

        return if (value.isBlank()) {
            "Business type is required."
        } else {
            null
        }
    }

    private fun validateCurrency(
        value: String
    ): String? {

        return if (value.isBlank()) {
            "Currency is required."
        } else {
            null
        }
    }

    /*
     * الـUI يمكن أن يعرض:
     * NIS ₪
     * USD $
     * JOD
     *
     * لكن Backend نخزن له قيمة ثابتة ونظيفة:
     * NIS / USD / JOD ...
     */
    private fun normalizeCurrency(
        currency: String
    ): String {

        return currency
            .trim()
            .substringBefore(" ")
            .uppercase()
    }
}