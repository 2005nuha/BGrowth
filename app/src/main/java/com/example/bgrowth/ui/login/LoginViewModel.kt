package com.example.bgrowth.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bgrowth.data.repository.AuthRepository
import java.io.IOException
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import retrofit2.HttpException

class LoginViewModel(
    private val authRepository: AuthRepository = AuthRepository()
) : ViewModel() {

    private val _uiState =
        MutableStateFlow(LoginUiState())

    val uiState: StateFlow<LoginUiState> =
        _uiState.asStateFlow()

    fun onEmailChange(value: String) {

        _uiState.update {
            it.copy(
                email = value,
                emailError = null,
                loginError = null,
                isLoginSuccessful = false
            )
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

        _uiState.update {
            it.copy(
                isPasswordVisible =
                    !it.isPasswordVisible
            )
        }
    }

    fun login() {

        val state =
            _uiState.value

        if (state.isLoading) {
            return
        }

        val emailError =
            validateEmail(state.email)

        val passwordError =
            if (state.password.isBlank()) {
                "Password is required."
            } else {
                null
            }

        if (
            emailError != null ||
            passwordError != null
        ) {

            _uiState.update {
                it.copy(
                    emailError = emailError,
                    passwordError = passwordError
                )
            }

            return
        }

        _uiState.update {
            it.copy(
                isLoading = true,
                loginError = null,
                isLoginSuccessful = false
            )
        }

        viewModelScope.launch {

            try {

                /*
                 * AuthRepository.login():
                 *
                 * POST /api/auth/login/
                 *
                 * ويحفظ access + refresh tokens
                 * داخل SessionManager عند النجاح.
                 */
                authRepository.login(
                    email =
                        state.email.trim(),

                    password =
                        state.password
                )

                _uiState.update {
                    it.copy(
                        isLoading = false,
                        loginError = null,
                        isLoginSuccessful = true
                    )
                }

            } catch (
                e: CancellationException
            ) {

                throw e

            } catch (
                e: HttpException
            ) {

                val errorBody =
                    try {

                        e.response()
                            ?.errorBody()
                            ?.string()

                    } catch (
                        _: Exception
                    ) {

                        null
                    }

                val message =
                    when (e.code()) {

                        /*
                         * Backend يستخدم 400
                         * عند email/password غير صحيحين.
                         */
                        400 -> {

                            if (
                                errorBody
                                    ?.contains(
                                        "Invalid email or password",
                                        ignoreCase = true
                                    ) == true
                            ) {

                                "Invalid email or password."

                            } else {

                                errorBody
                                    ?.takeIf(
                                        String::isNotBlank
                                    )
                                    ?: "Please check your information and try again."
                            }
                        }

                        /*
                         * Login endpoint public.
                         *
                         * لو ظهر 401 هنا، فهذا غير طبيعي
                         * ويشير لمشكلة Backend أو Networking config.
                         */
                        401 ->
                            "Authentication request was rejected by the server."

                        429 ->
                            "Too many requests. Please try again later."

                        in 500..599 ->
                            "Server error. Please try again later."

                        else ->
                            errorBody
                                ?.takeIf(
                                    String::isNotBlank
                                )
                                ?: "Error ${e.code()}. Please try again."
                    }

                _uiState.update {
                    it.copy(
                        isLoading = false,
                        isLoginSuccessful = false,
                        loginError = message
                    )
                }

            } catch (
                e: IOException
            ) {

                _uiState.update {
                    it.copy(
                        isLoading = false,
                        isLoginSuccessful = false,
                        loginError =
                            "Cannot reach the server. Check your connection."
                    )
                }

            } catch (
                e: Exception
            ) {

                _uiState.update {
                    it.copy(
                        isLoading = false,
                        isLoginSuccessful = false,
                        loginError =
                            e.message
                                ?.takeIf(
                                    String::isNotBlank
                                )
                                ?: "Login failed. Please try again."
                    )
                }
            }
        }
    }

    /*
     * نستدعيها بعد ما الشاشة تنتقل
     * حتى لا يتكرر Navigation عند recomposition.
     */
    fun consumeLoginSuccess() {

        _uiState.update {
            it.copy(
                isLoginSuccessful = false
            )
        }
    }

    private fun validateEmail(
        email: String
    ): String? {

        return when {

            email.isBlank() ->
                "Email address is required."

            !EMAIL_PATTERN.matches(
                email.trim()
            ) ->
                "Enter a valid email address."

            else ->
                null
        }
    }

    private companion object {

        val EMAIL_PATTERN =
            Regex(
                "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$"
            )
    }
}