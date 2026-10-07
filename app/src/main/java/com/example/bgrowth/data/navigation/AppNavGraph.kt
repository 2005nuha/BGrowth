package com.example.bgrowth.data.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
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
import kotlinx.coroutines.launch
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.example.bgrowth.ui.splash.SplashDestination
import com.example.bgrowth.ui.splash.SplashViewModel
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.example.bgrowth.ui.product.EditProductScreen
import com.example.bgrowth.ui.product.AdjustStockScreen


@Composable
fun AppNavGraph(
    modifier: Modifier = Modifier,
    navController: NavHostController =
        rememberNavController()
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

            val splashViewModel:
                    SplashViewModel =
                viewModel()

            val splashUiState by
            splashViewModel
                .uiState
                .collectAsState()

            LaunchedEffect(
                splashUiState.destination
            ) {

                val destination =
                    splashUiState.destination
                        ?: return@LaunchedEffect

                val route =
                    when (destination) {

                        SplashDestination.ONBOARDING ->
                            Routes.ONBOARDING

                        SplashDestination.BUSINESS_SETUP ->
                            Routes.BUSINESS_SETUP

                        SplashDestination.DASHBOARD ->
                            Routes.DASHBOARD
                    }

                navController.navigate(
                    route
                ) {

                    popUpTo(
                        Routes.SPLASH
                    ) {
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

        composable(
            Routes.ONBOARDING
        ) {

            val openRegister = {

                navController.navigate(
                    Routes.REGISTER
                ) {
                    launchSingleTop = true
                }
            }

            OnboardingPagerScreen(
                onFinished =
                    openRegister,

                onSkipClick =
                    openRegister
            )
        }

        // --------------------------------------------------
        // Register
        // --------------------------------------------------

        composable(
            Routes.REGISTER
        ) {

            val registerViewModel:
                    RegisterViewModel =
                viewModel()

            RegisterScreen(
                viewModel =
                    registerViewModel,

                onLoginClick = {

                    navController.navigate(
                        Routes.LOGIN
                    ) {
                        launchSingleTop = true
                    }
                },

                /*
                 * Register نجح،
                 * ولكن لا يوجد Business.
                 */
                onBusinessSetupRequired = {

                    navController.navigate(
                        Routes.BUSINESS_SETUP
                    ) {

                        popUpTo(
                            Routes.REGISTER
                        ) {
                            inclusive = true
                        }

                        launchSingleTop = true
                    }
                },

                /*
                 * Register نجح،
                 * ويوجد Business.
                 *
                 * هذا غير متوقع غالبًا
                 * للحساب الجديد، لكن الـFlow
                 * يبقى صحيحًا لو حصل.
                 */
                onDashboardRequired = {

                    navController.navigate(
                        Routes.DASHBOARD
                    ) {

                        popUpTo(
                            Routes.REGISTER
                        ) {
                            inclusive = true
                        }

                        launchSingleTop = true
                    }
                }
            )
        }

        // --------------------------------------------------
        // Forgot Password
        // --------------------------------------------------

        composable(
            Routes.FORGOT_PASSWORD
        ) {

            ForgotPasswordScreen(

                onBackClick = {
                    navController
                        .popBackStack()
                },

                /*
                 * مؤقتًا ننتقل إلى Reset Password.
                 *
                 * سنراجع Contract الخاص
                 * Password Reset في الخطوة
                 * المخصصة له قبل اعتماده نهائيًا.
                 */
                onCodeSent = {

                    navController.navigate(
                        Routes.RESET_PASSWORD
                    ) {
                        launchSingleTop = true
                    }
                }
            )
        }

        // --------------------------------------------------
        // Reset Password
        // --------------------------------------------------

        composable(
            Routes.RESET_PASSWORD
        ) {

            ResetPasswordScreen(

                onBackClick = {
                    navController
                        .popBackStack()
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

        composable(
            Routes.LOGIN
        ) {

            val loginViewModel:
                    LoginViewModel =
                viewModel()

            LoginScreen(
                viewModel =
                    loginViewModel,

                onBackClick = {
                    navController
                        .popBackStack()
                },

                /*
                 * Login نجح،
                 * ولا يوجد Business.
                 */
                onBusinessSetupRequired = {

                    navController.navigate(
                        Routes.BUSINESS_SETUP
                    ) {

                        popUpTo(
                            Routes.LOGIN
                        ) {
                            inclusive = true
                        }

                        launchSingleTop = true
                    }
                },

                /*
                 * Login نجح،
                 * ويوجد Business.
                 */
                onDashboardRequired = {

                    navController.navigate(
                        Routes.DASHBOARD
                    ) {

                        popUpTo(
                            Routes.LOGIN
                        ) {
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

        composable(
            Routes.BUSINESS_SETUP
        ) {

            BusinessSetupScreen(

                onBackClick = {
                    navController
                        .popBackStack()
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

        composable(
            Routes.DASHBOARD
        ) {

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
                                    navController
                                        .graph
                                        .id
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

        composable(
            Routes.PRODUCTS
        ) { backStackEntry ->

            val productsNeedRefresh by
            backStackEntry.savedStateHandle
                .getStateFlow(
                    "products_need_refresh",
                    false
                )
                .collectAsState()

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

                    navController.navigate(
                        Routes.editProduct(
                            productId = productId
                        )
                    ) {
                        launchSingleTop = true
                    }
                },

                onAdjustStockClick = { productId ->

                    navController.navigate(
                        Routes.adjustStock(
                            productId = productId
                        )
                    ) {
                        launchSingleTop = true
                    }
                },

                refreshRequested = productsNeedRefresh,

                onRefreshConsumed = {
                    backStackEntry.savedStateHandle[
                        "products_need_refresh"
                    ] = false
                }
            )
        }
        // --------------------------------------------------
        // Add Product
        // --------------------------------------------------

        composable(
            Routes.ADD_PRODUCT
        ) {

            AddProductScreen(

                onBackClick = {
                    navController
                        .popBackStack()
                },

                onCancelClick = {
                    navController
                        .popBackStack()
                },

                onProductSaved = {

                    navController.previousBackStackEntry
                        ?.savedStateHandle
                        ?.set("products_need_refresh", true)

                    navController.popBackStack()
                }
            )
        }
        composable(
            route = Routes.EDIT_PRODUCT_ROUTE,
            arguments = listOf(
                navArgument(Routes.PRODUCT_ID_ARGUMENT) {
                    type = NavType.IntType
                }
            )
        ) { backStackEntry ->

            val productId =
                backStackEntry.arguments
                    ?.getInt(Routes.PRODUCT_ID_ARGUMENT)
                    ?: return@composable

            EditProductScreen(
                productId = productId,
                onBackClick = {
                    navController.popBackStack()
                },
                onProductUpdated = {

                    navController.previousBackStackEntry
                        ?.savedStateHandle
                        ?.set("products_need_refresh", true)

                    navController.popBackStack()
                }
            )
        }
        // --------------------------------------------------
// Adjust Stock
// --------------------------------------------------

        composable(
            route = Routes.ADJUST_STOCK_ROUTE,
            arguments = listOf(
                navArgument(Routes.PRODUCT_ID_ARGUMENT) {
                    type = NavType.IntType
                }
            )
        ) { backStackEntry ->

            val productId =
                backStackEntry.arguments
                    ?.getInt(Routes.PRODUCT_ID_ARGUMENT)
                    ?: return@composable

            AdjustStockScreen(
                productId = productId,

                onBackClick = {
                    navController.popBackStack()
                },

                onStockUpdated = {

                    navController.previousBackStackEntry
                        ?.savedStateHandle
                        ?.set("products_need_refresh", true)

                    navController.popBackStack()
                }
            )
        }

        // --------------------------------------------------
        // Record Sale
        // --------------------------------------------------

        composable(
            Routes.RECORD_SALE
        ) {

            RecordSaleScreen(

                onBackClick = {
                    navController
                        .popBackStack()
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

        composable(
            Routes.SALES_HISTORY
        ) {

            SalesHistoryScreen(

                onBackClick = {
                    navController
                        .popBackStack()
                },

                onAddSaleClick = {

                    navController.navigate(
                        Routes.RECORD_SALE
                    ) {
                        launchSingleTop = true
                    }
                },

                onSaleClick = { saleId ->

                    /*
                     * Sprint 2:
                     * Edit Sale route
                     * سنضيفه لاحقًا.
                     */
                }
            )
        }
    }
}

