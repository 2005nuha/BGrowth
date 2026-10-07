package com.example.bgrowth.ui.register

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

class RegisterViewModel(
    private val authRepository: AuthRepository =
        AuthRepository(),

    private val businessRepository: BusinessRepository =
        BusinessRepository()
) : ViewModel() {

    private val _uiState =
        MutableStateFlow(RegisterUiState())

    val uiState: StateFlow<RegisterUiState> =
        _uiState.asStateFlow()

    fun onFirstNameChange(value: String) {

        _uiState.update {
            it.copy(
                firstName = value,
                firstNameError = null,
                registrationError = null,
                isRegistrationSuccessful = false,
                hasBusiness = false
            )
        }
    }

    fun onLastNameChange(value: String) {

        _uiState.update {
            it.copy(
                lastName = value,
                lastNameError = null,
                registrationError = null,
                isRegistrationSuccessful = false,
                hasBusiness = false
            )
        }
    }

    fun onEmailChange(value: String) {

        _uiState.update {
            it.copy(
                email = value,
                emailError = null,
                registrationError = null,
                isRegistrationSuccessful = false,
                hasBusiness = false
            )
        }
    }

    fun onPasswordChange(value: String) {

        _uiState.update {
            it.copy(
                password = value,
                passwordError = null,
                confirmPasswordError = null,
                registrationError = null,
                isRegistrationSuccessful = false,
                hasBusiness = false
            )
        }
    }

    fun onConfirmPasswordChange(value: String) {

        _uiState.update {
            it.copy(
                confirmPassword = value,
                confirmPasswordError = null,
                registrationError = null,
                isRegistrationSuccessful = false,
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

    fun toggleConfirmPasswordVisibility() {

        _uiState.update {
            it.copy(
                isConfirmPasswordVisible =
                    !it.isConfirmPasswordVisible
            )
        }
    }

    fun register() {

        if (
            _uiState.value.isLoading ||
            !validateInputs()
        ) {
            return
        }

        val state =
            _uiState.value

        _uiState.update {
            it.copy(
                isLoading = true,
                registrationError = null,
                isRegistrationSuccessful = false,
                hasBusiness = false
            )
        }

        viewModelScope.launch {

            try {

                /*
                 * POST /api/auth/register/
                 *
                 * Register endpoint يرجع tokens،
                 * وAuthRepository يحفظها مباشرة.
                 */
                authRepository.register(
                    firstName =
                        state.firstName.trim(),

                    lastName =
                        state.lastName.trim(),

                    email =
                        state.email.trim(),

                    password =
                        state.password,

                    passwordConfirm =
                        state.confirmPassword
                )

                /*
                 * بما أن المستخدم أصبح authenticated،
                 * نفحص هل لديه Business.
                 *
                 * غالبًا الحساب الجديد سيحصل على 404،
                 * وبالتالي ينتقل إلى Business Setup.
                 */
                val hasBusiness =
                    checkIfBusinessExists()

                _uiState.update {
                    it.copy(
                        isLoading = false,
                        registrationError = null,
                        isRegistrationSuccessful = true,
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

                handleRegisterHttpException(e)

            } catch (
                e: IOException
            ) {

                _uiState.update {
                    it.copy(
                        isLoading = false,
                        isRegistrationSuccessful = false,
                        hasBusiness = false,
                        registrationError =
                            "Cannot reach the server. Check your connection."
                    )
                }

            } catch (
                e: Exception
            ) {

                _uiState.update {
                    it.copy(
                        isLoading = false,
                        isRegistrationSuccessful = false,
                        hasBusiness = false,
                        registrationError =
                            e.message
                                ?.takeIf(
                                    String::isNotBlank
                                )
                                ?: "Unable to create your account. Please try again."
                    )
                }
            }
        }
    }

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

    private fun handleRegisterHttpException(
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

                400 ->
                    errorBody
                        ?.takeIf(
                            String::isNotBlank
                        )
                        ?: "Please check your registration information."

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
                isRegistrationSuccessful = false,
                hasBusiness = false,
                registrationError = message
            )
        }
    }

    fun consumeRegistrationSuccess() {

        _uiState.update {
            it.copy(
                isRegistrationSuccessful = false
            )
        }
    }

    private fun validateInputs(): Boolean {

        val state =
            _uiState.value

        val firstNameError =
            if (state.firstName.isBlank()) {
                "First name is required."
            } else {
                null
            }

        /*
         * Backend يعتبر last_name optional،
         * لذلك لا نجعله Required في الموبايل.
         */
        val lastNameError: String? =
            null

        val emailError =
            when {

                state.email.isBlank() ->
                    "Email address is required."

                !EMAIL_PATTERN.matches(
                    state.email.trim()
                ) ->
                    "Enter a valid email address."

                else ->
                    null
            }

        val passwordError =
            when {

                state.password.isBlank() ->
                    "Password is required."

                state.password.length <
                        MINIMUM_PASSWORD_LENGTH ->
                    "Password must contain at least 8 characters."

                else ->
                    null
            }

        val confirmPasswordError =
            when {

                state.confirmPassword.isBlank() ->
                    "Confirm password is required."

                state.confirmPassword !=
                        state.password ->
                    "Passwords do not match."

                else ->
                    null
            }

        _uiState.update {
            it.copy(
                firstNameError =
                    firstNameError,

                lastNameError =
                    lastNameError,

                emailError =
                    emailError,

                passwordError =
                    passwordError,

                confirmPasswordError =
                    confirmPasswordError,

                registrationError =
                    null
            )
        }

        return listOf(
            firstNameError,
            lastNameError,
            emailError,
            passwordError,
            confirmPasswordError
        ).all {
            it == null
        }
    }

    private companion object {

        const val MINIMUM_PASSWORD_LENGTH =
            8

        val EMAIL_PATTERN =
            Regex(
                "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$"
            )
    }
}