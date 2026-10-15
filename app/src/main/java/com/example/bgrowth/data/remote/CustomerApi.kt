package com.example.bgrowth.data.remote

import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

data class CustomerDto(
    val id: Int,
    val name: String,
    val phone: String,
    val address: String,
    val created_at: String
)

data class CreateCustomerRequest(
    val name: String,
    val phone: String = "",
    val address: String = ""
)

interface CustomerApi {
    @GET("api/business/customers/")
    suspend fun getCustomers(@Query("search") query: String? = null): List<CustomerDto>

    @POST("api/business/customers/")
    suspend fun createCustomer(@Body request: CreateCustomerRequest): CustomerDto

    @GET("api/business/customers/{id}/")
    suspend fun getCustomerDetail(@Path("id") id: Int): CustomerDto
}