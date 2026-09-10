package com.example.bgrowth.data.navigation

import android.net.Uri
import com.example.bgrowth.ui.verification.VerificationMode

object Routes {
    const val SPLASH = "splash"
    const val ONBOARDING = "onboarding"
    const val REGISTER = "register"
    const val VERIFICATION = "verification"
    const val LOGIN = "login"
    const val BUSINESS_SETUP = "business_setup"
    const val FORGOT_PASSWORD = "forgot_password"
    const val RESET_PASSWORD = "reset_password"
    const val BUSINESS_SETUP = "business_setup"

    const val VERIFICATION_MODE_ARGUMENT = "verificationMode"
    const val VERIFICATION_TARGET_ARGUMENT = "verificationTarget"
    const val VERIFICATION_ROUTE =
        "$VERIFICATION/{$VERIFICATION_MODE_ARGUMENT}/{$VERIFICATION_TARGET_ARGUMENT}"

    fun verification(
        verificationTarget: String,
        verificationMode: VerificationMode = VerificationMode.SIGN_UP
    ): String = "$VERIFICATION/${verificationMode.name}/${Uri.encode(verificationTarget)}"
}
