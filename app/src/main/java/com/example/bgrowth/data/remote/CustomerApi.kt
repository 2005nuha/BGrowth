package com.example.bgrowth.data.remote

import com.example.bgrowth.data.model.CustomerResponse
import com.example.bgrowth.data.model.AddCustomerRequest
import com.example.bgrowth.data.model.CustomerResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface CustomerApi {

    @GET("api/customers/")
    suspend fun getCustomers(): Response<List<CustomerResponse>>

    @GET("api/customers/{id}/")
    suspend fun getCustomerById(@Path("id") id: Int): Response<CustomerResponse>

    @POST("api/customers/")
    suspend fun addCustomer(@Body request: AddCustomerRequest): Response<CustomerResponse>
}