package com.example.bgrowth.ui.dashboard

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun DashboardRoute(
    onProfileClick: () -> Unit,
    onNotificationsClick: () -> Unit,
    onBusinessSettingsClick: () -> Unit,
    onLanguageClick: () -> Unit,
    onDarkModeClick: () -> Unit,
    onPrivacySecurityClick: () -> Unit,
    onLogoutClick: () -> Unit,

    onRecordSaleClick: () -> Unit,
    onAddProductClick: () -> Unit,
    onProductsClick: () -> Unit,
    onSalesClick: () -> Unit,

    modifier: Modifier = Modifier,
    viewModel: DashboardViewModel = viewModel()
) {

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    DashboardDrawer(
        userName = uiState.userName,
        secondaryAccountText =
            uiState.businessName.takeIf {
                it.isNotBlank()
            },

        onProfileClick = onProfileClick,
        onNotificationsClick = onNotificationsClick,
        onBusinessSettingsClick = onBusinessSettingsClick,
        onLanguageClick = onLanguageClick,
        onDarkModeClick = onDarkModeClick,
        onPrivacySecurityClick = onPrivacySecurityClick,
        onLogoutClick = onLogoutClick,
        modifier = modifier
    ) { onMenuClick ->

        DashboardScreen(
            uiState = uiState,

            onMenuClick = onMenuClick,

            onAddProductClick = onAddProductClick,

            onRecordSaleClick = onRecordSaleClick,

            onSalesClick = onSalesClick,

            onProductsClick = onProductsClick,

            onAddExpenseClick = {},

            onViewReportsClick = {},

            onLowStockClick = {},

            onMainAddClick = {}
        )
    }
}