package com.example.bgrowth.ui.debts

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class DebtsViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(DebtsUiState())
    val uiState: StateFlow<DebtsUiState> = _uiState.asStateFlow()

    // بيانات ديون الزبائن (وهمية للتجربة)
    private val owedToMeList = listOf(
        DebtItem(1, "SA", "Sara Ahmed", "Due Sep 20", "$45.00", true, "Unpaid"),
        DebtItem(2, "MK", "Mohammed Khalil", "Due Sep 15", "$120.00", true, "Unpaid"),
        DebtItem(3, "RN", "Rana Nasser", "No due date", "$120.00", false, "Partially Paid")
    )

    // بيانات ديون الموردين (وهمية للتجربة)
    private val iOweList = listOf(
        DebtItem(4, "JS", "John Supplier", "Due Oct 1", "$150.00", true, "Unpaid")
    )

    init {
        // تحميل الحالة الافتراضية للتبويب الأول
        _uiState.update {
            it.copy(
                debtsList = owedToMeList,
                isEmpty = owedToMeList.isEmpty()
            )
        }
    }

    // هذه الدالة تغير القائمة بناءً على التبويب الذي تم ضغطه
    fun onTabSelected(tab: String) {
        val newList = if (tab == "Owed to Me") owedToMeList else iOweList
        _uiState.update {
            it.copy(
                selectedTab = tab,
                debtsList = newList,
                isEmpty = newList.isEmpty()
            )
        }
    }
}