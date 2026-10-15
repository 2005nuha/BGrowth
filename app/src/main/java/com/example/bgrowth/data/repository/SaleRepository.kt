
package com.example.bgrowth.data.repository

import com.example.bgrowth.data.model.CreateSaleRequest
import com.example.bgrowth.data.model.Sale
import com.example.bgrowth.data.model.UpdateSaleRequest
import com.example.bgrowth.data.remote.SaleApi

class SaleRepository(
    private val api: SaleApi
) {

    suspend fun getSales(
        dateFrom: String? = null,
        dateTo: String? = null,
        ordering: String? = null
    ): List<Sale> {
        return api.getSales(
            dateFrom = dateFrom,
            dateTo = dateTo,
            ordering = ordering
        )
    }

    suspend fun getSaleById(saleId: Int): Sale {
        return api.getSaleById(saleId)
    }

    suspend fun createSale(request: CreateSaleRequest): Sale {
        return api.createSale(request)
    }

    suspend fun updateSale(
        saleId: Int,
        request: UpdateSaleRequest
    ): Sale {
        return api.updateSale(saleId, request)
    }

    suspend fun deleteSale(saleId: Int) {
        api.deleteSale(saleId)
    }
}
