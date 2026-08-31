package com.example.bgrowth.ui.resetpassword

import androidx.compose.runtime.Immutable

@Immutable
data class ResetPasswordUiState(
    val newPassword: String = "",
    val confirmPassword: String = "",
    val newPasswordError: String? = null,
    val confirmPasswordError: String? = null,
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val isNewPasswordVisible: Boolean = false,
    val isConfirmPasswordVisible: Boolean = false,
    val isPasswordResetSuccessful: Boolean = false
)
