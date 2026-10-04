package com.example.bgrowth.ui.adddebt

data class AddDebtUiState(
    val debtType: String = "Owed to Me", // القيم الممكنة: "Owed to Me" أو "I Owe"
    val partyName: String = "",
    val amount: String = "",
    val dueDate: String = "",
    val notes: String = "",
    val isDropdownExpanded: Boolean = false,
    val partiesList: List<String> = listOf("Sara Ahmed", "Mohammed Khalil", "Rana Nasser")
)