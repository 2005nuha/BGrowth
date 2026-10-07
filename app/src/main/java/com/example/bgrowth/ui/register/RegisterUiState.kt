package com.example.bgrowth.ui.register

data class RegisterUiState(
    val firstName: String = "",
    val lastName: String = "",
    val email: String = "",
    val password: String = "",
    val confirmPassword: String = "",

    val firstNameError: String? = null,
    val lastNameError: String? = null,
    val emailError: String? = null,
    val passwordError: String? = null,
    val confirmPasswordError: String? = null,

    val isPasswordVisible: Boolean = false,
    val isConfirmPasswordVisible: Boolean = false,

    val isLoading: Boolean = false,

    val isRegistrationSuccessful: Boolean = false,
    val hasBusiness: Boolean = false,

    val registrationError: String? = null
)