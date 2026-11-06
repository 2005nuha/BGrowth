package com.example.bgrowth.data.repository

import com.example.bgrowth.data.model.CreateInvoiceRequest
import com.example.bgrowth.data.model.InvoiceResponse
import com.example.bgrowth.data.remote.InvoiceApi
import com.example.bgrowth.data.remote.RetrofitClient

class InvoiceRepository(
    private val api: InvoiceApi = RetrofitClient.invoiceApi
) {
    suspend fun getInvoices(status: String? = null): Result<List<InvoiceResponse>> {
        return runCatching {
            api.getInvoices(status)
        }
    }

    suspend fun getInvoiceDetail(id: Int): Result<InvoiceResponse> {
        return runCatching {
            api.getInvoiceDetail(id)
        }
    }

    suspend fun createInvoice(
        saleId: Int,
        customerName: String? = null,
        customerPhone: String? = null,
        notes: String? = null
    ): Result<InvoiceResponse> {
        return runCatching {
            api.createInvoice(
                CreateInvoiceRequest(
                    saleId = saleId,
                    customerName = customerName,
                    customerPhone = customerPhone,
                    notes = notes
                )
            )
        }
    }

    suspend fun cancelInvoice(id: Int): Result<InvoiceResponse> {
        return runCatching {
            api.cancelInvoice(id)
        }
    }
}