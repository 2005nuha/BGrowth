package com.example.bgrowth.ui.invoices

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bgrowth.data.model.InvoiceResponse
import com.example.bgrowth.data.repository.InvoiceRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class InvoicesViewModel(
    private val repository: InvoiceRepository = InvoiceRepository()
) : ViewModel() {

    private val _uiState = MutableStateFlow(InvoicesUiState())
    val uiState: StateFlow<InvoicesUiState> = _uiState.asStateFlow()

    private var allInvoices: List<InvoiceResponse> = emptyList()

    init {
        loadInvoices()
    }

    fun loadInvoices() {
        viewModelScope.launch {
            val result = repository.getInvoices()
            if (result.isSuccess) {
                allInvoices = result.getOrDefault(emptyList())
                applySearchAndFilter(_uiState.value.searchQuery)
            }
        }
    }

    fun onSearchQueryChange(query: String) {
        _uiState.update { it.copy(searchQuery = query) }
        applySearchAndFilter(query)
    }

    private fun applySearchAndFilter(query: String) {
        val filtered = if (query.isBlank()) {
            allInvoices
        } else {
            allInvoices.filter {
                it.customer_name.contains(query, ignoreCase = true) ||
                        it.invoice_number.contains(query, ignoreCase = true)
            }
        }

        val mappedItems = filtered.map { invoice ->
            val status = when (invoice.status.uppercase()) {
                "CANCELLED" -> InvoiceStatus.CANCELED
                "PAID" -> InvoiceStatus.PAID
                else -> InvoiceStatus.PENDING
            }

            InvoiceItem(
                id = invoice.invoice_number,
                customerName = invoice.customer_name.ifEmpty { "Customer #${invoice.id}" },
                date = invoice.issued_at.take(10), // أخذ تاريخ فقط من الـ Timestamp
                amount = "$${invoice.total}",
                status = status
            )
        }

        _uiState.update { it.copy(invoices = mappedItems) }
    }
}