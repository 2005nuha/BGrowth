package com.example.bgrowth.ui.debts

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bgrowth.data.model.DebtResponse
import com.example.bgrowth.data.repository.DebtRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.Locale

class DebtsViewModel(
    private val repository: DebtRepository = DebtRepository()
) : ViewModel() {

    private val _uiState = MutableStateFlow(DebtsUiState())
    val uiState: StateFlow<DebtsUiState> = _uiState.asStateFlow()

    private var allDebts: List<DebtResponse> = emptyList()

    init {
        loadDebts()
    }

    fun loadDebts() {
        viewModelScope.launch {
            val result = repository.getDebts()

            if (result.isSuccess) {
                allDebts = result.getOrDefault(emptyList())

                val owedToMeTotal = allDebts
                    .filter { it.debt_type == "RECEIVABLE" }
                    .sumOf { (it.remaining_amount ?: it.original_amount).toDoubleOrNull() ?: 0.0 }

                val iOweTotal = allDebts
                    .filter { it.debt_type == "PAYABLE" }
                    .sumOf { (it.remaining_amount ?: it.original_amount).toDoubleOrNull() ?: 0.0 }

                _uiState.update { state ->
                    state.copy(
                        totalOwedToMe = "$${String.format(Locale.US, "%.2f", owedToMeTotal)}",
                        totalIOwe = "$${String.format(Locale.US, "%.2f", iOweTotal)}"
                    )
                }

                filterDebtsForSelectedTab(_uiState.value.selectedTab)
            }
        }
    }

    fun onTabSelected(tab: String) {
        _uiState.update { it.copy(selectedTab = tab) }
        filterDebtsForSelectedTab(tab)
    }

    private fun filterDebtsForSelectedTab(tab: String) {
        val targetType = if (tab == "Owed to Me") "RECEIVABLE" else "PAYABLE"

        val filteredList = allDebts
            .filter { it.debt_type == targetType }
            .map { debt ->
                val partyName = debt.party_name
                val initials = partyName.trim()
                    .split(" ")
                    .take(2)
                    .mapNotNull { part -> part.firstOrNull()?.uppercaseChar() }
                    .joinToString("")
                    .ifEmpty { "D" }

                val displayAmount = debt.remaining_amount ?: debt.original_amount
                val isUnpaid = debt.status.uppercase() == "UNPAID"

                val statusText = when (debt.status.uppercase()) {
                    "UNPAID" -> "Unpaid"
                    "PARTIAL" -> "Partially Paid"
                    "PAID" -> "Paid"
                    else -> debt.status
                }

                DebtItem(
                    id = debt.id,
                    initials = initials,
                    name = partyName,
                    dueDate = debt.due_date?.let { "Due $it" } ?: "No due date",
                    amount = "$$displayAmount",
                    isUnpaid = isUnpaid,
                    statusText = statusText
                )
            }

        _uiState.update { state ->
            state.copy(
                debtsList = filteredList,
                isEmpty = filteredList.isEmpty()
            )
        }
    }
}