package com.example.bgrowth.data.model

data class RegisterResponse(
    val user: User,
    val tokens: TokenPair
)
