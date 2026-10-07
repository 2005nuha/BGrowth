package com.example.bgrowth.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bgrowth.data.repository.AuthRepository
import com.example.bgrowth.data.repository.BusinessRepository
import java.io.IOException
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import retrofit2.HttpException

class LoginViewModel(
    private val authRepository: AuthRepository =
        AuthRepository(),

    private val businessRepository: BusinessRepository =
        BusinessRepository()
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
                isLoginSuccessful = false,
                hasBusiness = false
            )
        }
    }

    fun onPasswordChange(value: String) {

        _uiState.update {
            it.copy(
                password = value,
                passwordError = null,
                loginError = null,
                isLoginSuccessful = false,
                hasBusiness = false
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
                isLoginSuccessful = false,
                hasBusiness = false
            )
        }

        viewModelScope.launch {

            try {

                /*
                 * STEP 1
                 *
                 * Login.
                 *
                 * AuthRepository يحفظ
                 * access + refresh tokens.
                 */
                authRepository.login(
                    email =
                        state.email.trim(),
                    password =
                        state.password
                )

                /*
                 * STEP 2
                 *
                 * بعد وجود Access Token
                 * نفحص هل المستخدم لديه Business.
                 */
                val hasBusiness =
                    checkIfBusinessExists()

                /*
                 * STEP 3
                 *
                 * نبلغ الشاشة بنجاح Login
                 * وبنتيجة Business check.
                 */
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        loginError = null,
                        isLoginSuccessful = true,
                        hasBusiness = hasBusiness
                    )
                }

            } catch (
                e: CancellationException
            ) {

                throw e

            } catch (
                e: HttpException
            ) {

                handleLoginHttpException(e)

            } catch (
                e: IOException
            ) {

                _uiState.update {
                    it.copy(
                        isLoading = false,
                        isLoginSuccessful = false,
                        hasBusiness = false,
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
                        hasBusiness = false,
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
     * GET /api/business/
     *
     * 200:
     * المستخدم لديه Business.
     *
     * 404:
     * المستخدم لم ينشئ Business بعد.
     *
     * أي status آخر:
     * مشكلة حقيقية ولا نعتبرها
     * "Business غير موجود".
     */
    private suspend fun checkIfBusinessExists(): Boolean {

        return try {

            businessRepository.getBusiness()

            true

        } catch (
            e: HttpException
        ) {

            if (e.code() == 404) {

                false

            } else {

                throw e
            }
        }
    }

    private fun handleLoginHttpException(
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
                 * Login API يستخدم 400
                 * عند credentials غير صحيحة.
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

                401 ->
                    "Your session could not be authenticated. Please log in again."

                403 ->
                    "You do not have permission to perform this action."

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
                hasBusiness = false,
                loginError = message
            )
        }
    }

    /*
     * بعد تنفيذ Navigation
     * نمسح event حتى لا يتكرر
     * عند recomposition.
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