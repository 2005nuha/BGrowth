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
    const val DASHBOARD = "dashboard"
    const val FORGOT_PASSWORD = "forgot_password"
    const val RESET_PASSWORD = "reset_password"
    const val ADD_PRODUCT = "add_product"
    const val RECORD_SALE = "record_sale"
    const val PRODUCTS = "products"
    const val SALES_HISTORY = "sales_history"

    // --------------------------------------------------
    // Sprint 4 Routes
    // --------------------------------------------------
    const val DEBTS = "debts"
    const val ADD_DEBT = "add_debt"
    const val DEBT_DETAIL_ARGUMENT = "debtId"
    const val DEBT_DETAIL = "debt_detail/{$DEBT_DETAIL_ARGUMENT}"

    const val INVOICES = "invoices"
    const val INVOICE_DETAIL_ARGUMENT = "invoiceId"
    const val INVOICE_DETAIL = "invoice_detail/{$INVOICE_DETAIL_ARGUMENT}"

    const val REPORTS = "reports"

    const val VERIFICATION_MODE_ARGUMENT = "verificationMode"
    const val VERIFICATION_TARGET_ARGUMENT = "verificationTarget"
    const val VERIFICATION_ROUTE =
        "$VERIFICATION/{$VERIFICATION_MODE_ARGUMENT}/{$VERIFICATION_TARGET_ARGUMENT}"

    fun verification(
        verificationTarget: String,
        verificationMode: VerificationMode = VerificationMode.SIGN_UP
    ): String = "$VERIFICATION/${verificationMode.name}/${Uri.encode(verificationTarget)}"

    fun debtDetail(debtId: Int): String = "debt_detail/$debtId"
    fun invoiceDetail(invoiceId: String): String = "invoice_detail/$invoiceId"


}