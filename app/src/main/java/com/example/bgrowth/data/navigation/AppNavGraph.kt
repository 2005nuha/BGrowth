package com.example.bgrowth.data.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
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
import com.example.bgrowth.data.repository.AuthRepository
import com.example.bgrowth.ui.businesssetup.BusinessSetupScreen
import com.example.bgrowth.ui.dashboard.DashboardRoute
import com.example.bgrowth.ui.forgotpassword.ForgotPasswordScreen
import com.example.bgrowth.ui.login.LoginScreen
import com.example.bgrowth.ui.login.LoginViewModel
import com.example.bgrowth.ui.onboarding.OnboardingPagerScreen
import com.example.bgrowth.ui.product.AddProductScreen
import com.example.bgrowth.ui.product.ProductsScreen
import com.example.bgrowth.ui.register.RegisterScreen
import com.example.bgrowth.ui.register.RegisterViewModel
import com.example.bgrowth.ui.resetpassword.ResetPasswordScreen
import com.example.bgrowth.ui.sales.RecordSaleScreen
import com.example.bgrowth.ui.sales.SalesHistoryScreen
import com.example.bgrowth.ui.splash.SplashScreen
import com.example.bgrowth.ui.verification.VerificationMode
import com.example.bgrowth.ui.verification.VerificationScreen
import com.example.bgrowth.ui.verification.VerificationViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

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

        // --------------------------------------------------
        // Splash
        // --------------------------------------------------

        composable(Routes.SPLASH) {

            LaunchedEffect(Unit) {
                delay(SPLASH_DURATION_MILLIS)

                navController.navigate(Routes.ONBOARDING) {
                    popUpTo(Routes.SPLASH) {
                        inclusive = true
                    }

                    launchSingleTop = true
                }
            }

            SplashScreen()
        }

        // --------------------------------------------------
        // Onboarding
        // --------------------------------------------------

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

        // --------------------------------------------------
        // Register
        // --------------------------------------------------

        composable(Routes.REGISTER) {

            val registerViewModel: RegisterViewModel =
                viewModel()

            RegisterScreen(
                viewModel = registerViewModel,

                onLoginClick = {
                    navController.navigate(Routes.LOGIN) {
                        launchSingleTop = true
                    }
                },

                onRegistrationSuccess = {
                    navController.navigate(
                        Routes.BUSINESS_SETUP
                    ) {
                        popUpTo(Routes.REGISTER) {
                            inclusive = true
                        }

                        launchSingleTop = true
                    }
                }
            )
        }

        // --------------------------------------------------
        // Verification
        // --------------------------------------------------

        composable(
            route = Routes.VERIFICATION_ROUTE,
            arguments = listOf(

                navArgument(
                    Routes.VERIFICATION_MODE_ARGUMENT
                ) {
                    type = NavType.StringType
                },

                navArgument(
                    Routes.VERIFICATION_TARGET_ARGUMENT
                ) {
                    type = NavType.StringType
                }
            )
        ) { backStackEntry ->

            val verificationTarget =
                backStackEntry.arguments
                    ?.getString(
                        Routes.VERIFICATION_TARGET_ARGUMENT
                    )
                    .orEmpty()

            val verificationMode =
                VerificationMode.fromRouteValue(
                    backStackEntry.arguments
                        ?.getString(
                            Routes.VERIFICATION_MODE_ARGUMENT
                        )
                )

            val factory = remember {
                verificationViewModelFactory()
            }

            val verificationViewModel:
                    VerificationViewModel =
                viewModel(factory = factory)

            VerificationScreen(
                verificationTarget = verificationTarget,
                verificationMode = verificationMode,
                viewModel = verificationViewModel,

                onBackClick = {
                    navController.popBackStack()
                },

                onVerificationSuccess = {

                    when (verificationMode) {

                        VerificationMode.SIGN_UP -> {

                            navController.navigate(
                                Routes.BUSINESS_SETUP
                            ) {
                                launchSingleTop = true
                            }
                        }

                        VerificationMode.PASSWORD_RESET -> {

                            navController.navigate(
                                Routes.RESET_PASSWORD
                            ) {
                                launchSingleTop = true
                            }
                        }
                    }
                }
            )
        }

        // --------------------------------------------------
        // Forgot Password
        // --------------------------------------------------

        composable(Routes.FORGOT_PASSWORD) {

            ForgotPasswordScreen(
                onBackClick = {
                    navController.popBackStack()
                },

                onCodeSent = { email ->

                    navController.navigate(
                        Routes.verification(
                            verificationTarget = email,
                            verificationMode =
                                VerificationMode.PASSWORD_RESET
                        )
                    ) {
                        launchSingleTop = true
                    }
                }
            )
        }

        // --------------------------------------------------
        // Reset Password
        // --------------------------------------------------

        composable(Routes.RESET_PASSWORD) {

            ResetPasswordScreen(
                onBackClick = {
                    navController.popBackStack()
                },

                onPasswordResetSuccess = {

                    navController.navigate(
                        Routes.LOGIN
                    ) {

                        popUpTo(
                            Routes.FORGOT_PASSWORD
                        ) {
                            inclusive = true
                        }

                        launchSingleTop = true
                    }
                }
            )
        }

        // --------------------------------------------------
        // Login
        // --------------------------------------------------

        composable(Routes.LOGIN) {

            val loginViewModel:
                    LoginViewModel = viewModel()

            LoginScreen(
                viewModel = loginViewModel,

                onBackClick = {
                    navController.popBackStack()
                },

                onLoginSuccess = {

                    navController.navigate(
                        Routes.BUSINESS_SETUP
                    ) {

                        popUpTo(Routes.LOGIN) {
                            inclusive = true
                        }

                        launchSingleTop = true
                    }
                },

                onCreateAccountClick = {

                    navController.navigate(
                        Routes.REGISTER
                    ) {
                        launchSingleTop = true
                    }
                },

                onForgotPasswordClick = {

                    navController.navigate(
                        Routes.FORGOT_PASSWORD
                    ) {
                        launchSingleTop = true
                    }
                }
            )
        }

        // --------------------------------------------------
        // Business Setup
        // --------------------------------------------------

        composable(Routes.BUSINESS_SETUP) {

            BusinessSetupScreen(

                onBackClick = {
                    navController.popBackStack()
                },

                onBusinessSetupSuccess = {

                    navController.navigate(
                        Routes.DASHBOARD
                    ) {

                        popUpTo(
                            Routes.BUSINESS_SETUP
                        ) {
                            inclusive = true
                        }

                        launchSingleTop = true
                    }
                }
            )
        }

        // --------------------------------------------------
        // Dashboard
        // --------------------------------------------------

        composable(Routes.DASHBOARD) {

            val authRepository =
                remember {
                    AuthRepository()
                }

            val coroutineScope =
                rememberCoroutineScope()

            DashboardRoute(

                // -------------------------
                // Sprint 2 navigation
                // -------------------------

                onRecordSaleClick = {

                    navController.navigate(
                        Routes.RECORD_SALE
                    ) {
                        launchSingleTop = true
                    }
                },

                onAddProductClick = {

                    navController.navigate(
                        Routes.ADD_PRODUCT
                    ) {
                        launchSingleTop = true
                    }
                },

                onProductsClick = {

                    navController.navigate(
                        Routes.PRODUCTS
                    ) {
                        launchSingleTop = true
                    }
                },

                onSalesClick = {

                    navController.navigate(
                        Routes.SALES_HISTORY
                    ) {
                        launchSingleTop = true
                    }
                },

                // -------------------------
                // Drawer navigation
                // -------------------------

                onProfileClick = {},

                onNotificationsClick = {},

                onBusinessSettingsClick = {

                    navController.navigate(
                        Routes.BUSINESS_SETUP
                    ) {
                        launchSingleTop = true
                    }
                },

                onLanguageClick = {},

                onDarkModeClick = {},

                onPrivacySecurityClick = {},

                onLogoutClick = {

                    coroutineScope.launch {

                        try {
                            authRepository.logout()
                        } finally {

                            navController.navigate(
                                Routes.LOGIN
                            ) {

                                popUpTo(
                                    navController.graph.id
                                ) {
                                    inclusive = true
                                }

                                launchSingleTop = true
                            }
                        }
                    }
                }
            )
        }

        // --------------------------------------------------
        // Products
        // --------------------------------------------------

        composable(Routes.PRODUCTS) {

            ProductsScreen(

                onBackClick = {
                    navController.popBackStack()
                },

                onAddProductClick = {

                    navController.navigate(
                        Routes.ADD_PRODUCT
                    ) {
                        launchSingleTop = true
                    }
                },

                onProductClick = { productId ->

                    // TODO:
                    // Edit Product / Product Details later
                    // using productId.
                }
            )
        }

        // --------------------------------------------------
        // Add Product
        // --------------------------------------------------

        composable(Routes.ADD_PRODUCT) {

            AddProductScreen(

                onBackClick = {
                    navController.popBackStack()
                },

                onCancelClick = {
                    navController.popBackStack()
                },

                onProductSaved = {
                    navController.popBackStack()
                }
            )
        }

        // --------------------------------------------------
        // Record Sale
        // --------------------------------------------------

        composable(Routes.RECORD_SALE) {

            RecordSaleScreen(

                onBackClick = {
                    navController.popBackStack()
                },

                onViewAllClick = {

                    navController.navigate(
                        Routes.SALES_HISTORY
                    ) {
                        launchSingleTop = true
                    }
                }
            )
        }

        // --------------------------------------------------
        // Sales History
        // --------------------------------------------------

        composable(Routes.SALES_HISTORY) {

            SalesHistoryScreen(

                onBackClick = {
                    navController.popBackStack()
                },

                onAddSaleClick = {

                    navController.navigate(
                        Routes.RECORD_SALE
                    ) {
                        launchSingleTop = true
                    }
                },

                onSaleClick = { saleId ->

                    // TODO:
                    // Edit Sale / Sale Details later
                    // using saleId.
                }
            )
        }
    }
}


// --------------------------------------------------
// Verification ViewModel Factory
// --------------------------------------------------

private fun verificationViewModelFactory():
        ViewModelProvider.Factory =

    object : ViewModelProvider.Factory {

        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(
            modelClass: Class<T>
        ): T {

            if (
                !modelClass.isAssignableFrom(
                    VerificationViewModel::class.java
                )
            ) {
                throw IllegalArgumentException(
                    "Unknown ViewModel class: ${modelClass.name}"
                )
            }

            return VerificationViewModel() as T
        }
    }


// --------------------------------------------------
// Splash duration
// --------------------------------------------------

private const val SPLASH_DURATION_MILLIS =
    1_750L