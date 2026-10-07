package com.example.bgrowth.data.remote

import com.example.bgrowth.BGrowthApp
import com.example.bgrowth.data.model.RefreshTokenRequest
import kotlinx.coroutines.runBlocking
import okhttp3.Authenticator
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitClient {

    /*
     * ضع هنا رابط الـBackend الحالي.
     *
     * إذا كان Backend يعمل على نفس الكمبيوتر
     * والتطبيق يعمل على Android Emulator:
     *
     * http://10.0.2.2:8000/
     */
    private const val BASE_URL =
        "http://afterwards-payments-gotten-beef.trycloudflare.com/"

    /*
     * Logging لجميع HTTP requests/responses.
     *
     * Authorization يتم إخفاؤه من Logcat.
     */
    private val loggingInterceptor:
            HttpLoggingInterceptor by lazy {

        HttpLoggingInterceptor().apply {

            level =
                HttpLoggingInterceptor.Level.BODY

            redactHeader("Authorization")
        }
    }

    /*
     * Client للطلبات العامة:
     *
     * Login
     * Register
     * Token Refresh
     * Password Reset
     *
     * لا يرسل Authorization header.
     */
    private val publicOkHttpClient:
            OkHttpClient by lazy {

        OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .connectTimeout(
                30,
                TimeUnit.SECONDS
            )
            .readTimeout(
                30,
                TimeUnit.SECONDS
            )
            .writeTimeout(
                30,
                TimeUnit.SECONDS
            )
            .build()
    }

    /*
     * يضيف Access Token للطلبات المحمية.
     */
    private val authInterceptor =
        Interceptor { chain ->

            val accessToken =
                BGrowthApp.instance
                    .sessionManager
                    .getAccessToken()

            val originalRequest =
                chain.request()

            if (accessToken.isNullOrBlank()) {

                return@Interceptor chain.proceed(
                    originalRequest
                )
            }

            val authenticatedRequest =
                originalRequest
                    .newBuilder()
                    .header(
                        "Authorization",
                        "Bearer $accessToken"
                    )
                    .build()

            chain.proceed(
                authenticatedRequest
            )
        }

    /*
     * إذا انتهى Access Token ورجع السيرفر 401:
     *
     * 1. نستخدم Refresh Token.
     * 2. نحصل على Access + Refresh جديدين.
     * 3. نخزن الاثنين.
     * 4. نعيد الطلب الأصلي.
     *
     * Backend يستخدم Refresh Token Rotation،
     * لذلك يجب تخزين refresh الجديد أيضًا.
     */
    private val tokenAuthenticator =
        object : Authenticator {

            override fun authenticate(
                route: Route?,
                response: Response
            ): Request? {

                /*
                 * منع infinite refresh loop.
                 */
                if (responseCount(response) >= 2) {

                    BGrowthApp.instance
                        .sessionManager
                        .clearSession()

                    return null
                }

                val sessionManager =
                    BGrowthApp.instance
                        .sessionManager

                val refreshToken =
                    sessionManager
                        .getRefreshToken()
                        ?: return null

                return try {

                    val newTokens =
                        runBlocking {

                            publicAuthApi
                                .refreshToken(
                                    RefreshTokenRequest(
                                        refresh = refreshToken
                                    )
                                )
                        }

                    sessionManager.saveTokens(
                        accessToken =
                            newTokens.access,

                        refreshToken =
                            newTokens.refresh
                    )

                    response.request
                        .newBuilder()
                        .header(
                            "Authorization",
                            "Bearer ${newTokens.access}"
                        )
                        .build()

                } catch (
                    exception: Exception
                ) {

                    sessionManager.clearSession()

                    null
                }
            }
        }

    /*
     * Client للطلبات المحمية.
     */
    private val authenticatedOkHttpClient:
            OkHttpClient by lazy {

        OkHttpClient.Builder()
            .addInterceptor(
                authInterceptor
            )
            .authenticator(
                tokenAuthenticator
            )
            .addInterceptor(
                loggingInterceptor
            )
            .connectTimeout(
                30,
                TimeUnit.SECONDS
            )
            .readTimeout(
                30,
                TimeUnit.SECONDS
            )
            .writeTimeout(
                30,
                TimeUnit.SECONDS
            )
            .build()
    }

    /*
     * Retrofit للطلبات العامة.
     */
    private val publicRetrofit:
            Retrofit by lazy {

        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(publicOkHttpClient)
            .addConverterFactory(
                GsonConverterFactory.create()
            )
            .build()
    }

    /*
     * Retrofit للطلبات المحمية.
     */
    private val authenticatedRetrofit:
            Retrofit by lazy {

        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(
                authenticatedOkHttpClient
            )
            .addConverterFactory(
                GsonConverterFactory.create()
            )
            .build()
    }

    /*
     * Public Auth endpoints:
     *
     * Login
     * Register
     * Refresh
     * Password Reset
     */
    val publicAuthApi:
            AuthApi by lazy {

        publicRetrofit.create(
            AuthApi::class.java
        )
    }

    /*
     * Protected Auth endpoints:
     *
     * /me/
     * Logout
     */
    val authApi:
            AuthApi by lazy {

        authenticatedRetrofit.create(
            AuthApi::class.java
        )
    }

    /*
     * Business endpoints.
     *
     * تحتاج Authorization.
     */
    val businessApi:
            BusinessApi by lazy {

        authenticatedRetrofit.create(
            BusinessApi::class.java
        )
    }

    /*
     * Product + Category endpoints.
     *
     * تحتاج Authorization.
     */
    val productApi:
            ProductApi by lazy {

        authenticatedRetrofit.create(
            ProductApi::class.java
        )
    }

    /*
     * يحسب عدد مرات إعادة نفس Response
     * لمنع Authenticator من الدخول في loop.
     */
    private fun responseCount(
        response: Response
    ): Int {

        var result = 1

        var previousResponse =
            response.priorResponse

        while (
            previousResponse != null
        ) {

            result++

            previousResponse =
                previousResponse.priorResponse
        }

        return result
    }
}