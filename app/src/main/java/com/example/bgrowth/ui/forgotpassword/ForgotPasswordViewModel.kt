package com.example.bgrowth.ui.forgotpassword

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bgrowth.data.repository.AuthRepository
import java.io.IOException
import java.util.concurrent.CancellationException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import retrofit2.HttpException

class ForgotPasswordViewModel(
    private val authRepository: AuthRepository = AuthRepository()
) : ViewModel() {

    private val _uiState = MutableStateFlow(ForgotPasswordUiState())
    val uiState: StateFlow<ForgotPasswordUiState> = _uiState.asStateFlow()

    fun onEmailChange(value: String) {
        if (_uiState.value.isLoading) return

        _uiState.update { state ->
            state.copy(
                email = value,
                emailError = if (state.emailError != null) validateEmail(value) else null,
                errorMessage = null,
                codeSentEmail = null
            )
        }
    }

    fun sendVerificationCode() {
        val currentState = _uiState.value
        if (currentState.isLoading) return

        val emailError = validateEmail(currentState.email)
        if (emailError != null) {
            _uiState.update {
                it.copy(
                    emailError = emailError,
                    errorMessage = null,
                    codeSentEmail = null
                )
            }
            return
        }

        val normalizedEmail = currentState.email.trim()
        _uiState.update {
            it.copy(
                email = normalizedEmail,
                emailError = null,
                isLoading = true,
                errorMessage = null,
                codeSentEmail = null
            )
        }

        viewModelScope.launch {
            try {
                authRepository.passwordResetRequest(normalizedEmail)
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        codeSentEmail = normalizedEmail
                    )
                }
            } catch (exception: CancellationException) {
                throw exception
            } catch (exception: HttpException) {
                val message = when (exception.code()) {
                    404 -> "No account found with this email address."
                    429 -> "Too many requests. Please wait before trying again."
                    500 -> "Server error. Please try again later."
                    else -> "Unable to send a reset link. Please try again."
                }
                _uiState.update {
                    it.copy(isLoading = false, errorMessage = message)
                }
            } catch (exception: IOException) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = "Cannot reach the server. Check your connection."
                    )
                }
            } catch (exception: Throwable) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = "Unable to send a reset link. Please try again."
                    )
                }
            }
        }
    }

    fun consumeCodeSent() {
        _uiState.update { it.copy(codeSentEmail = null) }
    }

    private fun validateEmail(email: String): String? = when {
        email.isBlank() -> "Email address is required."
        !EMAIL_PATTERN.matches(email.trim()) -> "Enter a valid email address."
        else -> null
    }

    private companion object {
        val EMAIL_PATTERN = Regex(
            pattern = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$"
        )
    }
}
