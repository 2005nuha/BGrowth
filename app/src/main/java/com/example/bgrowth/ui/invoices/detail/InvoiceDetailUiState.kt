package com.example.bgrowth.ui.invoices.detail

import com.example.bgrowth.ui.invoices.InvoiceStatus

data class InvoiceDetailUiState(
    val invoiceId: String = "",
    val date: String = "",
    val status: InvoiceStatus = InvoiceStatus.PENDING,
    val billToName: String = "",
    val billToPhone: String = "",
    val items: List<InvoiceItemLine> = emptyList(),
    val totalAmount: String = "",
    val cancelDate: String? = null,
    val showCancelDialog: Boolean = false
)

data class InvoiceItemLine(
    val name: String,
    val qty: Int,
    val total: String
)