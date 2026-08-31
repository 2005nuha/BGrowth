package com.example.bgrowth.ui.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bgrowth.data.model.RegisterRequest
import com.example.bgrowth.data.remote.AuthApi
import com.example.bgrowth.data.remote.RetrofitClient
import java.util.concurrent.CancellationException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class RegisterViewModel(
    private val authApi: AuthApi = RetrofitClient.authApi
) : ViewModel() {

    private val _uiState = MutableStateFlow(RegisterUiState())
    val uiState: StateFlow<RegisterUiState> = _uiState.asStateFlow()

    fun onFullNameChange(value: String) {
        _uiState.update {
            it.copy(
                fullName = value,
                fullNameError = null,
                registrationResult = null,
                registrationError = null
            )
        }
    }

    fun onEmailChange(value: String) {
        _uiState.update {
            it.copy(
                email = value,
                emailError = null,
                registrationResult = null,
                registrationError = null
            )
        }
    }

    fun onPhoneNumberChange(value: String) {
        if (!value.all(::isAllowedPhoneCharacter)) return

        _uiState.update {
            it.copy(
                phoneNumber = value,
                phoneNumberError = null,
                registrationResult = null,
                registrationError = null
            )
        }
    }

    fun onPasswordChange(value: String) {
        _uiState.update {
            it.copy(
                password = value,
                passwordError = null,
                confirmPasswordError = null,
                registrationResult = null,
                registrationError = null
            )
        }
    }

    fun onConfirmPasswordChange(value: String) {
        _uiState.update {
            it.copy(
                confirmPassword = value,
                confirmPasswordError = null,
                registrationResult = null,
                registrationError = null
            )
        }
    }

    fun togglePasswordVisibility() {
        _uiState.update { it.copy(isPasswordVisible = !it.isPasswordVisible) }
    }

    fun toggleConfirmPasswordVisibility() {
        _uiState.update {
            it.copy(isConfirmPasswordVisible = !it.isConfirmPasswordVisible)
        }
    }

    fun validateForLocalNavigation(): Boolean = validateInputs()

    fun register() {
        if (_uiState.value.isLoading || !validateInputs()) return

        val validState = _uiState.value
        _uiState.update {
            it.copy(
                isLoading = true,
                registrationResult = null,
                registrationError = null
            )
        }

        viewModelScope.launch {
            try {
                val response = authApi.register(
                    RegisterRequest(
                        name = validState.fullName.trim(),
                        email = validState.email.trim(),
                        phone = validState.phoneNumber.trim(),
                        password = validState.password
                    )
                )
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        registrationResult = response,
                        registrationError = null
                    )
                }
            } catch (exception: CancellationException) {
                throw exception
            } catch (exception: Throwable) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        registrationResult = null,
                        registrationError = exception.message
                            ?.takeIf(String::isNotBlank)
                            ?: "Unable to create your account. Please try again."
                    )
                }
            }
        }
    }

    private fun validateInputs(): Boolean {
        val state = _uiState.value
        val fullNameError = if (state.fullName.isBlank()) {
            "Full name is required."
        } else {
            null
        }
        val emailError = when {
            state.email.isBlank() -> "Email address is required."
            !EMAIL_PATTERN.matches(state.email.trim()) -> "Enter a valid email address."
            else -> null
        }
        val phoneNumberError = when {
            state.phoneNumber.isBlank() -> "Phone number is required."
            !state.phoneNumber.all(::isAllowedPhoneCharacter) ->
                "Enter a valid phone number."
            else -> null
        }
        val passwordError = when {
            state.password.isBlank() -> "Password is required."
            state.password.length < MINIMUM_PASSWORD_LENGTH ->
                "Password must contain at least 8 characters."
            else -> null
        }
        val confirmPasswordError = when {
            state.confirmPassword.isBlank() -> "Confirm password is required."
            state.confirmPassword != state.password -> "Passwords do not match."
            else -> null
        }

        _uiState.update {
            it.copy(
                fullNameError = fullNameError,
                emailError = emailError,
                phoneNumberError = phoneNumberError,
                passwordError = passwordError,
                confirmPasswordError = confirmPasswordError,
                registrationResult = null,
                registrationError = null
            )
        }

        return listOf(
            fullNameError,
            emailError,
            phoneNumberError,
            passwordError,
            confirmPasswordError
        ).all { it == null }
    }

    private companion object {
        const val MINIMUM_PASSWORD_LENGTH = 8
        val EMAIL_PATTERN = Regex(
            pattern = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$"
        )

        fun isAllowedPhoneCharacter(character: Char): Boolean =
            character.isDigit() || character == '+' || character == '-' ||
                character == '(' || character == ')' || character.isWhitespace()
    }
}
