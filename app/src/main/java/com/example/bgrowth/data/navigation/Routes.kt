package com.example.bgrowth.data.navigation

object Routes {

    const val SPLASH =
        "splash"

    const val ONBOARDING =
        "onboarding"

    const val REGISTER =
        "register"

    const val LOGIN =
        "login"

    const val BUSINESS_SETUP =
        "business_setup"

    const val DASHBOARD =
        "dashboard"

    const val FORGOT_PASSWORD =
        "forgot_password"

    const val RESET_PASSWORD =
        "reset_password"

    // -------------------------
    // Products
    // -------------------------

    const val ADD_PRODUCT =
        "add_product"


    const val PRODUCTS =
        "products"

    const val EDIT_PRODUCT =
        "edit_product"

    const val PRODUCT_ID_ARGUMENT =
        "productId"

    const val EDIT_PRODUCT_ROUTE =
        "$EDIT_PRODUCT/{$PRODUCT_ID_ARGUMENT}"

    fun editProduct(
        productId: Int
    ): String {

        return "$EDIT_PRODUCT/$productId"
    }
    const val ADJUST_STOCK =
        "adjust_stock"

    const val ADJUST_STOCK_ROUTE =
        "$ADJUST_STOCK/{$PRODUCT_ID_ARGUMENT}"

    fun adjustStock(
        productId: Int
    ): String {
        return "$ADJUST_STOCK/$productId"
    }

    // -------------------------
    // Sales
    // -------------------------

    const val RECORD_SALE =
        "record_sale"

    const val SALES_HISTORY =
        "sales_history"
}