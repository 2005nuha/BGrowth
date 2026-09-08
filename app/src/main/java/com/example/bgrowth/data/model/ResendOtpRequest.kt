package com.example.bgrowth.data.model

// VERIFY BEFORE TESTING: confirm the field name matches your Laravel controller.
// Use "email" instead of "phone" if the resend target is an email address.
data class ResendOtpRequest(
    val phone: String
)
