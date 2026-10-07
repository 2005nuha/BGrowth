package com.example.bgrowth.data.remote

import com.example.bgrowth.data.model.Business
import com.example.bgrowth.data.model.CreateBusinessRequest
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface BusinessApi {

    @GET("api/business/")
    suspend fun getBusiness(): Business

    @POST("api/business/")
    suspend fun createBusiness(
        @Body request: CreateBusinessRequest
    ): Business
}