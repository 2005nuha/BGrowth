package com.example.bgrowth.data.remote

import com.example.bgrowth.data.model.Category
import com.example.bgrowth.data.model.CreateCategoryRequest
import com.example.bgrowth.data.model.CreateProductRequest
import com.example.bgrowth.data.model.Product
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ProductApi {

    // -----------------------------
    // Products
    // -----------------------------

    @GET("api/business/products/")
    suspend fun getProducts(): Response<List<Product>>

    @POST("api/business/products/")
    suspend fun createProduct(
        @Body request: CreateProductRequest
    ): Response<Product>


    // -----------------------------
    // Categories
    // -----------------------------

    @GET("api/business/categories/")
    suspend fun getCategories(): Response<List<Category>>

    @POST("api/business/categories/")
    suspend fun createCategory(
        @Body request: CreateCategoryRequest
    ): Response<Category>
}