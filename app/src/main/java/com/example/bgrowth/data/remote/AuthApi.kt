package com.example.bgrowth.data.remote

import com.example.bgrowth.data.model.LoginRequest
import com.example.bgrowth.data.model.LoginResponse
import com.example.bgrowth.data.model.RegisterRequest
import com.example.bgrowth.data.model.RegisterResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApi {

    @POST("auth/login")
    suspend fun login(
        @Body request: LoginRequest
    ): LoginResponse

    @POST("auth/register")
    suspend fun register(
        @Body request: RegisterRequest
    ): RegisterResponse
}