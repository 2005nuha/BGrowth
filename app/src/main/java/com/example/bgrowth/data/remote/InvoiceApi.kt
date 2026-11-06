package com.example.bgrowth.data.remote

import com.example.bgrowth.data.model.CreateInvoiceRequest
import com.example.bgrowth.data.model.InvoiceResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface InvoiceApi {

    @GET("api/business/invoices/")
    suspend fun getInvoices(
        @Query("status") status: String? = null
    ): List<InvoiceResponse>

    @GET("api/business/invoices/{id}/")
    suspend fun getInvoiceDetail(
        @Path("id") id: Int
    ): InvoiceResponse

    @POST("api/business/invoices/")
    suspend fun createInvoice(
        @Body request: CreateInvoiceRequest
    ): InvoiceResponse

    @POST("api/business/invoices/{id}/cancel/")
    suspend fun cancelInvoice(
        @Path("id") id: Int
    ): InvoiceResponse
}