package com.example.bgrowth.data.model

// VERIFY BEFORE TESTING: confirm these field names match your Laravel controller.
// Common alternatives: "phone" vs "email" vs "identifier"; "code" vs "otp" vs "otp_code".
// Add @SerializedName("your_field") above each property if they differ.
data class VerifyOtpRequest(
    val phone: String,
    val code: String
)
