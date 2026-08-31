package com.example.bgrowth.data.repository

import com.example.bgrowth.data.model.LoginRequest
import com.example.bgrowth.data.model.LoginResponse
import com.example.bgrowth.data.model.RegisterRequest
import com.example.bgrowth.data.model.RegisterResponse
import com.example.bgrowth.data.remote.AuthApi
import com.example.bgrowth.data.remote.RetrofitClient

class AuthRepository(
    private val authApi: AuthApi = RetrofitClient.authApi
) {

    suspend fun login(
        email: String,
        password: String
    ): LoginResponse {

        val request = LoginRequest(
            email = email,
            password = password
        )

        return authApi.login(request)
    }

    suspend fun register(
        name: String,
        email: String,
        phone: String,
        password: String
    ): RegisterResponse {

        val request = RegisterRequest(
            name = name,
            email = email,
            phone = phone,
            password = password
        )

        return authApi.register(request)
    }
}