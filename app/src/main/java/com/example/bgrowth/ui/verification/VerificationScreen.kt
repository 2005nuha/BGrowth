package com.example.bgrowth.ui.verification

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onPreviewKeyEvent
import androidx.compose.ui.input.key.type
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.bgrowth.ui.theme.BGrothTheme

private val VerificationPrimary = Color(0xFF0F5D46)
private val VerificationSecondary = Color(0xFF1F8A61)
private val VerificationBackground = Color(0xFFF7F8F4)
private val VerificationSecondaryText = Color(0xFF5E6B63)
private val VerificationBorder = Color(0xFFD9E3DA)
private val VerificationError = Color(0xFFD9534F)

@Composable
fun VerificationScreen(
    verificationTarget: String,
    verificationMode: VerificationMode,
    onBackClick: () -> Unit,
    onVerificationSuccess: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: VerificationViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(verificationTarget, verificationMode) {
        viewModel.setVerificationContext(
            verificationTarget = verificationTarget,
            verificationMode = verificationMode
        )
    }

    LaunchedEffect(uiState.isVerificationSuccessful) {
        if (uiState.isVerificationSuccessful) {
            onVerificationSuccess()
            viewModel.consumeVerificationSuccess()
        }
    }

    VerificationScreen(
        uiState = uiState,
        onOtpChange = viewModel::onOtpChange,
        onBackClick = onBackClick,
        onVerifyClick = viewModel::verify,
        onResendClick = viewModel::resendCode,
        modifier = modifier
    )
}

@Composable
fun VerificationScreen(
    uiState: VerificationUiState,
    onOtpChange: (String) -> Unit,
    onBackClick: () -> Unit,
    onVerifyClick: () -> Unit,
    onResendClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(VerificationBackground)
            .safeDrawingPadding()
            .imePadding()
            .padding(horizontal = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBackClick) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = VerificationPrimary
                )
            }
        }

        Spacer(
            modifier = Modifier
                .height(24.dp)
                .weight(0.55f)
        )

        Text(
            text = if (uiState.verificationMode == VerificationMode.PASSWORD_RESET) {
                "Verify Your Email"
            } else {
                "Verify Your Phone"
            },
            modifier = Modifier.fillMaxWidth(),
            color = VerificationPrimary,
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            lineHeight = 35.sp,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(10.dp))

        Text(
            text = "We've sent a 6-digit code to",
            modifier = Modifier.fillMaxWidth(),
            color = VerificationSecondaryText,
            fontSize = 15.sp,
            lineHeight = 22.sp,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = uiState.verificationTarget,
            modifier = Modifier.fillMaxWidth(),
            color = VerificationPrimary,
            fontSize = 16.sp,
            fontWeight = FontWeight.SemiBold,
            lineHeight = 24.sp,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(38.dp))

        OtpInput(
            otpCode = uiState.otpCode,
            onOtpChange = onOtpChange,
            enabled = !uiState.isLoading,
            isError = uiState.errorMessage != null
        )

        if (uiState.errorMessage != null) {
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = uiState.errorMessage,
                modifier = Modifier.fillMaxWidth(),
                color = VerificationError,
                fontSize = 13.sp,
                lineHeight = 18.sp,
                textAlign = TextAlign.Center
            )
        } else {
            Spacer(modifier = Modifier.height(10.dp))
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            contentAlignment = Alignment.Center
        ) {
            when {
                uiState.isResending -> {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(16.dp),
                            color = VerificationSecondary,
                            strokeWidth = 2.dp
                        )
                        Text(
                            text = "Resending Code...",
                            color = VerificationSecondaryText,
                            fontSize = 14.sp
                        )
                    }
                }

                uiState.canResend -> {
                    TextButton(onClick = onResendClick) {
                        Text(
                            text = "Resend Code",
                            color = VerificationSecondary,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }

                else -> {
                    Text(
                        text = "Resend Code in ${formatRemainingTime(uiState.remainingSeconds)}",
                        color = VerificationSecondaryText,
                        fontSize = 14.sp,
                        lineHeight = 20.sp
                    )
                }
            }
        }

        Spacer(
            modifier = Modifier
                .height(28.dp)
                .weight(1f)
        )

        Button(
            onClick = onVerifyClick,
            enabled = !uiState.isLoading,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = VerificationPrimary,
                contentColor = Color.White,
                disabledContainerColor = VerificationPrimary.copy(alpha = 0.65f),
                disabledContentColor = Color.White
            ),
            elevation = ButtonDefaults.buttonElevation(defaultElevation = 0.dp)
        ) {
            if (uiState.isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(22.dp),
                    color = Color.White,
                    strokeWidth = 2.dp
                )
            } else {
                Text(
                    text = "Verify",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }

        Spacer(modifier = Modifier.height(26.dp))
    }
}

@Composable
private fun OtpInput(
    otpCode: String,
    onOtpChange: (String) -> Unit,
    enabled: Boolean,
    isError: Boolean
) {
    val focusRequesters = remember {
        List(OTP_LENGTH) { FocusRequester() }
    }
    var focusedIndex by remember { mutableStateOf<Int?>(null) }

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        repeat(OTP_LENGTH) { index ->
            val digit = otpCode.getOrNull(index)?.toString().orEmpty()
            val isActive = focusedIndex == index
            val borderColor = when {
                isError -> VerificationError
                isActive -> VerificationPrimary
                digit.isNotEmpty() -> VerificationSecondary
                else -> VerificationBorder
            }

            BasicTextField(
                value = digit,
                onValueChange = { input ->
                    val digits = input.filter(Char::isDigit)
                    when {
                        digits.isEmpty() -> {
                            if (index < otpCode.length) {
                                onOtpChange(otpCode.removeRange(index, index + 1))
                            }
                        }

                        digits.length > 1 && digit.isEmpty() -> {
                            val pastedCode = digits.take(OTP_LENGTH)
                            onOtpChange(pastedCode)
                            val destination = pastedCode.length.coerceAtMost(
                                OTP_LENGTH - 1
                            )
                            focusRequesters[destination].requestFocus()
                        }

                        else -> {
                            val newDigit = digits.last()
                            val targetIndex = index.coerceAtMost(otpCode.length)
                            val updatedCode = if (targetIndex < otpCode.length) {
                                otpCode.replaceRange(
                                    targetIndex,
                                    targetIndex + 1,
                                    newDigit.toString()
                                )
                            } else {
                                otpCode + newDigit
                            }
                            onOtpChange(updatedCode)
                            if (targetIndex < OTP_LENGTH - 1) {
                                focusRequesters[targetIndex + 1].requestFocus()
                            }
                        }
                    }
                },
                modifier = Modifier
                    .weight(1f)
                    .height(56.dp)
                    .focusRequester(focusRequesters[index])
                    .onFocusChanged { focusState ->
                        if (focusState.isFocused) focusedIndex = index
                        else if (focusedIndex == index) focusedIndex = null
                    }
                    .onPreviewKeyEvent { event ->
                        val isEmptyBackspace = event.type == KeyEventType.KeyDown &&
                            event.key == Key.Backspace &&
                            digit.isEmpty() &&
                            index > 0
                        if (isEmptyBackspace) {
                            val previousDigitIndex = (index - 1).coerceAtMost(
                                otpCode.lastIndex
                            )
                            if (previousDigitIndex >= 0) {
                                onOtpChange(
                                    otpCode.removeRange(
                                        previousDigitIndex,
                                        previousDigitIndex + 1
                                    )
                                )
                                focusRequesters[previousDigitIndex].requestFocus()
                            } else {
                                focusRequesters[index - 1].requestFocus()
                            }
                            true
                        } else {
                            false
                        }
                    }
                    .background(
                        color = Color.White,
                        shape = RoundedCornerShape(14.dp)
                    )
                    .border(
                        width = if (isActive) 2.dp else 1.dp,
                        color = borderColor,
                        shape = RoundedCornerShape(14.dp)
                    )
                    .padding(horizontal = 4.dp),
                enabled = enabled,
                singleLine = true,
                textStyle = TextStyle(
                    color = VerificationPrimary,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.SemiBold,
                    textAlign = TextAlign.Center
                ),
                cursorBrush = SolidColor(VerificationPrimary),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.NumberPassword,
                    imeAction = if (index == OTP_LENGTH - 1) {
                        ImeAction.Done
                    } else {
                        ImeAction.Next
                    }
                ),
                keyboardActions = KeyboardActions(
                    onNext = {
                        if (index < OTP_LENGTH - 1) {
                            focusRequesters[index + 1].requestFocus()
                        }
                    }
                ),
                decorationBox = { innerTextField ->
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        innerTextField()
                    }
                }
            )
        }
    }
}

private fun formatRemainingTime(remainingSeconds: Int): String {
    val safeSeconds = remainingSeconds.coerceAtLeast(0)
    val minutes = safeSeconds / 60
    val seconds = safeSeconds % 60
    return minutes.toString().padStart(2, '0') + ":" +
        seconds.toString().padStart(2, '0')
}

private const val OTP_LENGTH = 6

@Preview(
    name = "Verification - Default",
    showBackground = true,
    widthDp = 390,
    heightDp = 844
)
@Composable
private fun VerificationDefaultPreview() {
    BGrothTheme {
        VerificationPreview(
            uiState = VerificationUiState(
                verificationTarget = "+966 50 123 4567",
                remainingSeconds = 45
            )
        )
    }
}

@Preview(
    name = "Verification - Filled",
    showBackground = true,
    widthDp = 390,
    heightDp = 844
)
@Composable
private fun VerificationFilledPreview() {
    BGrothTheme {
        VerificationPreview(
            uiState = VerificationUiState(
                verificationTarget = "+966 50 123 4567",
                otpCode = "123456",
                remainingSeconds = 28
            )
        )
    }
}

@Preview(
    name = "Verification - Error",
    showBackground = true,
    widthDp = 390,
    heightDp = 844
)
@Composable
private fun VerificationErrorPreview() {
    BGrothTheme {
        VerificationPreview(
            uiState = VerificationUiState(
                verificationTarget = "+966 50 123 4567",
                otpCode = "123456",
                errorMessage = "The code you entered is invalid. Please try again.",
                remainingSeconds = 17
            )
        )
    }
}

@Preview(
    name = "Verification - Loading",
    showBackground = true,
    widthDp = 390,
    heightDp = 844
)
@Composable
private fun VerificationLoadingPreview() {
    BGrothTheme {
        VerificationPreview(
            uiState = VerificationUiState(
                verificationTarget = "+966 50 123 4567",
                otpCode = "123456",
                isLoading = true,
                remainingSeconds = 12
            )
        )
    }
}

@Composable
private fun VerificationPreview(uiState: VerificationUiState) {
    VerificationScreen(
        uiState = uiState,
        onOtpChange = {},
        onBackClick = {},
        onVerifyClick = {},
        onResendClick = {}
    )
}
