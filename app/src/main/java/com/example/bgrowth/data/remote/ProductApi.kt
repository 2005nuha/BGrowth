package com.example.bgrowth.data.remote

import com.example.bgrowth.data.model.AdjustStockRequest
import com.example.bgrowth.data.model.Category
import com.example.bgrowth.data.model.CreateCategoryRequest
import com.example.bgrowth.data.model.CreateProductRequest
import com.example.bgrowth.data.model.Product
import com.example.bgrowth.data.model.UpdateCategoryRequest
import com.example.bgrowth.data.model.UpdateProductRequest
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface ProductApi {

    // ==================================================
    // Products
    // ==================================================

    @GET("api/business/products/")
    suspend fun getProducts(
        @Query("search")
        search: String? = null,

        @Query("ordering")
        ordering: String? = null
    ): List<Product>

    @GET("api/business/products/{id}/")
    suspend fun getProduct(
        @Path("id")
        productId: Int
    ): Product

    @POST("api/business/products/")
    suspend fun createProduct(
        @Body
        request: CreateProductRequest
    ): Product

    @PATCH("api/business/products/{id}/")
    suspend fun updateProduct(
        @Path("id")
        productId: Int,

        @Body
        request: UpdateProductRequest
    ): Product

    @DELETE("api/business/products/{id}/")
    suspend fun deleteProduct(
        @Path("id")
        productId: Int
    )

    // ==================================================
    // Inventory
    // ==================================================

    @GET("api/business/products/low-stock/")
    suspend fun getLowStockProducts():
            List<Product>

    @GET("api/business/products/out-of-stock/")
    suspend fun getOutOfStockProducts():
            List<Product>

    @POST("api/business/products/{id}/adjust-stock/")
    suspend fun adjustStock(
        @Path("id")
        productId: Int,

        @Body
        request: AdjustStockRequest
    ): Product

    // ==================================================
    // Categories
    // ==================================================

    @GET("api/business/categories/")
    suspend fun getCategories():
            List<Category>

    @GET("api/business/categories/{id}/")
    suspend fun getCategory(
        @Path("id")
        categoryId: Int
    ): Category

    @POST("api/business/categories/")
    suspend fun createCategory(
        @Body
        request: CreateCategoryRequest
    ): Category

    @PATCH("api/business/categories/{id}/")
    suspend fun updateCategory(
        @Path("id")
        categoryId: Int,

        @Body
        request: UpdateCategoryRequest
    ): Category

    @DELETE("api/business/categories/{id}/")
    suspend fun deleteCategory(
        @Path("id")
        categoryId: Int
    )
}