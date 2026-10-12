package com.example.bgrowth.data.model

data class InvoiceItemResponse(
    val id: Int,
    val product_name: String,
    val quantity: Int,
    val unit_price: String,
    val line_total: String
)

data class InvoiceResponse(
    val id: Int,
    val business: Int,
    val sale: Int,
    val invoice_number: String,
    val status: String, // "ISSUED" or "CANCELLED"
    val customer_name: String,
    val customer_phone: String,
    val total: String,
    val notes: String,
    val issued_at: String,
    val items: List<InvoiceItemResponse> = emptyList()
)