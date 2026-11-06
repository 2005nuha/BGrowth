package com.example.bgrowth.ui.debts.add

data class AddDebtUiState(
    val debtType: String = "Owed to Me", // "Owed to Me" أو "I Owe"
    val partyName: String = "",
    val amount: String = "",
    val dueDate: String = "",
    val notes: String = "",
    val isDropdownExpanded: Boolean = false,
    val partiesList: List<String> = listOf("Sara Ahmed", "Mohammed Khalil", "Rana Nasser"),
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)