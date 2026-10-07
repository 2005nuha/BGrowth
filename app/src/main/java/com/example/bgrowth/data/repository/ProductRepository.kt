package com.example.bgrowth.data.repository

import com.example.bgrowth.data.model.AdjustStockRequest
import com.example.bgrowth.data.model.Category
import com.example.bgrowth.data.model.CreateCategoryRequest
import com.example.bgrowth.data.model.CreateProductRequest
import com.example.bgrowth.data.model.Product
import com.example.bgrowth.data.model.UpdateCategoryRequest
import com.example.bgrowth.data.model.UpdateProductRequest
import com.example.bgrowth.data.remote.ProductApi
import com.example.bgrowth.data.remote.RetrofitClient

class ProductRepository(
    private val api: ProductApi =
        RetrofitClient.productApi
) {

    // ==================================================
    // Products
    // ==================================================

    suspend fun getProducts(
        search: String? = null,
        ordering: String? = null
    ): List<Product> {

        return api.getProducts(
            search = search,
            ordering = ordering
        )
    }

    suspend fun getProduct(
        productId: Int
    ): Product {

        return api.getProduct(
            productId = productId
        )
    }

    suspend fun createProduct(
        request: CreateProductRequest
    ): Product {

        return api.createProduct(
            request = request
        )
    }

    suspend fun updateProduct(
        productId: Int,
        request: UpdateProductRequest
    ): Product {

        return api.updateProduct(
            productId = productId,
            request = request
        )
    }

    suspend fun deleteProduct(
        productId: Int
    ) {

        api.deleteProduct(
            productId = productId
        )
    }

    // ==================================================
    // Inventory
    // ==================================================

    suspend fun getLowStockProducts():
            List<Product> {

        return api.getLowStockProducts()
    }

    suspend fun getOutOfStockProducts():
            List<Product> {

        return api.getOutOfStockProducts()
    }

    suspend fun adjustStock(
        productId: Int,
        quantity: Int,
        movementType: String,
        reason: String? = null
    ): Product {

        val request =
            AdjustStockRequest(
                quantity = quantity,
                movementType = movementType,
                reason = reason
                    ?.trim()
                    ?.takeIf {
                        it.isNotEmpty()
                    }
            )

        return api.adjustStock(
            productId = productId,
            request = request
        )
    }

    // ==================================================
    // Categories
    // ==================================================

    suspend fun getCategories():
            List<Category> {

        return api.getCategories()
    }

    suspend fun getCategory(
        categoryId: Int
    ): Category {

        return api.getCategory(
            categoryId = categoryId
        )
    }

    suspend fun createCategory(
        name: String,
        description: String = ""
    ): Category {

        val request =
            CreateCategoryRequest(
                name = name.trim(),
                description =
                    description.trim()
            )

        return api.createCategory(
            request = request
        )
    }

    suspend fun updateCategory(
        categoryId: Int,
        request: UpdateCategoryRequest
    ): Category {

        return api.updateCategory(
            categoryId = categoryId,
            request = request
        )
    }

    suspend fun deleteCategory(
        categoryId: Int
    ) {

        api.deleteCategory(
            categoryId = categoryId
        )
    }
}