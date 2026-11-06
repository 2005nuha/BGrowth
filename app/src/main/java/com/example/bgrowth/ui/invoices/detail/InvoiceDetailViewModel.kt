package com.example.bgrowth.ui.invoices.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bgrowth.data.repository.InvoiceRepository
import com.example.bgrowth.ui.invoices.InvoiceStatus
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

    fun loadInvoiceDetail(invoiceId: Int) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null, invoiceNumericId = invoiceId) }

            repository.getInvoiceDetail(invoiceId)
                .onSuccess { response ->
                    val mappedItems = response.items.map { dto ->
                        InvoiceItemLine(
                            name = dto.productName,
                            qty = dto.quantity,
                            total = dto.lineTotal
                        )
                    }

                    val mappedStatus = when (response.status.uppercase()) {
                        "CANCELLED", "CANCELED" -> InvoiceStatus.CANCELED
                        else -> InvoiceStatus.PENDING
                    }

                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            invoiceId = response.invoiceNumber,
                            date = response.issuedAt ?: "",
                            status = mappedStatus,
                            billToName = response.customerName ?: "N/A",
                            billToPhone = response.customerPhone ?: "N/A",
                            totalAmount = response.total?.let { t -> "$$t" } ?: "$0.00",
                            items = mappedItems
                        )
                    }
                }
                .onFailure { error ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = error.localizedMessage ?: "Failed to load invoice detail"
                        )
                    }
                }
        }
    }

    fun onShowCancelDialog(show: Boolean) {
        _uiState.update { it.copy(showCancelDialog = show) }
    }

    fun onConfirmCancel() {
        val currentId = _uiState.value.invoiceNumericId
        if (currentId == 0) return

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, showCancelDialog = false) }
            repository.cancelInvoice(currentId)
                .onSuccess {
                    loadInvoiceDetail(currentId)
                }
                .onFailure { error ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = error.localizedMessage ?: "Failed to cancel invoice"
                        )
                    }
                }
        }
    }

    fun onMarkAsPaid() {
        // يمكنكِ إضافة دالة تحديث الحالة إذا كانت مدعومة في الباك إند
    }
}