package com.example.bgrowth.ui.sales

data class SalesHistoryItem(
    val id: Int,
    val productName: String,
    val quantity: Int,
    val unitPrice: Double,
    val paymentMethod: String,
    val time: String,
    val dateLabel: String
) {
    val total: Double
        get() = quantity * unitPrice
}

enum class SalesPeriodFilter {
    THIS_WEEK,
    TODAY,
    THIS_MONTH
}

data class SalesHistoryUiState(
    val sales: List<SalesHistoryItem> = emptyList(),

    val searchQuery: String = "",

    val selectedPeriod: SalesPeriodFilter =
        SalesPeriodFilter.THIS_WEEK,

    val isLoading: Boolean = false,

    val errorMessage: String? = null
) {

    val filteredSales: List<SalesHistoryItem>
        get() {

            if (searchQuery.isBlank()) {
                return sales
            }

            val query =
                searchQuery.trim()

            return sales.filter { sale ->

                sale.productName.contains(
                    query,
                    ignoreCase = true
                ) ||
                        sale.paymentMethod.contains(
                            query,
                            ignoreCase = true
                        ) ||
                        sale.total.toString().contains(query)
            }
        }

    val totalSales: Double
        get() =
            filteredSales.sumOf {
                it.total
            }

    val averageSale: Double
        get() =
            if (filteredSales.isEmpty()) {
                0.0
            } else {
                totalSales /
                        filteredSales.size
            }

    val groupedSales:
            Map<String, List<SalesHistoryItem>>
        get() =
            filteredSales.groupBy {
                it.dateLabel
            }
}