package com.example.bgrowth.data.model

import com.google.gson.annotations.SerializedName

data class RegisterRequest(
    val email: String,

    @SerializedName("first_name")
    val firstName: String = "",

    @SerializedName("last_name")
    val lastName: String = "",

    val password: String,

    @SerializedName("password_confirm")
    val passwordConfirm: String
)