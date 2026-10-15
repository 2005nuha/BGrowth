package com.example.bgrowth.data.remote

import com.example.bgrowth.data.model.InvoiceResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface InvoiceApi {

    @GET("api/business/invoices/")
    suspend fun getInvoices(): Response<List<InvoiceResponse>>

    @GET("api/business/invoices/{id}/")
    suspend fun getInvoiceById(@Path("id") id: Int): Response<InvoiceResponse>

    @POST("api/business/invoices/{id}/cancel/")
    suspend fun cancelInvoice(@Path("id") id: Int): Response<InvoiceResponse>
}