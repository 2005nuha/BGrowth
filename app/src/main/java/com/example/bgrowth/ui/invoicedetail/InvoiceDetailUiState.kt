package com.example.bgrowth.ui.invoicedetail

data class InvoiceItemLine(
    val name: String,
    val qty: Int,
    val total: String
)

data class InvoiceDetailUiState(
    val invoiceId: String = "INV-0142",
    val date: String = "Sep 8, 2028",
    val status: InvoiceStatus = InvoiceStatus.PENDING,
    val billToName: String = "Mohammed Khalil",
    val billToPhone: String = "059 987 6543",
    val items: List<InvoiceItemLine> = listOf(
        InvoiceItemLine("Arabic coffee 250g", 2, "$90.00")
    ),
    val totalAmount: String = "$90.00",
    val showCancelDialog: Boolean = false,
    val cancelDate: String? = null
)

enum class InvoiceStatus {
    PENDING, CANCELED, PAID
}