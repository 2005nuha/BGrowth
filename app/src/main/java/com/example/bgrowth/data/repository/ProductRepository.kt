package com.example.bgrowth.data.repository

import com.example.bgrowth.data.model.Category
import com.example.bgrowth.data.model.CreateCategoryRequest
import com.example.bgrowth.data.model.CreateProductRequest
import com.example.bgrowth.data.model.Product
import com.example.bgrowth.data.remote.ProductApi
import com.example.bgrowth.data.remote.RetrofitClient

class ProductRepository(
    private val api: ProductApi = RetrofitClient.productApi
) {

    suspend fun getProducts(): Result<List<Product>> {
        return try {
            val response = api.getProducts()

            if (response.isSuccessful) {
                Result.success(response.body().orEmpty())
            } else {
                Result.failure(
                    Exception(
                        "Failed to load products. Code: ${response.code()}"
                    )
                )
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun createProduct(
        request: CreateProductRequest
    ): Result<Product> {
        return try {
            val response = api.createProduct(request)

            if (response.isSuccessful) {
                val product = response.body()

                if (product != null) {
                    Result.success(product)
                } else {
                    Result.failure(
                        Exception("Empty product response.")
                    )
                }
            } else {
                Result.failure(
                    Exception(
                        "Failed to create product. Code: ${response.code()}"
                    )
                )
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getCategories(): Result<List<Category>> {
        return try {
            val response = api.getCategories()

            if (response.isSuccessful) {
                Result.success(response.body().orEmpty())
            } else {
                Result.failure(
                    Exception(
                        "Failed to load categories. Code: ${response.code()}"
                    )
                )
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun createCategory(
        request: CreateCategoryRequest
    ): Result<Category> {
        return try {
            val response = api.createCategory(request)

            if (response.isSuccessful) {
                val category = response.body()

                if (category != null) {
                    Result.success(category)
                } else {
                    Result.failure(
                        Exception("Empty category response.")
                    )
                }
            } else {
                Result.failure(
                    Exception(
                        "Failed to create category. Code: ${response.code()}"
                    )
                )
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}