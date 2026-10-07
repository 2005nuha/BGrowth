package com.example.bgrowth.ui.register

import androidx.compose.foundation.background
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.PathParser
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
import com.example.bgrowth.ui.theme.BGrothTheme

private val RegisterPrimary =
    Color(0xFF0F5D46)

private val RegisterBackground =
    Color(0xFFF7F8F4)

private val RegisterText =
    Color(0xFF173B31)

private val RegisterSecondaryText =
    Color(0xFF68756F)

private val RegisterFieldBorder =
    Color(0xFFD7DDD9)

private val RegisterError =
    Color(0xFFBA1A1A)

@Composable
fun RegisterScreen(
    modifier: Modifier = Modifier,
    viewModel: RegisterViewModel = viewModel(),
    onLoginClick: () -> Unit = {},
    onBusinessSetupRequired: () -> Unit = {},
    onDashboardRequired: () -> Unit = {}
) {

    val uiState by
    viewModel.uiState.collectAsState()

    /*
     * بعد نجاح Register:
     *
     * hasBusiness = false
     * → Business Setup
     *
     * hasBusiness = true
     * → Dashboard
     */
    LaunchedEffect(
        uiState.isRegistrationSuccessful,
        uiState.hasBusiness
    ) {

        if (
            uiState.isRegistrationSuccessful
        ) {

            val hasBusiness =
                uiState.hasBusiness

            viewModel
                .consumeRegistrationSuccess()

            if (hasBusiness) {

                onDashboardRequired()

            } else {

                onBusinessSetupRequired()
            }
        }
    }

    RegisterScreen(
        uiState = uiState,

        onFirstNameChange =
            viewModel::onFirstNameChange,

        onLastNameChange =
            viewModel::onLastNameChange,

        onEmailChange =
            viewModel::onEmailChange,

        onPasswordChange =
            viewModel::onPasswordChange,

        onConfirmPasswordChange =
            viewModel::onConfirmPasswordChange,

        onTogglePasswordVisibility =
            viewModel::togglePasswordVisibility,

        onToggleConfirmPasswordVisibility =
            viewModel::toggleConfirmPasswordVisibility,

        onCreateAccountClick =
            viewModel::register,

        onLoginClick =
            onLoginClick,

        modifier =
            modifier
    )
}

@Composable
fun RegisterScreen(
    uiState: RegisterUiState,
    onFirstNameChange: (String) -> Unit,
    onLastNameChange: (String) -> Unit,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onConfirmPasswordChange: (String) -> Unit,
    onTogglePasswordVisibility: () -> Unit,
    onToggleConfirmPasswordVisibility: () -> Unit,
    onCreateAccountClick: () -> Unit,
    onLoginClick: () -> Unit,
    modifier: Modifier = Modifier
) {

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                RegisterBackground
            )
            .safeDrawingPadding()
            .imePadding()
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(
                    rememberScrollState()
                )
                .padding(
                    horizontal = 24.dp
                ),
            horizontalAlignment =
                Alignment.CenterHorizontally
        ) {

            Spacer(
                modifier =
                    Modifier.height(34.dp)
            )

            Text(
                text =
                    "Create Your Account",
                modifier =
                    Modifier.fillMaxWidth(),
                color =
                    RegisterPrimary,
                fontSize =
                    28.sp,
                fontWeight =
                    FontWeight.Bold,
                lineHeight =
                    35.sp,
                textAlign =
                    TextAlign.Center
            )

            Spacer(
                modifier =
                    Modifier.height(8.dp)
            )

            Text(
                text =
                    "Start managing your business with BGrowth.",
                modifier =
                    Modifier.fillMaxWidth(),
                color =
                    RegisterSecondaryText,
                fontSize =
                    15.sp,
                lineHeight =
                    22.sp,
                textAlign =
                    TextAlign.Center
            )

            Spacer(
                modifier =
                    Modifier.height(28.dp)
            )

            /*
             * First Name
             */
            RegisterTextField(
                value =
                    uiState.firstName,

                onValueChange =
                    onFirstNameChange,

                label =
                    "First Name",

                error =
                    uiState.firstNameError,

                keyboardType =
                    KeyboardType.Text,

                imeAction =
                    ImeAction.Next,

                enabled =
                    !uiState.isLoading,

                trailingIcon = {

                    RegisterFieldIcon(
                        imageVector =
                            Icons.Filled.Person,
                        contentDescription =
                            "First name"
                    )
                }
            )

            Spacer(
                modifier =
                    Modifier.height(12.dp)
            )

            /*
             * Last Name
             */
            RegisterTextField(
                value =
                    uiState.lastName,

                onValueChange =
                    onLastNameChange,

                label =
                    "Last Name",

                error =
                    uiState.lastNameError,

                keyboardType =
                    KeyboardType.Text,

                imeAction =
                    ImeAction.Next,

                enabled =
                    !uiState.isLoading,

                trailingIcon = {

                    RegisterFieldIcon(
                        imageVector =
                            Icons.Filled.Person,
                        contentDescription =
                            "Last name"
                    )
                }
            )

            Spacer(
                modifier =
                    Modifier.height(12.dp)
            )

            /*
             * Email
             */
            RegisterTextField(
                value =
                    uiState.email,

                onValueChange =
                    onEmailChange,

                label =
                    "Email Address",

                error =
                    uiState.emailError,

                keyboardType =
                    KeyboardType.Email,

                imeAction =
                    ImeAction.Next,

                enabled =
                    !uiState.isLoading,

                trailingIcon = {

                    RegisterFieldIcon(
                        imageVector =
                            Icons.Filled.Email,
                        contentDescription =
                            "Email address"
                    )
                }
            )

            Spacer(
                modifier =
                    Modifier.height(12.dp)
            )

            /*
             * Password
             */
            RegisterTextField(
                value =
                    uiState.password,

                onValueChange =
                    onPasswordChange,

                label =
                    "Password",

                error =
                    uiState.passwordError,

                keyboardType =
                    KeyboardType.Password,

                imeAction =
                    ImeAction.Next,

                enabled =
                    !uiState.isLoading,

                visualTransformation =
                    if (
                        uiState.isPasswordVisible
                    ) {

                        VisualTransformation.None

                    } else {

                        PasswordVisualTransformation()
                    },

                trailingIcon = {

                    PasswordVisibilityButton(
                        isVisible =
                            uiState.isPasswordVisible,

                        onClick =
                            onTogglePasswordVisibility,

                        enabled =
                            !uiState.isLoading,

                        description =
                            if (
                                uiState.isPasswordVisible
                            ) {
                                "Hide password"
                            } else {
                                "Show password"
                            }
                    )
                }
            )

            Spacer(
                modifier =
                    Modifier.height(12.dp)
            )

            /*
             * Confirm Password
             */
            RegisterTextField(
                value =
                    uiState.confirmPassword,

                onValueChange =
                    onConfirmPasswordChange,

                label =
                    "Confirm Password",

                error =
                    uiState.confirmPasswordError,

                keyboardType =
                    KeyboardType.Password,

                imeAction =
                    ImeAction.Done,

                enabled =
                    !uiState.isLoading,

                visualTransformation =
                    if (
                        uiState
                            .isConfirmPasswordVisible
                    ) {

                        VisualTransformation.None

                    } else {

                        PasswordVisualTransformation()
                    },

                trailingIcon = {

                    PasswordVisibilityButton(
                        isVisible =
                            uiState
                                .isConfirmPasswordVisible,

                        onClick =
                            onToggleConfirmPasswordVisibility,

                        enabled =
                            !uiState.isLoading,

                        description =
                            if (
                                uiState
                                    .isConfirmPasswordVisible
                            ) {
                                "Hide confirm password"
                            } else {
                                "Show confirm password"
                            }
                    )
                }
            )

            Spacer(
                modifier =
                    Modifier.height(24.dp)
            )

            Button(
                onClick =
                    onCreateAccountClick,

                enabled =
                    !uiState.isLoading,

                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),

                shape =
                    RoundedCornerShape(
                        16.dp
                    ),

                colors =
                    ButtonDefaults
                        .buttonColors(
                            containerColor =
                                RegisterPrimary,

                            contentColor =
                                Color.White,

                            disabledContainerColor =
                                RegisterPrimary
                                    .copy(
                                        alpha = 0.65f
                                    ),

                            disabledContentColor =
                                Color.White
                        ),

                elevation =
                    ButtonDefaults
                        .buttonElevation(
                            defaultElevation =
                                0.dp
                        )
            ) {

                if (uiState.isLoading) {

                    CircularProgressIndicator(
                        modifier =
                            Modifier.size(
                                22.dp
                            ),
                        color =
                            Color.White,
                        strokeWidth =
                            2.dp
                    )

                } else {

                    Text(
                        text =
                            "Create Account",
                        fontSize =
                            16.sp,
                        fontWeight =
                            FontWeight.SemiBold
                    )
                }
            }

            uiState.registrationError
                ?.let { error ->

                    Spacer(
                        modifier =
                            Modifier.height(
                                10.dp
                            )
                    )

                    Text(
                        text =
                            error,

                        modifier =
                            Modifier.fillMaxWidth(),

                        color =
                            RegisterError,

                        fontSize =
                            13.sp,

                        lineHeight =
                            18.sp,

                        textAlign =
                            TextAlign.Center
                    )
                }

            Spacer(
                modifier =
                    Modifier.height(8.dp)
            )

            Row(
                modifier =
                    Modifier.fillMaxWidth(),

                horizontalArrangement =
                    Arrangement.Center,

                verticalAlignment =
                    Alignment.CenterVertically
            ) {

                Text(
                    text =
                        "Already have an account?",

                    color =
                        RegisterSecondaryText,

                    fontSize =
                        14.sp
                )

                TextButton(
                    onClick =
                        onLoginClick,

                    enabled =
                        !uiState.isLoading
                ) {

                    Text(
                        text =
                            "Log in",

                        color =
                            RegisterPrimary,

                        fontSize =
                            14.sp,

                        fontWeight =
                            FontWeight.SemiBold
                    )
                }
            }

            Spacer(
                modifier =
                    Modifier.height(18.dp)
            )
        }
    }
}

@Composable
private fun RegisterTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    error: String?,
    keyboardType: KeyboardType,
    imeAction: ImeAction,
    enabled: Boolean,
    trailingIcon: @Composable () -> Unit,
    visualTransformation:
    VisualTransformation =
        VisualTransformation.None
) {

    OutlinedTextField(
        value =
            value,

        onValueChange =
            onValueChange,

        modifier =
            Modifier.fillMaxWidth(),

        enabled =
            enabled,

        label = {
            Text(
                text = label
            )
        },

        trailingIcon =
            trailingIcon,

        supportingText =
            error?.let {

                {
                    Text(
                        text =
                            it,

                        fontSize =
                            12.sp,

                        lineHeight =
                            16.sp
                    )
                }
            },

        isError =
            error != null,

        singleLine =
            true,

        shape =
            RoundedCornerShape(
                14.dp
            ),

        visualTransformation =
            visualTransformation,

        keyboardOptions =
            KeyboardOptions(
                keyboardType =
                    keyboardType,

                imeAction =
                    imeAction
            ),

        colors =
            OutlinedTextFieldDefaults
                .colors(
                    focusedTextColor =
                        RegisterText,

                    unfocusedTextColor =
                        RegisterText,

                    disabledTextColor =
                        RegisterText,

                    focusedBorderColor =
                        RegisterPrimary,

                    unfocusedBorderColor =
                        RegisterFieldBorder,

                    disabledBorderColor =
                        RegisterFieldBorder,

                    errorBorderColor =
                        RegisterError,

                    focusedLabelColor =
                        RegisterPrimary,

                    unfocusedLabelColor =
                        RegisterSecondaryText,

                    disabledLabelColor =
                        RegisterSecondaryText,

                    errorLabelColor =
                        RegisterError,

                    cursorColor =
                        RegisterPrimary,

                    errorCursorColor =
                        RegisterError,

                    focusedContainerColor =
                        Color.White,

                    unfocusedContainerColor =
                        Color.White,

                    disabledContainerColor =
                        Color.White,

                    errorContainerColor =
                        Color.White,

                    errorSupportingTextColor =
                        RegisterError
                )
    )
}

@Composable
private fun RegisterFieldIcon(
    imageVector: ImageVector,
    contentDescription: String
) {

    Icon(
        imageVector =
            imageVector,

        contentDescription =
            contentDescription,

        tint =
            RegisterSecondaryText
    )
}

@Composable
private fun PasswordVisibilityButton(
    isVisible: Boolean,
    onClick: () -> Unit,
    enabled: Boolean,
    description: String
) {

    IconButton(
        onClick =
            onClick,

        enabled =
            enabled
    ) {

        Icon(
            imageVector =
                if (isVisible) {
                    VisibilityOffIcon
                } else {
                    VisibilityIcon
                },

            contentDescription =
                description,

            tint =
                RegisterSecondaryText
        )
    }
}

private val VisibilityIcon:
        ImageVector by lazy {

    ImageVector.Builder(
        name =
            "Visibility",

        defaultWidth =
            24.dp,

        defaultHeight =
            24.dp,

        viewportWidth =
            24f,

        viewportHeight =
            24f
    ).apply {

        addPath(
            pathData =
                PathParser()
                    .parsePathString(
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
                    )
                    .toNodes(),

            fill =
                SolidColor(
                    Color.Black
                )
        )
    }.build()
}

private val VisibilityOffIcon:
        ImageVector by lazy {

    ImageVector.Builder(
        name =
            "VisibilityOff",

        defaultWidth =
            24.dp,

        defaultHeight =
            24.dp,

        viewportWidth =
            24f,

        viewportHeight =
            24f
    ).apply {

        addPath(
            pathData =
                PathParser()
                    .parsePathString(
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
                    )
                    .toNodes(),

            fill =
                SolidColor(
                    Color.Black
                )
        )
    }.build()
}

@Preview(
    name =
        "Register - Default",
    showBackground =
        true,
    widthDp =
        390,
    heightDp =
        844
)
@Composable
private fun RegisterDefaultPreview() {

    BGrothTheme {

        RegisterPreview(
            uiState =
                RegisterUiState()
        )
    }
}

@Preview(
    name =
        "Register - Error",
    showBackground =
        true,
    widthDp =
        390,
    heightDp =
        844
)
@Composable
private fun RegisterErrorPreview() {

    BGrothTheme {

        RegisterPreview(
            uiState =
                RegisterUiState(
                    firstName =
                        "Hazem",

                    lastName =
                        "Hendawi",

                    email =
                        "hazem@",

                    password =
                        "short",

                    confirmPassword =
                        "different",

                    emailError =
                        "Enter a valid email address.",

                    passwordError =
                        "Password must contain at least 8 characters.",

                    confirmPasswordError =
                        "Passwords do not match."
                )
        )
    }
}

@Preview(
    name =
        "Register - Loading",
    showBackground =
        true,
    widthDp =
        390,
    heightDp =
        844
)
@Composable
private fun RegisterLoadingPreview() {

    BGrothTheme {

        RegisterPreview(
            uiState =
                RegisterUiState(
                    firstName =
                        "Hazem",

                    lastName =
                        "Hendawi",

                    email =
                        "hazem@example.com",

                    password =
                        "password123",

                    confirmPassword =
                        "password123",

                    isLoading =
                        true
                )
        )
    }
}

@Composable
private fun RegisterPreview(
    uiState: RegisterUiState
) {

    RegisterScreen(
        uiState =
            uiState,

        onFirstNameChange = {},
        onLastNameChange = {},
        onEmailChange = {},
        onPasswordChange = {},
        onConfirmPasswordChange = {},
        onTogglePasswordVisibility = {},
        onToggleConfirmPasswordVisibility = {},
        onCreateAccountClick = {},
        onLoginClick = {}
    )
}