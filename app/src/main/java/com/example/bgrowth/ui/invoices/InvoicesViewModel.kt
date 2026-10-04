package com.example.bgrowth.ui.invoices

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class InvoicesViewModel : ViewModel() {

    // وضعنا بيانات وهمية (Mock Data) مطابقة لتصميم Figma لتجربتها
    private val _uiState = MutableStateFlow(
        InvoicesUiState(
            invoices = listOf(
                InvoiceItem("INV-0143", "Sara Ahmed", "Sep 9", "$35.00", InvoiceStatus.PAID),
                InvoiceItem("INV-0142", "Mohammed Khalil", "Sep 8", "$90.00", InvoiceStatus.PENDING),
                InvoiceItem("INV-0141", "Rana Nasser", "Sep 3", "$60.00", InvoiceStatus.CANCELED)
            )
        )
    )
    val uiState: StateFlow<InvoicesUiState> = _uiState.asStateFlow()

    fun onSearchQueryChange(query: String) {
        _uiState.update { it.copy(searchQuery = query) }
    }
}