package com.example.bgrowth.ui.invoices.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bgrowth.data.repository.InvoiceRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class InvoiceDetailViewModel(
    private val repository: InvoiceRepository = InvoiceRepository()
) : ViewModel() {

    private val _uiState = MutableStateFlow(InvoiceDetailUiState())
    val uiState: StateFlow<InvoiceDetailUiState> = _uiState.asStateFlow()

    private var currentInvoiceNumericId: Int = 0

    fun loadInvoiceDetail(invoiceId: Int) {
        currentInvoiceNumericId = invoiceId
        viewModelScope.launch {
            val result = repository.getInvoiceById(invoiceId)
            if (result.isSuccess) {
                val invoice = result.getOrNull()
                if (invoice != null) {
                    val status = when (invoice.status.uppercase()) {
                        "CANCELLED" -> InvoiceStatus.CANCELED
                        "PAID" -> InvoiceStatus.PAID
                        else -> InvoiceStatus.PENDING
                    }

                    val itemsLines = invoice.items.map { item ->
                        InvoiceItemLine(
                            name = item.product_name,
                            qty = item.quantity,
                            total = "$${item.line_total}"
                        )
                    }

                    _uiState.update { state ->
                        state.copy(
                            invoiceId = invoice.invoice_number,
                            date = invoice.issued_at.take(10),
                            status = status,
                            billToName = invoice.customer_name.ifEmpty { "N/A" },
                            billToPhone = invoice.customer_phone.ifEmpty { "N/A" },
                            items = itemsLines,
                            totalAmount = "$${invoice.total}",
                            cancelDate = if (status == InvoiceStatus.CANCELED) invoice.issued_at.take(10) else null
                        )
                    }
                }
            }
        }
    }

    fun onShowCancelDialog(show: Boolean) {
        _uiState.update { it.copy(showCancelDialog = show) }
    }

    fun onConfirmCancel() {
        if (currentInvoiceNumericId == 0) return

        viewModelScope.launch {
            val result = repository.cancelInvoice(currentInvoiceNumericId)
            if (result.isSuccess) {
                val invoice = result.getOrNull()
                _uiState.update {
                    it.copy(
                        status = InvoiceStatus.CANCELED,
                        showCancelDialog = false,
                        cancelDate = invoice?.issued_at?.take(10) ?: "Today"
                    )
                }
            } else {
                _uiState.update { it.copy(showCancelDialog = false) }
            }
        }
    }

    fun onMarkAsPaid() {
        _uiState.update { it.copy(status = InvoiceStatus.PAID) }
    }
}