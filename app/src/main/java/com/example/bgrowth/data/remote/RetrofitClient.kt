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
     * مهم:
     * ضعي هنا آخر URL يعطيك إياه Backend Developer.
     *
     * من Android Emulator إذا Backend على نفس الكمبيوتر:
     * http://10.0.2.2:8000/
     */
    private const val BASE_URL =
        "http://afterwards-payments-gotten-beef.trycloudflare.com/"

    /*
     * هذا Client للطلبات العامة فقط.
     *
     * لا يرسل Authorization
     * ولا يعمل refresh تلقائي.
     */
    private val publicOkHttpClient: OkHttpClient by lazy {

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
     * هذا Interceptor يضيف Access Token
     * فقط للطلبات المحمية.
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
     * إذا Backend رجع 401:
     *
     * 1. نأخذ refresh token
     * 2. نطلب tokens جديدة
     * 3. نحفظ access + refresh الجديدين
     * 4. نعيد نفس request مرة واحدة
     */
    private val tokenAuthenticator =
        object : Authenticator {

            override fun authenticate(
                route: Route?,
                response: Response
            ): Request? {

                /*
                 * منع infinite loop.
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

                } catch (exception: Exception) {

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

    private val loggingInterceptor:
            HttpLoggingInterceptor by lazy {

        HttpLoggingInterceptor()
            .apply {

                level =
                    HttpLoggingInterceptor
                        .Level
                        .BODY

                redactHeader(
                    "Authorization"
                )
            }
    }

    /*
     * Retrofit بدون Authentication.
     */
    private val publicRetrofit:
            Retrofit by lazy {

        Retrofit.Builder()
            .baseUrl(
                BASE_URL
            )
            .client(
                publicOkHttpClient
            )
            .addConverterFactory(
                GsonConverterFactory.create()
            )
            .build()
    }

    /*
     * Retrofit للطلبات التي تحتاج Bearer token.
     */
    private val authenticatedRetrofit:
            Retrofit by lazy {

        Retrofit.Builder()
            .baseUrl(
                BASE_URL
            )
            .client(
                authenticatedOkHttpClient
            )
            .addConverterFactory(
                GsonConverterFactory.create()
            )
            .build()
    }

    /*
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
     * /me/
     * logout
     * وأي Auth endpoint محمي.
     */
    val authApi:
            AuthApi by lazy {

        authenticatedRetrofit.create(
            AuthApi::class.java
        )
    }

    /*
     * Products تحتاج Authentication.
     */
    val productApi:
            ProductApi by lazy {

        authenticatedRetrofit.create(
            ProductApi::class.java
        )
    }

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
                previousResponse
                    .priorResponse
        }

        return result
    }
}