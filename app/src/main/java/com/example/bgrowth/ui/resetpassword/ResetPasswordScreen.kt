package com.example.bgrowth.ui.resetpassword

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
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
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.PathParser
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.bgrowth.R
import com.example.bgrowth.ui.theme.BGrothTheme

private val ResetPasswordPrimary = Color(0xFF0F5D46)
private val ResetPasswordSecondary = Color(0xFF1F8A61)
private val ResetPasswordBackground = Color(0xFFF7F8F4)
private val ResetPasswordSurface = Color(0xFFFFFFFF)
private val ResetPasswordBorder = Color(0xFFD9E3DA)
private val ResetPasswordSecondaryText = Color(0xFF5E6B63)
private val ResetPasswordMutedText = Color(0xFF8A958E)
private val ResetPasswordError = Color(0xFFD9534F)

@Composable
fun ResetPasswordScreen(
    onBackClick: () -> Unit,
    onPasswordResetSuccess: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ResetPasswordViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(uiState.isPasswordResetSuccessful) {
        if (uiState.isPasswordResetSuccessful) {
            onPasswordResetSuccess()
            viewModel.consumePasswordResetSuccess()
        }
    }

    ResetPasswordScreen(
        uiState = uiState,
        onNewPasswordChange = viewModel::onNewPasswordChange,
        onConfirmPasswordChange = viewModel::onConfirmPasswordChange,
        onToggleNewPasswordVisibility = viewModel::toggleNewPasswordVisibility,
        onToggleConfirmPasswordVisibility = viewModel::toggleConfirmPasswordVisibility,
        onBackClick = onBackClick,
        onResetPasswordClick = viewModel::resetPassword,
        modifier = modifier
    )
}

@Composable
fun ResetPasswordScreen(
    uiState: ResetPasswordUiState,
    onNewPasswordChange: (String) -> Unit,
    onConfirmPasswordChange: (String) -> Unit,
    onToggleNewPasswordVisibility: () -> Unit,
    onToggleConfirmPasswordVisibility: () -> Unit,
    onBackClick: () -> Unit,
    onResetPasswordClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    BoxWithConstraints(
        modifier = modifier
            .fillMaxSize()
            .background(ResetPasswordBackground)
            .safeDrawingPadding()
            .imePadding()
    ) {
        val compactHeight = maxHeight < 760.dp
        val illustrationWidth = (maxWidth * 0.60f).coerceAtMost(240.dp)

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
                        tint = ResetPasswordPrimary
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
                Spacer(modifier = Modifier.height(if (compactHeight) 12.dp else 18.dp))

                Text(
                    text = "Create New Password",
                    modifier = Modifier.fillMaxWidth(),
                    color = ResetPasswordPrimary,
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    lineHeight = 35.sp,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Enter your new password and make sure it’s strong and secure.",
                    modifier = Modifier.fillMaxWidth(),
                    color = ResetPasswordSecondaryText,
                    fontSize = 15.sp,
                    lineHeight = 22.sp,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(if (compactHeight) 16.dp else 20.dp))

                ResetPasswordField(
                    value = uiState.newPassword,
                    onValueChange = onNewPasswordChange,
                    label = "New Password",
                    error = uiState.newPasswordError,
                    isVisible = uiState.isNewPasswordVisible,
                    onToggleVisibility = onToggleNewPasswordVisibility,
                    enabled = !uiState.isLoading,
                    imeAction = ImeAction.Next
                )

                Spacer(modifier = Modifier.height(12.dp))

                ResetPasswordField(
                    value = uiState.confirmPassword,
                    onValueChange = onConfirmPasswordChange,
                    label = "Confirm New Password",
                    error = uiState.confirmPasswordError,
                    isVisible = uiState.isConfirmPasswordVisible,
                    onToggleVisibility = onToggleConfirmPasswordVisibility,
                    enabled = !uiState.isLoading,
                    imeAction = ImeAction.Done,
                    onDone = onResetPasswordClick
                )

                Spacer(modifier = Modifier.height(if (compactHeight) 14.dp else 18.dp))

                PasswordRequirements()

                Spacer(modifier = Modifier.height(if (compactHeight) 16.dp else 20.dp))

                Button(
                    onClick = onResetPasswordClick,
                    enabled = !uiState.isLoading,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = ResetPasswordPrimary,
                        contentColor = Color.White,
                        disabledContainerColor = ResetPasswordPrimary.copy(alpha = 0.65f),
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
                            text = "Reset Password",
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
                        color = ResetPasswordError,
                        fontSize = 13.sp,
                        lineHeight = 18.sp,
                        textAlign = TextAlign.Center
                    )
                }

                Spacer(modifier = Modifier.height(if (compactHeight) 10.dp else 14.dp))

                Image(
                    painter = painterResource(id = R.drawable.reset_password_illustration),
                    contentDescription = null,
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .width(illustrationWidth)
                        .aspectRatio(1f),
                    contentScale = ContentScale.Fit
                )

                Spacer(modifier = Modifier.height(if (compactHeight) 8.dp else 14.dp))
            }
        }
    }
}

@Composable
private fun ResetPasswordField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    error: String?,
    isVisible: Boolean,
    onToggleVisibility: () -> Unit,
    enabled: Boolean,
    imeAction: ImeAction,
    onDone: () -> Unit = {}
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = Modifier.fillMaxWidth(),
        enabled = enabled,
        label = { Text(text = label) },
        trailingIcon = {
            IconButton(
                onClick = onToggleVisibility,
                enabled = enabled
            ) {
                Icon(
                    imageVector = if (isVisible) VisibilityOffIcon else VisibilityIcon,
                    contentDescription = if (isVisible) "Hide $label" else "Show $label",
                    tint = ResetPasswordMutedText
                )
            }
        },
        supportingText = error?.let {
            {
                Text(
                    text = it,
                    fontSize = 12.sp,
                    lineHeight = 16.sp
                )
            }
        },
        isError = error != null,
        singleLine = true,
        shape = RoundedCornerShape(14.dp),
        visualTransformation = if (isVisible) {
            VisualTransformation.None
        } else {
            PasswordVisualTransformation()
        },
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Password,
            imeAction = imeAction
        ),
        keyboardActions = KeyboardActions(onDone = { onDone() }),
        colors = OutlinedTextFieldDefaults.colors(
            focusedTextColor = ResetPasswordPrimary,
            unfocusedTextColor = ResetPasswordPrimary,
            disabledTextColor = ResetPasswordPrimary,
            focusedBorderColor = ResetPasswordPrimary,
            unfocusedBorderColor = ResetPasswordBorder,
            disabledBorderColor = ResetPasswordBorder,
            errorBorderColor = ResetPasswordError,
            focusedLabelColor = ResetPasswordPrimary,
            unfocusedLabelColor = ResetPasswordSecondaryText,
            disabledLabelColor = ResetPasswordMutedText,
            errorLabelColor = ResetPasswordError,
            cursorColor = ResetPasswordPrimary,
            errorCursorColor = ResetPasswordError,
            focusedContainerColor = ResetPasswordSurface,
            unfocusedContainerColor = ResetPasswordSurface,
            disabledContainerColor = ResetPasswordSurface,
            errorContainerColor = ResetPasswordSurface,
            errorSupportingTextColor = ResetPasswordError
        )
    )
}

@Composable
private fun PasswordRequirements() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = ResetPasswordSurface,
                shape = RoundedCornerShape(14.dp)
            )
            .padding(horizontal = 14.dp, vertical = 12.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        PasswordRequirement(text = "At least 8 characters")
        PasswordRequirement(text = "Include letters and numbers")
        PasswordRequirement(text = "No spaces allowed")
    }
}

@Composable
private fun PasswordRequirement(text: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Icon(
            imageVector = Icons.Filled.CheckCircle,
            contentDescription = null,
            modifier = Modifier.size(18.dp),
            tint = ResetPasswordSecondary
        )
        Text(
            text = text,
            color = ResetPasswordSecondaryText,
            fontSize = 13.sp,
            lineHeight = 18.sp
        )
    }
}

private val VisibilityIcon: ImageVector by lazy {
    ImageVector.Builder(
        name = "Visibility",
        defaultWidth = 24.dp,
        defaultHeight = 24.dp,
        viewportWidth = 24f,
        viewportHeight = 24f
    ).apply {
        addPath(
            pathData = PathParser().parsePathString(
                "M12,4.5 C7,4.5 2.73,7.61 1,12 " +
                    "C2.73,16.39 7,19.5 12,19.5 " +
                    "C17,19.5 21.27,16.39 23,12 " +
                    "C21.27,7.61 17,4.5 12,4.5 Z " +
                    "M12,17 C9.24,17 7,14.76 7,12 " +
                    "C7,9.24 9.24,7 12,7 " +
                    "C14.76,7 17,9.24 17,12 " +
                    "C17,14.76 14.76,17 12,17 Z " +
                    "M12,9 C10.34,9 9,10.34 9,12 " +
                    "C9,13.66 10.34,15 12,15 " +
                    "C13.66,15 15,13.66 15,12 " +
                    "C15,10.34 13.66,9 12,9 Z"
            ).toNodes(),
            fill = SolidColor(Color.Black)
        )
    }.build()
}

private val VisibilityOffIcon: ImageVector by lazy {
    ImageVector.Builder(
        name = "VisibilityOff",
        defaultWidth = 24.dp,
        defaultHeight = 24.dp,
        viewportWidth = 24f,
        viewportHeight = 24f
    ).apply {
        addPath(
            pathData = PathParser().parsePathString(
                "M12,7 C14.76,7 17,9.24 17,12 " +
                    "C17,12.65 16.87,13.26 16.64,13.82 " +
                    "L19.56,16.74 C21.07,15.48 22.26,13.85 22.99,12 " +
                    "C21.26,7.61 16.99,4.5 11.99,4.5 " +
                    "C10.59,4.5 9.25,4.75 8.01,5.2 L10.17,7.36 " +
                    "C10.74,7.13 11.35,7 12,7 Z " +
                    "M2,4.27 L4.28,6.55 L4.73,7 " +
                    "C3.08,8.3 1.78,10.02 1,12 " +
                    "C2.73,16.39 7,19.5 12,19.5 " +
                    "C13.55,19.5 15.03,19.2 16.38,18.66 " +
                    "L16.8,19.08 L19.73,22 L21,20.73 L3.27,3 Z " +
                    "M7.53,9.8 L9.08,11.35 " +
                    "C9.03,11.56 9,11.78 9,12 " +
                    "C9,13.66 10.34,15 12,15 " +
                    "C12.22,15 12.44,14.97 12.65,14.92 " +
                    "L14.2,16.47 C13.53,16.8 12.79,17 12,17 " +
                    "C9.24,17 7,14.76 7,12 C7,11.21 7.2,10.47 7.53,9.8 Z " +
                    "M11.84,9.02 L14.99,12.17 L15,12 " +
                    "C15,10.34 13.66,9 12,9 Z"
            ).toNodes(),
            fill = SolidColor(Color.Black)
        )
    }.build()
}

@Preview(
    name = "Reset Password - Default",
    showBackground = true,
    widthDp = 390,
    heightDp = 844
)
@Composable
private fun ResetPasswordDefaultPreview() {
    BGrothTheme {
        ResetPasswordPreview(uiState = ResetPasswordUiState())
    }
}

@Preview(
    name = "Reset Password - Error",
    showBackground = true,
    widthDp = 390,
    heightDp = 844
)
@Composable
private fun ResetPasswordErrorPreview() {
    BGrothTheme {
        ResetPasswordPreview(
            uiState = ResetPasswordUiState(
                newPassword = "short",
                confirmPassword = "different",
                newPasswordError = "Password must contain at least 8 characters.",
                confirmPasswordError = "Passwords do not match."
            )
        )
    }
}

@Preview(
    name = "Reset Password - Loading",
    showBackground = true,
    widthDp = 390,
    heightDp = 844
)
@Composable
private fun ResetPasswordLoadingPreview() {
    BGrothTheme {
        ResetPasswordPreview(
            uiState = ResetPasswordUiState(
                newPassword = "Secure123",
                confirmPassword = "Secure123",
                isLoading = true
            )
        )
    }
}

@Composable
private fun ResetPasswordPreview(uiState: ResetPasswordUiState) {
    ResetPasswordScreen(
        uiState = uiState,
        onNewPasswordChange = {},
        onConfirmPasswordChange = {},
        onToggleNewPasswordVisibility = {},
        onToggleConfirmPasswordVisibility = {},
        onBackClick = {},
        onResetPasswordClick = {}
    )
}
