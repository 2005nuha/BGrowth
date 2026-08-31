package com.example.bgrowth.ui.resetpassword

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import java.util.concurrent.CancellationException
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ResetPasswordViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(ResetPasswordUiState())
    val uiState: StateFlow<ResetPasswordUiState> = _uiState.asStateFlow()

    fun onNewPasswordChange(value: String) {
        if (_uiState.value.isLoading) return

        _uiState.update { state ->
            state.copy(
                newPassword = value,
                newPasswordError = if (state.newPasswordError != null) {
                    validateNewPassword(value)
                } else {
                    null
                },
                confirmPasswordError = if (state.confirmPasswordError != null) {
                    validateConfirmPassword(state.confirmPassword, value)
                } else {
                    null
                },
                errorMessage = null,
                isPasswordResetSuccessful = false
            )
        }
    }

    fun onConfirmPasswordChange(value: String) {
        if (_uiState.value.isLoading) return

        _uiState.update { state ->
            state.copy(
                confirmPassword = value,
                confirmPasswordError = if (state.confirmPasswordError != null) {
                    validateConfirmPassword(value, state.newPassword)
                } else {
                    null
                },
                errorMessage = null,
                isPasswordResetSuccessful = false
            )
        }
    }

    fun toggleNewPasswordVisibility() {
        _uiState.update {
            it.copy(isNewPasswordVisible = !it.isNewPasswordVisible)
        }
    }

    fun toggleConfirmPasswordVisibility() {
        _uiState.update {
            it.copy(isConfirmPasswordVisible = !it.isConfirmPasswordVisible)
        }
    }

    fun resetPassword() {
        val currentState = _uiState.value
        if (currentState.isLoading) return

        val newPasswordError = validateNewPassword(currentState.newPassword)
        val confirmPasswordError = validateConfirmPassword(
            confirmPassword = currentState.confirmPassword,
            newPassword = currentState.newPassword
        )

        if (newPasswordError != null || confirmPasswordError != null) {
            _uiState.update {
                it.copy(
                    newPasswordError = newPasswordError,
                    confirmPasswordError = confirmPasswordError,
                    errorMessage = null,
                    isPasswordResetSuccessful = false
                )
            }
            return
        }

        _uiState.update {
            it.copy(
                isLoading = true,
                newPasswordError = null,
                confirmPasswordError = null,
                errorMessage = null,
                isPasswordResetSuccessful = false
            )
        }

        viewModelScope.launch {
            try {
                // TODO: Replace with real reset-password API
                delay(LOCAL_REQUEST_DELAY_MILLIS)
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        isPasswordResetSuccessful = true
                    )
                }
            } catch (exception: CancellationException) {
                throw exception
            } catch (exception: Throwable) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = "Unable to reset your password. Please try again."
                    )
                }
            }
        }
    }

    fun consumePasswordResetSuccess() {
        _uiState.update { it.copy(isPasswordResetSuccessful = false) }
    }

    private fun validateNewPassword(password: String): String? = when {
        password.isBlank() -> "New password is required."
        password.length < MINIMUM_PASSWORD_LENGTH ->
            "Password must contain at least 8 characters."
        password.none(Char::isLetter) -> "Password must include at least one letter."
        password.none(Char::isDigit) -> "Password must include at least one number."
        password.any(Char::isWhitespace) -> "Password must not contain spaces."
        else -> null
    }

    private fun validateConfirmPassword(
        confirmPassword: String,
        newPassword: String
    ): String? = when {
        confirmPassword.isBlank() -> "Confirm new password is required."
        confirmPassword != newPassword -> "Passwords do not match."
        else -> null
    }

    private companion object {
        const val MINIMUM_PASSWORD_LENGTH = 8
        const val LOCAL_REQUEST_DELAY_MILLIS = 600L
    }
}
