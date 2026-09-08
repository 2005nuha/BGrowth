package com.example.bgrowth.data.model

import com.google.gson.annotations.SerializedName

data class PasswordResetConfirmRequest(
    val token: String,
    @SerializedName("new_password") val newPassword: String,
    @SerializedName("new_password_confirm") val newPasswordConfirm: String
)
