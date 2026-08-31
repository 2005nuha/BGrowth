package com.example.bgrowth.data.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.bgrowth.ui.forgotpassword.ForgotPasswordScreen
import com.example.bgrowth.ui.login.LoginScreen
import com.example.bgrowth.ui.onboarding.OnboardingPagerScreen
import com.example.bgrowth.ui.register.RegisterScreen
import com.example.bgrowth.ui.register.RegisterViewModel
import com.example.bgrowth.ui.resetpassword.ResetPasswordScreen
import com.example.bgrowth.ui.splash.SplashScreen
import com.example.bgrowth.ui.verification.ResendOtpAction
import com.example.bgrowth.ui.verification.VerificationMode
import com.example.bgrowth.ui.verification.VerificationScreen
import com.example.bgrowth.ui.verification.VerificationViewModel
import com.example.bgrowth.ui.verification.VerifyOtpAction
import kotlinx.coroutines.delay

@Composable
fun AppNavGraph(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        navController = navController,
        startDestination = Routes.SPLASH,
        modifier = modifier
    ) {
        composable(Routes.SPLASH) {
            LaunchedEffect(Unit) {
                delay(SPLASH_DURATION_MILLIS)
                navController.navigate(Routes.ONBOARDING) {
                    popUpTo(Routes.SPLASH) { inclusive = true }
                    launchSingleTop = true
                }
            }
            SplashScreen()
        }

        composable(Routes.ONBOARDING) {
            val openRegister = {
                navController.navigate(Routes.REGISTER) {
                    launchSingleTop = true
                }
            }

            OnboardingPagerScreen(
                onFinished = openRegister,
                onSkipClick = openRegister
            )
        }

        composable(Routes.REGISTER) {
            val registerViewModel: RegisterViewModel = viewModel()

            RegisterScreen(
                viewModel = registerViewModel,
                onLoginClick = {
                    navController.navigate(Routes.LOGIN) {
                        launchSingleTop = true
                    }
                },
                onCreateAccountClick = {
                    // TODO: Replace with real backend API
                    if (registerViewModel.validateForLocalNavigation()) {
                        val phoneNumber = registerViewModel.uiState.value.phoneNumber.trim()
                        navController.navigate(
                            Routes.verification(
                                verificationTarget = phoneNumber,
                                verificationMode = VerificationMode.SIGN_UP
                            )
                        ) {
                            launchSingleTop = true
                        }
                    }
                }
            )
        }

        composable(
            route = Routes.VERIFICATION_ROUTE,
            arguments = listOf(
                navArgument(Routes.VERIFICATION_MODE_ARGUMENT) {
                    type = NavType.StringType
                },
                navArgument(Routes.VERIFICATION_TARGET_ARGUMENT) {
                    type = NavType.StringType
                }
            )
        ) { backStackEntry ->
            val verificationTarget = backStackEntry.arguments
                ?.getString(Routes.VERIFICATION_TARGET_ARGUMENT)
                .orEmpty()
            val verificationMode = VerificationMode.fromRouteValue(
                backStackEntry.arguments
                    ?.getString(Routes.VERIFICATION_MODE_ARGUMENT)
            )
            val factory = remember(verificationMode) {
                localVerificationViewModelFactory()
            }
            val verificationViewModel: VerificationViewModel = viewModel(
                factory = factory
            )

            VerificationScreen(
                verificationTarget = verificationTarget,
                verificationMode = verificationMode,
                viewModel = verificationViewModel,
                onBackClick = { navController.popBackStack() },
                onVerificationSuccess = {
                    when (verificationMode) {
                        VerificationMode.SIGN_UP -> {
                            navController.navigate(Routes.LOGIN) {
                                launchSingleTop = true
                            }
                        }

                        VerificationMode.PASSWORD_RESET -> {
                            navController.navigate(Routes.RESET_PASSWORD) {
                                launchSingleTop = true
                            }
                        }
                    }
                }
            )
        }

        composable(Routes.FORGOT_PASSWORD) {
            ForgotPasswordScreen(
                onBackClick = { navController.popBackStack() },
                onCodeSent = { email ->
                    navController.navigate(
                        Routes.verification(
                            verificationTarget = email,
                            verificationMode = VerificationMode.PASSWORD_RESET
                        )
                    ) {
                        launchSingleTop = true
                    }
                }
            )
        }

        composable(Routes.RESET_PASSWORD) {
            ResetPasswordScreen(
                onBackClick = { navController.popBackStack() },
                onPasswordResetSuccess = {
                    navController.navigate(Routes.LOGIN) {
                        popUpTo(Routes.FORGOT_PASSWORD) { inclusive = true }
                        launchSingleTop = true
                    }
                }
            )
        }

        composable(Routes.LOGIN) {
            LoginScreen(
                onBackClick = { navController.popBackStack() },
                onCreateAccountClick = {
                    navController.navigate(Routes.REGISTER) {
                        launchSingleTop = true
                    }
                },
                onForgotPasswordClick = {
                    navController.navigate(Routes.FORGOT_PASSWORD) {
                        launchSingleTop = true
                    }
                }
            )
        }
    }
}

private fun localVerificationViewModelFactory(): ViewModelProvider.Factory =
    object : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (!modelClass.isAssignableFrom(VerificationViewModel::class.java)) {
                throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
            }

            return VerificationViewModel(
                verifyOtpAction = VerifyOtpAction { _, otpCode ->
                    // TODO: Replace with real OTP verification API
                    delay(LOCAL_REQUEST_DELAY_MILLIS)
                    if (otpCode != LOCAL_TEST_OTP) {
                        throw IllegalArgumentException(
                            "The code you entered is invalid. Please try again."
                        )
                    }
                },
                resendOtpAction = ResendOtpAction {
                    // TODO: Replace with real OTP verification API
                    delay(LOCAL_REQUEST_DELAY_MILLIS)
                }
            ) as T
        }
    }

private const val SPLASH_DURATION_MILLIS = 1_750L
private const val LOCAL_REQUEST_DELAY_MILLIS = 450L
private const val LOCAL_TEST_OTP = "123456"
