package com.example.bgrowth.ui.register

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

class RegisterViewModel(
    private val authRepository: AuthRepository = AuthRepository()
) : ViewModel() {

    private val _uiState =
        MutableStateFlow(RegisterUiState())

    val uiState: StateFlow<RegisterUiState> =
        _uiState.asStateFlow()

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

    /*
     * Phone موجود حاليًا في الـUI فقط.
     *
     * Backend Register API لا يستقبل phone.
     * لذلك نخزنه في UI state فقط ولا نرسله للسيرفر.
     */
    fun onPhoneNumberChange(value: String) {

        if (!value.all(::isAllowedPhoneCharacter)) {
            return
        }

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

    fun consumeRegistrationResult() {

        _uiState.update {
            it.copy(
                registrationResult = null
            )
        }
    }

    fun validateForLocalNavigation(): Boolean {

        return validateInputs()
    }

    fun register() {

        if (
            _uiState.value.isLoading ||
            !validateInputs()
        ) {
            return
        }

        val validState =
            _uiState.value

        _uiState.update {
            it.copy(
                isLoading = true,
                registrationResult = null,
                registrationError = null
            )
        }

        viewModelScope.launch {

            try {

                /*
                 * الـUI يحتوي Full Name واحد.
                 *
                 * Backend يحتاج:
                 * first_name
                 * last_name
                 *
                 * نقسم الاسم عند أول مسافة.
                 */
                val fullName =
                    validState.fullName.trim()

                val nameParts =
                    fullName.split(
                        "\\s+".toRegex(),
                        limit = 2
                    )

                val firstName =
                    nameParts
                        .firstOrNull()
                        .orEmpty()

                val lastName =
                    nameParts
                        .getOrNull(1)
                        .orEmpty()

                /*
                 * Register endpoint نفسه يرجع:
                 *
                 * user + access token + refresh token
                 *
                 * AuthRepository يحفظ الـtokens.
                 * لا نعمل Login ثاني.
                 */
                val response =
                    authRepository.register(
                        firstName = firstName,
                        lastName = lastName,
                        email =
                            validState.email.trim(),
                        password =
                            validState.password,
                        passwordConfirm =
                            validState.confirmPassword
                    )

                _uiState.update {
                    it.copy(
                        isLoading = false,
                        registrationResult = response,
                        registrationError = null
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
                    when {

                        !errorBody.isNullOrBlank() ->
                            errorBody

                        e.code() == 400 ->
                            "Invalid registration data. Please check your information."

                        e.code() == 429 ->
                            "Too many requests. Please try again later."

                        e.code() >= 500 ->
                            "Server error. Please try again later."

                        else ->
                            "Error ${e.code()}. Please try again."
                    }

                _uiState.update {
                    it.copy(
                        isLoading = false,
                        registrationResult = null,
                        registrationError = message
                    )
                }

            } catch (
                e: IOException
            ) {

                _uiState.update {
                    it.copy(
                        isLoading = false,
                        registrationResult = null,
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
                        registrationResult = null,
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

    private fun validateInputs(): Boolean {

        val state =
            _uiState.value

        val fullNameError =
            if (
                state.fullName.isBlank()
            ) {

                "Full name is required."

            } else {

                null
            }

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

        /*
         * Phone ليس جزءًا من Register API الحالي.
         *
         * إذا المستخدم كتب phone نتحقق فقط
         * من الأحرف المسموحة.
         *
         * لكنه ليس required.
         */
        val phoneNumberError =
            if (
                state.phoneNumber.isNotBlank() &&
                !state.phoneNumber.all(
                    ::isAllowedPhoneCharacter
                )
            ) {

                "Enter a valid phone number."

            } else {

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
                fullNameError =
                    fullNameError,

                emailError =
                    emailError,

                phoneNumberError =
                    phoneNumberError,

                passwordError =
                    passwordError,

                confirmPasswordError =
                    confirmPasswordError,

                registrationResult =
                    null,

                registrationError =
                    null
            )
        }

        return listOf(
            fullNameError,
            emailError,
            phoneNumberError,
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

        fun isAllowedPhoneCharacter(
            character: Char
        ): Boolean {

            return character.isDigit() ||
                    character == '+' ||
                    character == '-' ||
                    character == '(' ||
                    character == ')' ||
                    character.isWhitespace()
        }
    }
}