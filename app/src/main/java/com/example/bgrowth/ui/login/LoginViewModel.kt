package com.example.bgrowth.ui.login

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

class LoginViewModel(
    private val authRepository: AuthRepository = AuthRepository()
) : ViewModel() {

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    fun onEmailChange(value: String) {
        _uiState.update {
            it.copy(email = value, emailError = null, loginError = null, isLoginSuccessful = false)
        }
    }

    fun onPasswordChange(value: String) {
        _uiState.update {
            it.copy(
                password = value,
                passwordError = null,
                loginError = null,
                isLoginSuccessful = false
            )
        }
    }

    fun togglePasswordVisibility() {
        _uiState.update { it.copy(isPasswordVisible = !it.isPasswordVisible) }
    }

    fun login() {
        val state = _uiState.value
        if (state.isLoading) return

        val emailError = validateEmail(state.email)
        val passwordError = if (state.password.isBlank()) "Password is required." else null

        if (emailError != null || passwordError != null) {
            _uiState.update {
                it.copy(emailError = emailError, passwordError = passwordError)
            }
            return
        }

        _uiState.update {
            it.copy(isLoading = true, loginError = null, isLoginSuccessful = false)
        }

        viewModelScope.launch {
            try {
                authRepository.login(
                    email = state.email.trim(),
                    password = state.password
                )
                _uiState.update { it.copy(isLoading = false, isLoginSuccessful = true) }
            } catch (e: CancellationException) {
                throw e
            } catch (e: HttpException) {
                // Map common Laravel HTTP status codes to user-facing messages.
                val message = when (e.code()) {
                    401 -> "Invalid email or password."
                    422 -> "Please check your input and try again."
                    500 -> "Server error. Please try again later."
                    else -> "Error ${e.code()}. Please try again."
                }
                _uiState.update { it.copy(isLoading = false, loginError = message) }
            } catch (e: IOException) {
                // Network failure or timeout — the server was not reachable.
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        loginError = "Cannot reach the server. Check your connection."
                    )
                }
            } catch (e: Throwable) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        loginError = e.message?.takeIf(String::isNotBlank)
                            ?: "Login failed. Please try again."
                    )
                }
            }
        }
    }

    // Called by the screen after it has reacted to isLoginSuccessful = true,
    // so a configuration change does not re-trigger navigation.
    fun consumeLoginSuccess() {
        _uiState.update { it.copy(isLoginSuccessful = false) }
    }

    private fun validateEmail(email: String): String? = when {
        email.isBlank() -> "Email address is required."
        !EMAIL_PATTERN.matches(email.trim()) -> "Enter a valid email address."
        else -> null
    }

    private companion object {
        val EMAIL_PATTERN = Regex("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")
    }
}
