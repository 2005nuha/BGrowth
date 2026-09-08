package com.example.bgrowth.ui.login

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.bgrowth.ui.theme.BGrothTheme

private val LoginPrimary = Color(0xFF0F5D46)
private val LoginBackground = Color(0xFFF7F8F4)
private val LoginSurface = Color(0xFFFFFFFF)
private val LoginSecondaryText = Color(0xFF5E6B63)
private val LoginMutedText = Color(0xFF8A958E)
private val LoginBorder = Color(0xFFD9E3DA)
private val LoginError = Color(0xFFD9534F)
private val LoginSuccess = Color(0xFF1F8A61)

@Composable
fun LoginScreen(
    onBackClick: () -> Unit,
    onCreateAccountClick: () -> Unit,
    onLoginSuccess: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: LoginViewModel = viewModel(),
    onForgotPasswordClick: () -> Unit = {},
    onGoogleClick: () -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsState()

    // Navigate when the ViewModel signals a successful login.
    // consumeLoginSuccess() prevents re-navigation on configuration change.
    LaunchedEffect(uiState.isLoginSuccessful) {
        if (uiState.isLoginSuccessful) {
            viewModel.consumeLoginSuccess()
            onLoginSuccess()
        }
    }

    // Combine field-level and API-level errors into a single message string
    // so the existing LoginContent layout (single message slot) works unchanged.
    val message = uiState.loginError ?: uiState.emailError ?: uiState.passwordError
    val isError = message != null

    LoginContent(
        email = uiState.email,
        password = uiState.password,
        message = message,
        isError = isError,
        isLoading = uiState.isLoading,
        onEmailChange = viewModel::onEmailChange,
        onPasswordChange = viewModel::onPasswordChange,
        onLoginClick = viewModel::login,
        onBackClick = onBackClick,
        onForgotPasswordClick = onForgotPasswordClick,
        onGoogleClick = onGoogleClick,
        onCreateAccountClick = onCreateAccountClick,
        modifier = modifier
    )
}

@Composable
private fun LoginContent(
    email: String,
    password: String,
    message: String?,
    isError: Boolean,
    isLoading: Boolean,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onLoginClick: () -> Unit,
    onBackClick: () -> Unit,
    onForgotPasswordClick: () -> Unit,
    onGoogleClick: () -> Unit,
    onCreateAccountClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    BoxWithConstraints(
        modifier = modifier
            .fillMaxSize()
            .background(LoginBackground)
            .safeDrawingPadding()
            .imePadding()
    ) {
        val compactHeight = maxHeight < 720.dp

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
                        tint = LoginPrimary
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
                Spacer(modifier = Modifier.height(if (compactHeight) 26.dp else 78.dp))

                Text(
                    text = "Welcome Back",
                    modifier = Modifier.fillMaxWidth(),
                    color = LoginPrimary,
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    lineHeight = 35.sp,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Log in to continue managing your business.",
                    modifier = Modifier.fillMaxWidth(),
                    color = LoginSecondaryText,
                    fontSize = 15.sp,
                    lineHeight = 22.sp,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(if (compactHeight) 22.dp else 30.dp))

                OutlinedTextField(
                    value = email,
                    onValueChange = onEmailChange,
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("Email Address") },
                    trailingIcon = {
                        Icon(
                            imageVector = Icons.Filled.Email,
                            contentDescription = "Email address",
                            tint = LoginSecondaryText
                        )
                    },
                    enabled = !isLoading,
                    singleLine = true,
                    shape = RoundedCornerShape(14.dp),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Email,
                        imeAction = ImeAction.Next
                    ),
                    colors = loginTextFieldColors()
                )

                Spacer(modifier = Modifier.height(14.dp))

                OutlinedTextField(
                    value = password,
                    onValueChange = onPasswordChange,
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("Password") },
                    trailingIcon = {
                        Icon(
                            imageVector = Icons.Filled.Lock,
                            contentDescription = "Password",
                            tint = LoginSecondaryText
                        )
                    },
                    enabled = !isLoading,
                    singleLine = true,
                    shape = RoundedCornerShape(14.dp),
                    visualTransformation = PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Password,
                        imeAction = ImeAction.Done
                    ),
                    colors = loginTextFieldColors()
                )

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(44.dp),
                    contentAlignment = Alignment.CenterEnd
                ) {
                    TextButton(
                        onClick = onForgotPasswordClick,
                        contentPadding = PaddingValues(horizontal = 0.dp)
                    ) {
                        Text(
                            text = "Forgot Password?",
                            color = LoginPrimary,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }

                Spacer(modifier = Modifier.height(if (compactHeight) 10.dp else 18.dp))

                Button(
                    onClick = onLoginClick,
                    enabled = !isLoading,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = LoginPrimary,
                        contentColor = LoginSurface,
                        disabledContainerColor = LoginPrimary.copy(alpha = 0.65f),
                        disabledContentColor = LoginSurface
                    ),
                    elevation = ButtonDefaults.buttonElevation(defaultElevation = 0.dp)
                ) {
                    if (isLoading) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(22.dp),
                            color = LoginSurface,
                            strokeWidth = 2.dp
                        )
                    } else {
                        Text(
                            text = "Log in",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }

                message?.let {
                    Spacer(modifier = Modifier.height(14.dp))
                    Text(
                        text = it,
                        modifier = Modifier.fillMaxWidth(),
                        color = if (isError) LoginError else LoginSuccess,
                        fontSize = 13.sp,
                        lineHeight = 18.sp,
                        textAlign = TextAlign.Center
                    )
                }

                Spacer(modifier = Modifier.height(if (compactHeight) 16.dp else 24.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    HorizontalDivider(modifier = Modifier.weight(1f), color = LoginBorder)
                    Text(
                        text = "or",
                        modifier = Modifier.padding(horizontal = 14.dp),
                        color = LoginMutedText,
                        fontSize = 14.sp
                    )
                    HorizontalDivider(modifier = Modifier.weight(1f), color = LoginBorder)
                }

                Spacer(modifier = Modifier.height(if (compactHeight) 16.dp else 24.dp))

                OutlinedButton(
                    onClick = onGoogleClick,
                    enabled = !isLoading,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    shape = RoundedCornerShape(16.dp),
                    border = BorderStroke(1.dp, LoginBorder),
                    colors = ButtonDefaults.outlinedButtonColors(
                        containerColor = LoginSurface,
                        contentColor = LoginPrimary
                    )
                ) {
                    // Add the official Google asset at the start when ic_google is supplied.
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Continue to Google",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = if (compactHeight) 8.dp else 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = "Don't have an account?",
                    color = LoginMutedText,
                    fontSize = 14.sp
                )
                TextButton(
                    onClick = onCreateAccountClick,
                    contentPadding = PaddingValues(horizontal = 5.dp)
                ) {
                    Text(
                        text = "Create Account",
                        color = LoginPrimary,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
                Spacer(modifier = Modifier.weight(1f))
            }
        }
    }
}

@Composable
private fun loginTextFieldColors() = OutlinedTextFieldDefaults.colors(
    focusedBorderColor = LoginPrimary,
    unfocusedBorderColor = LoginBorder,
    focusedLabelColor = LoginPrimary,
    unfocusedLabelColor = LoginSecondaryText,
    cursorColor = LoginPrimary,
    focusedContainerColor = LoginSurface,
    unfocusedContainerColor = LoginSurface
)

@Preview(
    name = "Login - Default",
    showBackground = true,
    widthDp = 390,
    heightDp = 844
)
@Composable
private fun LoginDefaultPreview() {
    BGrothTheme {
        LoginContent(
            email = "",
            password = "",
            message = null,
            isError = false,
            isLoading = false,
            onEmailChange = {},
            onPasswordChange = {},
            onLoginClick = {},
            onBackClick = {},
            onForgotPasswordClick = {},
            onGoogleClick = {},
            onCreateAccountClick = {}
        )
    }
}

@Preview(
    name = "Login - Error",
    showBackground = true,
    widthDp = 390,
    heightDp = 844
)
@Composable
private fun LoginErrorPreview() {
    BGrothTheme {
        LoginContent(
            email = "hazem@",
            password = "",
            message = "Enter a valid email address.",
            isError = true,
            isLoading = false,
            onEmailChange = {},
            onPasswordChange = {},
            onLoginClick = {},
            onBackClick = {},
            onForgotPasswordClick = {},
            onGoogleClick = {},
            onCreateAccountClick = {}
        )
    }
}

@Preview(
    name = "Login - Loading",
    showBackground = true,
    widthDp = 390,
    heightDp = 844
)
@Composable
private fun LoginLoadingPreview() {
    BGrothTheme {
        LoginContent(
            email = "hazem@example.com",
            password = "password123",
            message = null,
            isError = false,
            isLoading = true,
            onEmailChange = {},
            onPasswordChange = {},
            onLoginClick = {},
            onBackClick = {},
            onForgotPasswordClick = {},
            onGoogleClick = {},
            onCreateAccountClick = {}
        )
    }
}
