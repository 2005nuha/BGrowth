package com.example.bgrowth.ui.debts

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bgrowth.data.repository.DebtRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class DebtsViewModel(
    private val repository: DebtRepository = DebtRepository()
) : ViewModel() {

    private val _uiState = MutableStateFlow(DebtsUiState())
    val uiState: StateFlow<DebtsUiState> = _uiState.asStateFlow()

    init {
        loadDebts()
    }

    fun onTabSelected(tab: String) {
        _uiState.update { it.copy(selectedTab = tab) }
        loadDebts()
    }

    fun loadDebts() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }

            repository.getDebts()
                .onSuccess { debtsList ->
                    val mappedItems = debtsList.map { dto ->
                        val initials = dto.party_name.trim()
                            .split(" ")
                            .take(2)
                            .mapNotNull { it.firstOrNull()?.uppercaseChar() }
                            .joinToString("")
                            .ifEmpty { "D" }

                        DebtItemUiState(
                            id = dto.id,
                            name = dto.party_name,
                            initials = initials,
                            dueDate = dto.due_date ?: "N/A",
                            amount = "$${dto.remaining_amount}",
                            status = dto.status,
                            isUnpaid = dto.status == "UNPAID"
                        )
                    }

                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            debtsList = mappedItems
                        )
                    }
                }
                .onFailure { error ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = error.message ?: "Failed to load debts"
                        )
                    }
                }
        }
    }
}