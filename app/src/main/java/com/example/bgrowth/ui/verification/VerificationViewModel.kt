package com.example.bgrowth.ui.verification

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import java.util.concurrent.CancellationException
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

fun interface VerifyOtpAction {
    suspend fun verify(verificationTarget: String, otpCode: String)
}

fun interface ResendOtpAction {
    suspend fun resend(verificationTarget: String)
}

class VerificationViewModel(
    private val verifyOtpAction: VerifyOtpAction? = null,
    private val resendOtpAction: ResendOtpAction? = null
) : ViewModel() {

    private val _uiState = MutableStateFlow(VerificationUiState())
    val uiState: StateFlow<VerificationUiState> = _uiState.asStateFlow()

    private var countdownJob: Job? = null

    init {
        startCountdown()
    }

    fun setVerificationContext(
        verificationTarget: String,
        verificationMode: VerificationMode
    ) {
        _uiState.update { state ->
            if (
                state.verificationTarget == verificationTarget &&
                state.verificationMode == verificationMode
            ) {
                state
            } else {
                state.copy(
                    verificationTarget = verificationTarget,
                    verificationMode = verificationMode
                )
            }
        }
    }

    fun onOtpChange(value: String) {
        if (_uiState.value.isLoading) return

        val digits = value.filter(Char::isDigit).take(OTP_LENGTH)
        _uiState.update {
            it.copy(
                otpCode = digits,
                errorMessage = null,
                isVerificationSuccessful = false
            )
        }
    }

    fun verify() {
        val currentState = _uiState.value
        if (currentState.isLoading) return

        if (currentState.otpCode.length != OTP_LENGTH) {
            _uiState.update {
                it.copy(errorMessage = "Enter the complete 6-digit code.")
            }
            return
        }

        val verificationAction = verifyOtpAction
        if (verificationAction == null) {
            _uiState.update {
                it.copy(errorMessage = "Phone verification is not available yet.")
            }
            return
        }

        _uiState.update {
            it.copy(
                isLoading = true,
                errorMessage = null,
                isVerificationSuccessful = false
            )
        }

        viewModelScope.launch {
            try {
                verificationAction.verify(
                    verificationTarget = currentState.verificationTarget,
                    otpCode = currentState.otpCode
                )
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = null,
                        isVerificationSuccessful = true
                    )
                }
            } catch (exception: CancellationException) {
                throw exception
            } catch (exception: Throwable) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = exception.message
                            ?.takeIf(String::isNotBlank)
                            ?: "The code you entered is invalid. Please try again.",
                        isVerificationSuccessful = false
                    )
                }
            }
        }
    }

    fun resendCode() {
        val currentState = _uiState.value
        if (!currentState.canResend || currentState.isResending) return

        val resendAction = resendOtpAction
        if (resendAction == null) {
            _uiState.update {
                it.copy(errorMessage = "Code resend is not available yet.")
            }
            return
        }

        _uiState.update {
            it.copy(
                canResend = false,
                isResending = true,
                errorMessage = null
            )
        }

        viewModelScope.launch {
            try {
                resendAction.resend(currentState.verificationTarget)
                _uiState.update {
                    it.copy(
                        otpCode = "",
                        isResending = false,
                        errorMessage = null,
                        isVerificationSuccessful = false
                    )
                }
                startCountdown()
            } catch (exception: CancellationException) {
                throw exception
            } catch (exception: Throwable) {
                _uiState.update {
                    it.copy(
                        canResend = true,
                        isResending = false,
                        errorMessage = exception.message
                            ?.takeIf(String::isNotBlank)
                            ?: "Unable to resend the code. Please try again."
                    )
                }
            }
        }
    }

    fun consumeVerificationSuccess() {
        _uiState.update { it.copy(isVerificationSuccessful = false) }
    }

    private fun startCountdown() {
        countdownJob?.cancel()
        _uiState.update {
            it.copy(
                remainingSeconds = RESEND_COUNTDOWN_SECONDS,
                canResend = false
            )
        }
        countdownJob = viewModelScope.launch {
            while (_uiState.value.remainingSeconds > 0) {
                delay(ONE_SECOND_MILLIS)
                _uiState.update { state ->
                    val nextSecond = (state.remainingSeconds - 1).coerceAtLeast(0)
                    state.copy(
                        remainingSeconds = nextSecond,
                        canResend = nextSecond == 0
                    )
                }
            }
        }
    }

    private companion object {
        const val OTP_LENGTH = 6
        const val RESEND_COUNTDOWN_SECONDS = 45
        const val ONE_SECOND_MILLIS = 1_000L
    }
}
