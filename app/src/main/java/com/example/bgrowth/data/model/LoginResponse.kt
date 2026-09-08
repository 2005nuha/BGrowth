package com.example.bgrowth.data.model

data class LoginResponse(
    val user: User,
    val tokens: TokenPair
)
