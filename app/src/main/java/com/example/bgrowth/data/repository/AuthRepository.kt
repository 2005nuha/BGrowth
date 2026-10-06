package com.example.bgrowth.data.repository

import com.example.bgrowth.BGrowthApp
import com.example.bgrowth.data.model.LoginRequest
import com.example.bgrowth.data.model.LoginResponse
import com.example.bgrowth.data.model.MessageResponse
import com.example.bgrowth.data.model.PasswordResetConfirmRequest
import com.example.bgrowth.data.model.PasswordResetRequest
import com.example.bgrowth.data.model.RefreshTokenRequest
import com.example.bgrowth.data.model.RegisterRequest
import com.example.bgrowth.data.model.RegisterResponse
import com.example.bgrowth.data.model.User
import com.example.bgrowth.data.remote.AuthApi
import com.example.bgrowth.data.remote.RetrofitClient
import com.example.bgrowth.data.session.SessionManager

class AuthRepository(

    private val publicAuthApi:
    AuthApi =
        RetrofitClient.publicAuthApi,

    private val authApi:
    AuthApi =
        RetrofitClient.authApi,

    private val sessionManager:
    SessionManager =
        BGrowthApp.instance.sessionManager
) {

    suspend fun login(
        email: String,
        password: String
    ): LoginResponse {

        val response =
            publicAuthApi.login(
                LoginRequest(
                    email = email.trim(),
                    password = password
                )
            )

        sessionManager.saveTokens(
            accessToken =
                response.tokens.access,

            refreshToken =
                response.tokens.refresh
        )

        return response
    }

    suspend fun register(
        firstName: String,
        lastName: String,
        email: String,
        password: String,
        passwordConfirm: String
    ): RegisterResponse {

        val response =
            publicAuthApi.register(
                RegisterRequest(
                    email =
                        email.trim(),

                    firstName =
                        firstName.trim(),

                    lastName =
                        lastName.trim(),

                    password =
                        password,

                    passwordConfirm =
                        passwordConfirm
                )
            )

        /*
         * Register نفسه يرجع tokens.
         * لذلك لا نعمل Login مرة ثانية.
         */
        sessionManager.saveTokens(
            accessToken =
                response.tokens.access,

            refreshToken =
                response.tokens.refresh
        )

        return response
    }

    suspend fun getCurrentUser():
            User {

        return authApi.me()
    }

    suspend fun logout() {

        val refreshToken =
            sessionManager
                .getRefreshToken()

        try {

            if (
                !refreshToken.isNullOrBlank()
            ) {

                authApi.logout(
                    RefreshTokenRequest(
                        refresh =
                            refreshToken
                    )
                )
            }

        } finally {

            /*
             * حتى لو فشل السيرفر،
             * نحذف Session المحلية.
             */
            sessionManager.clearSession()
        }
    }

    suspend fun passwordResetRequest(
        email: String
    ): MessageResponse {

        return publicAuthApi
            .passwordResetRequest(
                PasswordResetRequest(
                    email =
                        email.trim()
                )
            )
    }

    suspend fun passwordResetConfirm(
        token: String,
        newPassword: String,
        newPasswordConfirm: String
    ): MessageResponse {

        return publicAuthApi
            .passwordResetConfirm(
                PasswordResetConfirmRequest(
                    token =
                        token,

                    newPassword =
                        newPassword,

                    newPasswordConfirm =
                        newPasswordConfirm
                )
            )
    }

    fun hasSession():
            Boolean {

        return sessionManager
            .hasSession()
    }

    fun clearSession() {

        sessionManager
            .clearSession()
    }
}