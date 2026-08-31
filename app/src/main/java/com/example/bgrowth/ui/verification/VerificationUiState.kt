package com.example.bgrowth.ui.verification

import androidx.compose.runtime.Immutable

enum class VerificationMode {
    SIGN_UP,
    PASSWORD_RESET;

    companion object {
        fun fromRouteValue(value: String?): VerificationMode =
            entries.firstOrNull { it.name == value } ?: SIGN_UP
    }
}

@Immutable
data class VerificationUiState(
    val verificationTarget: String = "",
    val verificationMode: VerificationMode = VerificationMode.SIGN_UP,
    val otpCode: String = "",
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val remainingSeconds: Int = 45,
    val canResend: Boolean = false,
    val isResending: Boolean = false,
    val isVerificationSuccessful: Boolean = false
)
