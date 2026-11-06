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
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }

            repository.getInvoices()
                .onSuccess { responseList ->
                    allInvoices = responseList
                    applySearchAndFilter(_uiState.value.searchQuery)
                }
                .onFailure { error ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = error.localizedMessage ?: "Failed to load invoices"
                        )
                    }
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
            allInvoices.filter { invoice ->
                (invoice.customerName?.contains(query, ignoreCase = true) == true) ||
                        (invoice.invoiceNumber.contains(query, ignoreCase = true))
            }
        }

        val mappedItems = filtered.map { invoice ->
            val status = when (invoice.status.uppercase()) {
                "CANCELLED", "CANCELED" -> InvoiceStatus.CANCELED
                else -> InvoiceStatus.PENDING
            }

            InvoiceItem(
                id = invoice.id,
                invoiceNumber = invoice.invoiceNumber,
                customerName = invoice.customerName ?: "N/A",
                date = invoice.issuedAt ?: "",
                amount = invoice.total?.let { "$$it" } ?: "$0.00",
                status = status
            )
        }

        _uiState.update {
            it.copy(
                isLoading = false,
                invoices = mappedItems
            )
        }
    }
}