package com.example.bgrowth.ui.splash

enum class SplashDestination {
    ONBOARDING,
    BUSINESS_SETUP,
    DASHBOARD
}

data class SplashUiState(
    val isLoading: Boolean = true,
    val destination: SplashDestination? = null
)