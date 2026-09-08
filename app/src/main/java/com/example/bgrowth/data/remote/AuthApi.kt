package com.example.bgrowth.data.remote

import com.example.bgrowth.data.model.LoginRequest
import com.example.bgrowth.data.model.LoginResponse
import com.example.bgrowth.data.model.MessageResponse
import com.example.bgrowth.data.model.PasswordResetConfirmRequest
import com.example.bgrowth.data.model.PasswordResetRequest
import com.example.bgrowth.data.model.RegisterRequest
import com.example.bgrowth.data.model.RegisterResponse
import com.example.bgrowth.data.model.TokenPair
import com.example.bgrowth.data.model.User
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface AuthApi {

    @POST("api/auth/login/")
    suspend fun login(@Body request: LoginRequest): LoginResponse

    @POST("api/auth/register/")
    suspend fun register(@Body request: RegisterRequest): RegisterResponse

    @POST("api/auth/logout/")
    suspend fun logout(@Body tokens: TokenPair): MessageResponse

    @POST("api/auth/token/refresh/")
    suspend fun refreshToken(@Body tokens: TokenPair): TokenPair

    @GET("api/auth/me/")
    suspend fun me(): User

    @POST("api/auth/password-reset/request/")
    suspend fun passwordResetRequest(@Body request: PasswordResetRequest): MessageResponse

    @POST("api/auth/password-reset/confirm/")
    suspend fun passwordResetConfirm(@Body request: PasswordResetConfirmRequest): MessageResponse
}
