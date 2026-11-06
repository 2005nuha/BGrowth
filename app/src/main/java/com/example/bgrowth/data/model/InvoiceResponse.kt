package com.example.bgrowth.data.model

import com.google.gson.annotations.SerializedName

// الفاتورة الأساسية
data class InvoiceResponse(
    @SerializedName("id") val id: Int,
    @SerializedName("invoice_number") val invoiceNumber: String,
    @SerializedName("status") val status: String,
    @SerializedName("issued_at") val issuedAt: String?,
    @SerializedName("customer_name") val customerName: String?,
    @SerializedName("customer_phone") val customerPhone: String?,
    @SerializedName("total") val total: String?,
    @SerializedName("notes") val notes: String?,
    @SerializedName("items") val items: List<InvoiceItemDto> = emptyList()
)

// عناصر الفاتورة (InvoiceItem)
data class InvoiceItemDto(
    @SerializedName("id") val id: Int = 0,
    @SerializedName("product_name") val productName: String,
    @SerializedName("quantity") val quantity: Int,
    @SerializedName("unit_price") val unitPrice: String,
    @SerializedName("line_total") val lineTotal: String
)

// طلب إنشاء فاتورة جديدة
data class CreateInvoiceRequest(
    @SerializedName("sale_id") val saleId: Int,
    @SerializedName("customer_name") val customerName: String? = null,
    @SerializedName("customer_phone") val customerPhone: String? = null,
    @SerializedName("notes") val notes: String? = null
)