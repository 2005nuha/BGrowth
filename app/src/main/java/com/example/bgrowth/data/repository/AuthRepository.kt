package com.example.bgrowth.data.repository

import com.example.bgrowth.BGrowthApp
import com.example.bgrowth.data.model.LoginRequest
import com.example.bgrowth.data.model.LoginResponse
import com.example.bgrowth.data.model.MessageResponse
import com.example.bgrowth.data.model.PasswordResetConfirmRequest
import com.example.bgrowth.data.model.PasswordResetRequest
import com.example.bgrowth.data.model.RegisterRequest
import com.example.bgrowth.data.model.RegisterResponse
import com.example.bgrowth.data.model.TokenPair
import com.example.bgrowth.data.remote.AuthApi
import com.example.bgrowth.data.remote.RetrofitClient
import com.example.bgrowth.data.session.SessionManager

class AuthRepository(
    private val authApi: AuthApi = RetrofitClient.authApi,
    private val sessionManager: SessionManager = BGrowthApp.instance.sessionManager
) {

    suspend fun login(email: String, password: String): LoginResponse {
        val response = authApi.login(LoginRequest(email = email, password = password))
        sessionManager.saveTokens(response.tokens.access, response.tokens.refresh)
        return response
    }

    suspend fun register(
        firstName: String,
        email: String,
        password: String,
        passwordConfirm: String
    ): RegisterResponse {
        val response = authApi.register(
            RegisterRequest(
                email = email,
                firstName = firstName,
                password = password,
                passwordConfirm = passwordConfirm
            )
        )
        sessionManager.saveTokens(response.tokens.access, response.tokens.refresh)
        return response
    }

    suspend fun logout() {
        val access = sessionManager.getToken() ?: return
        val refresh = sessionManager.getRefreshToken() ?: return
        try {
            authApi.logout(TokenPair(access = access, refresh = refresh))
        } finally {
            sessionManager.clearAll()
        }
    }

    suspend fun passwordResetRequest(email: String): MessageResponse =
        authApi.passwordResetRequest(PasswordResetRequest(email = email))

    suspend fun passwordResetConfirm(
        token: String,
        newPassword: String,
        newPasswordConfirm: String
    ): MessageResponse = authApi.passwordResetConfirm(
        PasswordResetConfirmRequest(
            token = token,
            newPassword = newPassword,
            newPasswordConfirm = newPasswordConfirm
        )
    )

    fun clearSession() {
        sessionManager.clearAll()
    }
}
