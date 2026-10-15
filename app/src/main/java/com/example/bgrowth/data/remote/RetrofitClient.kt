package com.example.bgrowth.data.remote

import com.example.bgrowth.BGrowthApp
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitClient {
    private const val BASE_URL = "https://pichunter-commander-biol-gadgets.trycloudflare.com/"

    private val authInterceptor = Interceptor { chain ->
        val token = BGrowthApp.instance.sessionManager.getToken()
        val request = if (token != null) {
            chain.request().newBuilder()
                .addHeader("Authorization", "Bearer $token")
                .build()
        } else {
            chain.request()
        }
        chain.proceed(request)
    }

    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
        redactHeader("Authorization")
    }

    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(authInterceptor)
        .addInterceptor(loggingInterceptor)
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .build()

    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val authApi: AuthApi by lazy { retrofit.create(AuthApi::class.java) }
    val customerApi: CustomerApi by lazy { retrofit.create(CustomerApi::class.java) }
    val debtApi: DebtApi by lazy { retrofit.create(DebtApi::class.java) }
    val invoiceApi: InvoiceApi by lazy { retrofit.create(InvoiceApi::class.java) }
    val reportApi: ReportApi by lazy { retrofit.create(ReportApi::class.java) }

    val expenseApi: ExpenseApi by lazy { retrofit.create(ExpenseApi::class.java) }


}