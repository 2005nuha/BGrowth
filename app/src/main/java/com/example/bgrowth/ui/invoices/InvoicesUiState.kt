package com.example.bgrowth.ui.invoices

data class InvoiceItem(
    val id: String, // مثلاً: INV-0143
    val customerName: String,
    val date: String,
    val amount: String,
    val status: InvoiceStatus
)

enum class InvoiceStatus(val label: String) {
    PAID("Paid"),
    PENDING("Pending"),
    CANCELED("Canceled")
}

data class InvoicesUiState(
    val searchQuery: String = "",
    val invoices: List<InvoiceItem> = emptyList()
)