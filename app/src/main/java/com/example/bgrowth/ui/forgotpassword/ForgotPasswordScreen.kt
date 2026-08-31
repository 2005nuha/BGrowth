package com.example.bgrowth.ui.forgotpassword

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.BoxWithConstraints
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.bgrowth.R
import com.example.bgrowth.ui.theme.BGrothTheme

private val ForgotPasswordPrimary = Color(0xFF0F5D46)
private val ForgotPasswordSecondary = Color(0xFF1F8A61)
private val ForgotPasswordBackground = Color(0xFFF7F8F4)
private val ForgotPasswordSurface = Color(0xFFFFFFFF)
private val ForgotPasswordBorder = Color(0xFFD9E3DA)
private val ForgotPasswordSecondaryText = Color(0xFF5E6B63)
private val ForgotPasswordMutedText = Color(0xFF8A958E)
private val ForgotPasswordError = Color(0xFFD9534F)

@Composable
fun ForgotPasswordScreen(
    onBackClick: () -> Unit,
    onCodeSent: (String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ForgotPasswordViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(uiState.codeSentEmail) {
        uiState.codeSentEmail?.let { email ->
            onCodeSent(email)
            viewModel.consumeCodeSent()
        }
    }

    ForgotPasswordScreen(
        uiState = uiState,
        onEmailChange = viewModel::onEmailChange,
        onBackClick = onBackClick,
        onSendVerificationCodeClick = viewModel::sendVerificationCode,
        modifier = modifier
    )
}

@Composable
fun ForgotPasswordScreen(
    uiState: ForgotPasswordUiState,
    onEmailChange: (String) -> Unit,
    onBackClick: () -> Unit,
    onSendVerificationCodeClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    BoxWithConstraints(
        modifier = modifier
            .fillMaxSize()
            .background(ForgotPasswordBackground)
            .safeDrawingPadding()
            .imePadding()
    ) {
        val compactHeight = maxHeight < 720.dp
        val illustrationWidth = (maxWidth * 0.62f).coerceAtMost(250.dp)

        Column(modifier = Modifier.fillMaxSize()) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
                    .padding(horizontal = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onBackClick) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = ForgotPasswordPrimary
                    )
                }
            }

            Column(
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(if (compactHeight) 18.dp else 42.dp))

                Text(
                    text = "Forgot Password?",
                    modifier = Modifier.fillMaxWidth(),
                    color = ForgotPasswordPrimary,
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    lineHeight = 35.sp,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = "Enter your email and we'll send you a verification code to reset your password.",
                    modifier = Modifier.fillMaxWidth(),
                    color = ForgotPasswordSecondaryText,
                    fontSize = 15.sp,
                    lineHeight = 22.sp,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(if (compactHeight) 24.dp else 34.dp))

                OutlinedTextField(
                    value = uiState.email,
                    onValueChange = onEmailChange,
                    modifier = Modifier.fillMaxWidth(),
                    enabled = !uiState.isLoading,
                    label = { Text(text = "Email Address") },
                    placeholder = { Text(text = "Enter your Email") },
                    trailingIcon = {
                        Icon(
                            imageVector = Icons.Filled.Email,
                            contentDescription = "Email address",
                            tint = if (uiState.emailError != null) {
                                ForgotPasswordError
                            } else {
                                ForgotPasswordMutedText
                            }
                        )
                    },
                    supportingText = uiState.emailError?.let { error ->
                        {
                            Text(
                                text = error,
                                fontSize = 12.sp,
                                lineHeight = 16.sp
                            )
                        }
                    },
                    isError = uiState.emailError != null,
                    singleLine = true,
                    shape = RoundedCornerShape(14.dp),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Email,
                        imeAction = ImeAction.Done
                    ),
                    keyboardActions = KeyboardActions(
                        onDone = { onSendVerificationCodeClick() }
                    ),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = ForgotPasswordPrimary,
                        unfocusedTextColor = ForgotPasswordPrimary,
                        disabledTextColor = ForgotPasswordPrimary,
                        focusedBorderColor = ForgotPasswordPrimary,
                        unfocusedBorderColor = ForgotPasswordBorder,
                        disabledBorderColor = ForgotPasswordBorder,
                        errorBorderColor = ForgotPasswordError,
                        focusedLabelColor = ForgotPasswordPrimary,
                        unfocusedLabelColor = ForgotPasswordSecondaryText,
                        disabledLabelColor = ForgotPasswordMutedText,
                        errorLabelColor = ForgotPasswordError,
                        cursorColor = ForgotPasswordPrimary,
                        errorCursorColor = ForgotPasswordError,
                        focusedContainerColor = ForgotPasswordSurface,
                        unfocusedContainerColor = ForgotPasswordSurface,
                        disabledContainerColor = ForgotPasswordSurface,
                        errorContainerColor = ForgotPasswordSurface,
                        errorSupportingTextColor = ForgotPasswordError,
                        focusedPlaceholderColor = ForgotPasswordMutedText,
                        unfocusedPlaceholderColor = ForgotPasswordMutedText
                    )
                )

                Spacer(modifier = Modifier.height(if (uiState.emailError == null) 26.dp else 14.dp))

                Button(
                    onClick = onSendVerificationCodeClick,
                    enabled = !uiState.isLoading,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = ForgotPasswordPrimary,
                        contentColor = Color.White,
                        disabledContainerColor = ForgotPasswordPrimary.copy(alpha = 0.65f),
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
                            text = "Send Verification Code",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }

                uiState.errorMessage?.let { error ->
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = error,
                        modifier = Modifier.fillMaxWidth(),
                        color = ForgotPasswordError,
                        fontSize = 13.sp,
                        lineHeight = 18.sp,
                        textAlign = TextAlign.Center
                    )
                }

                Spacer(modifier = Modifier.height(if (compactHeight) 12.dp else 18.dp))

                Image(
                    painter = painterResource(id = R.drawable.forgot_password),
                    contentDescription = null,
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .width(illustrationWidth)
                        .aspectRatio(2f / 3f),
                    contentScale = ContentScale.Fit
                )

                Spacer(modifier = Modifier.height(if (compactHeight) 10.dp else 16.dp))
            }
        }
    }
}

@Preview(
    name = "Forgot Password - Default",
    showBackground = true,
    widthDp = 390,
    heightDp = 844
)
@Composable
private fun ForgotPasswordDefaultPreview() {
    BGrothTheme {
        ForgotPasswordPreview(uiState = ForgotPasswordUiState())
    }
}

@Preview(
    name = "Forgot Password - Error",
    showBackground = true,
    widthDp = 390,
    heightDp = 844
)
@Composable
private fun ForgotPasswordErrorPreview() {
    BGrothTheme {
        ForgotPasswordPreview(
            uiState = ForgotPasswordUiState(
                email = "hazem@",
                emailError = "Enter a valid email address."
            )
        )
    }
}

@Preview(
    name = "Forgot Password - Loading",
    showBackground = true,
    widthDp = 390,
    heightDp = 844
)
@Composable
private fun ForgotPasswordLoadingPreview() {
    BGrothTheme {
        ForgotPasswordPreview(
            uiState = ForgotPasswordUiState(
                email = "hazem@example.com",
                isLoading = true
            )
        )
    }
}

@Composable
private fun ForgotPasswordPreview(uiState: ForgotPasswordUiState) {
    ForgotPasswordScreen(
        uiState = uiState,
        onEmailChange = {},
        onBackClick = {},
        onSendVerificationCodeClick = {}
    )
}
