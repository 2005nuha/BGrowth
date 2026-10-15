
package com.example.bgrowth.data.remote

import com.example.bgrowth.data.model.CreateSaleRequest
import com.example.bgrowth.data.model.Sale
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query
import com.example.bgrowth.data.model.UpdateSaleRequest

interface SaleApi {

    // جلب جميع المبيعات
    @GET("api/business/sales/")
    suspend fun getSales(
        @Query("date_from") dateFrom: String? = null,
        @Query("date_to") dateTo: String? = null,
        @Query("ordering") ordering: String? = null
    ): List<Sale>

    // جلب عملية بيع محددة
    @GET("api/business/sales/{id}/")
    suspend fun getSaleById(
        @Path("id") saleId: Int
    ): Sale

    // تسجيل عملية بيع جديدة
    @POST("api/business/sales/")
    suspend fun createSale(
        @Body request: CreateSaleRequest
    ): Sale

    // تعديل عملية بيع
    @PATCH("api/business/sales/{id}/")
    suspend fun updateSale(
        @Path("id") saleId: Int,
        @Body request: UpdateSaleRequest
    ): Sale

    // حذف عملية بيع
    @DELETE("api/business/sales/{id}/")
    suspend fun deleteSale(
        @Path("id") saleId: Int
    )
}
