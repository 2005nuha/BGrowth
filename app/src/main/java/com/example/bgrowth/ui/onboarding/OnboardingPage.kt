package com.example.bgrowth.ui.onboarding

import androidx.annotation.DrawableRes
import androidx.compose.runtime.Immutable
import com.example.bgrowth.R

@Immutable
data class OnboardingPage(
    val title: String,
    val description: String,
    @DrawableRes val imageRes: Int,
    val buttonText: String
)

val onboardingPages = listOf(
    OnboardingPage(
        title = "Run Your Business Smarter",
        description = "All the tools you need to manage your business in one simple place.",
        imageRes = R.drawable.onboarding_1,
        buttonText = "Next"
    ),
    OnboardingPage(
        title = "Track Everything That Matters",
        description = "Sales, expenses, inventory, and profit in real-time.",
        imageRes = R.drawable.onboarding_2,
        buttonText = "Next"
    ),
    OnboardingPage(
        title = "Grow with Clear Insights",
        description = "Make better decisions with powerful reports and analytics.",
        imageRes = R.drawable.onboarding_3,
        buttonText = "Get Started"
    )
)
