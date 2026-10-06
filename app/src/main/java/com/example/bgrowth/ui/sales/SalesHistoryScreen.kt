package com.example.bgrowth.ui.sales

import com.example.bgrowth.ui.components.BGrowthBottomNavigation
import com.example.bgrowth.ui.components.BottomNavItem
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.bgrowth.ui.theme.BGrothTheme
import java.util.Locale

private val SalesHistoryPrimary = Color(0xFF0F5D46)
private val SalesHistorySurface = Color.White
private val SalesHistoryBackground = Color(0xFFF7F8F4)
private val SalesHistoryBorder = Color(0xFFD9E3DA)
private val SalesHistorySecondaryText = Color(0xFF5E6B63)
private val SalesHistoryMutedText = Color(0xFF8A958E)

@Composable
fun SalesHistoryScreen(
    onBackClick: () -> Unit,
    onAddSaleClick: () -> Unit = {},
    onSaleClick: (Int) -> Unit = {},
    viewModel: SalesHistoryViewModel = viewModel()
) {

    val uiState by viewModel.uiState.collectAsState()

    SalesHistoryContent(
        uiState = uiState,
        onBackClick = onBackClick,
        onAddSaleClick = onAddSaleClick,
        onSaleClick = onSaleClick,
        onSearchQueryChange = viewModel::onSearchQueryChange,
        onPeriodSelected = viewModel::onPeriodSelected
    )
}

@Composable
private fun SalesHistoryContent(
    uiState: SalesHistoryUiState,
    onBackClick: () -> Unit,
    onAddSaleClick: () -> Unit,
    onSaleClick: (Int) -> Unit,
    onSearchQueryChange: (String) -> Unit,
    onPeriodSelected: (SalesPeriodFilter) -> Unit
) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {

        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
        ) {

            SalesHistoryHeader(
                onBackClick = onBackClick
            )

            Spacer(
                modifier = Modifier.height(14.dp)
            )

            SearchSection(
                searchQuery = uiState.searchQuery,
                onSearchQueryChange = onSearchQueryChange
            )

            Spacer(
                modifier = Modifier.height(14.dp)
            )

            PeriodFilterSection(
                selectedPeriod = uiState.selectedPeriod,
                onPeriodSelected = onPeriodSelected
            )

            Spacer(
                modifier = Modifier.height(20.dp)
            )

            SalesSummaryCard(
                totalSales = uiState.totalSales,
                averageSale = uiState.averageSale
            )

            Spacer(
                modifier = Modifier.height(20.dp)
            )

            if (uiState.filteredSales.isEmpty()) {

                EmptySalesHistory()

            } else {

                uiState.groupedSales.forEach { (dateLabel, sales) ->

                    SalesDateSection(
                        dateLabel = dateLabel,
                        sales = sales,
                        onSaleClick = onSaleClick
                    )

                    Spacer(
                        modifier = Modifier.height(20.dp)
                    )
                }
            }

            Spacer(
                modifier = Modifier.height(20.dp)
            )
        }

        SalesBottomNavigation(
            onAddSaleClick = onAddSaleClick
        )
    }
}

@Composable
private fun SalesHistoryHeader(
    onBackClick: () -> Unit
) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                start = 8.dp,
                end = 16.dp,
                top = 12.dp
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {

        IconButton(
            onClick = onBackClick
        ) {
            Icon(
                imageVector =
                    Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Back",
                tint = SalesHistoryPrimary
            )
        }

        Text(
            text = "History Sales",
            color = SalesHistoryPrimary,
            fontSize = 25.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
private fun SearchSection(
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit
) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 14.dp),
        horizontalArrangement =
            Arrangement.spacedBy(8.dp),
        verticalAlignment =
            Alignment.CenterVertically
    ) {

        OutlinedTextField(
            value = searchQuery,
            onValueChange = onSearchQueryChange,
            modifier = Modifier.weight(1f),
            placeholder = {
                Text(
                    "Search by Products or amount..."
                )
            },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = null
                )
            },
            singleLine = true,
            shape = RoundedCornerShape(13.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor =
                    SalesHistoryPrimary,
                unfocusedBorderColor =
                    SalesHistoryBorder,
                focusedContainerColor =
                    SalesHistorySurface,
                unfocusedContainerColor =
                    SalesHistorySurface
            )
        )

        Surface(
            modifier = Modifier
                .size(56.dp)
                .clickable {
                    // TODO:
                    // Advanced sales filters later
                },
            shape = RoundedCornerShape(13.dp),
            color = SalesHistorySurface,
            border = BorderStroke(
                1.dp,
                SalesHistoryBorder
            )
        ) {

            Box(
                contentAlignment =
                    Alignment.Center
            ) {

                Icon(
                    imageVector =
                        Icons.Default.Settings,
                    contentDescription =
                        "Sales filters",
                    tint = SalesHistoryPrimary
                )
            }
        }
    }
}

@Composable
private fun PeriodFilterSection(
    selectedPeriod: SalesPeriodFilter,
    onPeriodSelected: (SalesPeriodFilter) -> Unit
) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 14.dp)
            .horizontalScroll(
                rememberScrollState()
            ),
        horizontalArrangement =
            Arrangement.spacedBy(8.dp)
    ) {

        SalesPeriodChip(
            text = "This week",
            selected =
                selectedPeriod ==
                        SalesPeriodFilter.THIS_WEEK,
            onClick = {
                onPeriodSelected(
                    SalesPeriodFilter.THIS_WEEK
                )
            }
        )

        SalesPeriodChip(
            text = "Today",
            selected =
                selectedPeriod ==
                        SalesPeriodFilter.TODAY,
            onClick = {
                onPeriodSelected(
                    SalesPeriodFilter.TODAY
                )
            }
        )

        SalesPeriodChip(
            text = "This month",
            selected =
                selectedPeriod ==
                        SalesPeriodFilter.THIS_MONTH,
            onClick = {
                onPeriodSelected(
                    SalesPeriodFilter.THIS_MONTH
                )
            }
        )

        Spacer(
            modifier = Modifier.width(10.dp)
        )
    }
}

@Composable
private fun SalesPeriodChip(
    text: String,
    selected: Boolean,
    onClick: () -> Unit
) {

    Surface(
        modifier = Modifier
            .clickable(
                onClick = onClick
            ),
        shape =
            RoundedCornerShape(18.dp),
        color =
            if (selected) {
                SalesHistoryPrimary
            } else {
                SalesHistorySurface
            },
        border =
            if (selected) {
                null
            } else {
                BorderStroke(
                    1.dp,
                    SalesHistoryBorder
                )
            }
    ) {

        Text(
            text = text,
            color =
                if (selected) {
                    Color.White
                } else {
                    SalesHistoryMutedText
                },
            fontSize = 15.sp,
            modifier = Modifier.padding(
                horizontal = 20.dp,
                vertical = 14.dp
            )
        )
    }
}

@Composable
private fun SalesSummaryCard(
    totalSales: Double,
    averageSale: Double
) {

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 17.dp),
        shape = RoundedCornerShape(14.dp),
        colors =
            CardDefaults.cardColors(
                containerColor =
                    SalesHistorySurface
            ),
        border = BorderStroke(
            1.dp,
            SalesHistoryBorder
        )
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp)
        ) {

            Column(
                modifier = Modifier.weight(1f),
                horizontalAlignment =
                    Alignment.CenterHorizontally
            ) {

                Text(
                    text = "Total Sales",
                    color =
                        SalesHistoryMutedText,
                    fontSize = 15.sp
                )

                Spacer(
                    modifier =
                        Modifier.height(14.dp)
                )

                Text(
                    text = formatMoney(
                        totalSales
                    ),
                    fontSize = 20.sp,
                    fontWeight =
                        FontWeight.Bold
                )
            }

            Box(
                modifier = Modifier
                    .width(1.dp)
                    .height(62.dp)
                    .background(
                        SalesHistoryMutedText
                            .copy(alpha = 0.5f)
                    )
            )

            Column(
                modifier = Modifier.weight(1f),
                horizontalAlignment =
                    Alignment.CenterHorizontally
            ) {

                Text(
                    text = "Avg.Sale",
                    color =
                        SalesHistoryMutedText,
                    fontSize = 15.sp
                )

                Spacer(
                    modifier =
                        Modifier.height(14.dp)
                )

                Text(
                    text = formatMoney(
                        averageSale
                    ),
                    fontSize = 20.sp,
                    fontWeight =
                        FontWeight.Bold
                )
            }
        }
    }
}

@Composable
private fun SalesDateSection(
    dateLabel: String,
    sales: List<SalesHistoryItem>,
    onSaleClick: (Int) -> Unit
) {

    val dailyTotal =
        sales.sumOf {
            it.total
        }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 17.dp)
    ) {

        Row(
            modifier =
                Modifier.fillMaxWidth(),
            horizontalArrangement =
                Arrangement.SpaceBetween,
            verticalAlignment =
                Alignment.CenterVertically
        ) {

            Text(
                text = dateLabel,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = formatMoney(dailyTotal),
                color = SalesHistoryPrimary,
                fontSize = 16.sp,
                fontWeight =
                    FontWeight.Medium
            )
        }

        Spacer(
            modifier = Modifier.height(14.dp)
        )

        sales.forEach { sale ->

            SalesHistoryItemCard(
                sale = sale,
                onClick = {
                    onSaleClick(sale.id)
                }
            )

            Spacer(
                modifier = Modifier.height(10.dp)
            )
        }
    }
}

@Composable
private fun SalesHistoryItemCard(
    sale: SalesHistoryItem,
    onClick: () -> Unit
) {

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(
                onClick = onClick
            ),
        shape = RoundedCornerShape(14.dp),
        colors =
            CardDefaults.cardColors(
                containerColor =
                    SalesHistorySurface
            ),
        border = BorderStroke(
            1.dp,
            SalesHistoryBorder
        )
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment =
                Alignment.CenterVertically
        ) {

            Box(
                modifier = Modifier
                    .size(50.dp)
                    .background(
                        color =
                            Color(0xFFE1E3E2),
                        shape =
                            RoundedCornerShape(
                                11.dp
                            )
                    )
            )

            Spacer(
                modifier = Modifier.width(10.dp)
            )

            Column(
                modifier = Modifier.weight(1f)
            ) {

                Text(
                    text =
                        "${sale.productName} x${sale.quantity}",
                    fontSize = 15.sp,
                    fontWeight =
                        FontWeight.Medium,
                    maxLines = 1,
                    overflow =
                        TextOverflow.Ellipsis
                )

                Spacer(
                    modifier =
                        Modifier.height(3.dp)
                )

                Text(
                    text =
                        "${sale.time} . ${sale.paymentMethod}",
                    color =
                        SalesHistoryMutedText,
                    fontSize = 11.sp,
                    maxLines = 1
                )
            }

            Text(
                text = formatMoney(
                    sale.total
                ),
                fontSize = 13.sp,
                fontWeight =
                    FontWeight.Bold
            )
        }
    }
}

@Composable
private fun EmptySalesHistory() {

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                top = 80.dp,
                start = 30.dp,
                end = 30.dp
            ),
        horizontalAlignment =
            Alignment.CenterHorizontally
    ) {

        Box(
            modifier = Modifier
                .size(66.dp)
                .background(
                    color =
                        Color(0xFFEEF3ED),
                    shape = CircleShape
                ),
            contentAlignment =
                Alignment.Center
        ) {

            Icon(
                imageVector =
                    Icons.Default.ShoppingCart,
                contentDescription = null,
                tint = SalesHistoryPrimary
            )
        }

        Spacer(
            modifier = Modifier.height(14.dp)
        )

        Text(
            text = "No sales found",
            color = SalesHistoryPrimary,
            fontSize = 17.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(
            modifier = Modifier.height(6.dp)
        )

        Text(
            text =
                "Your sales history will appear here.",
            color =
                SalesHistorySecondaryText,
            fontSize = 13.sp,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun SalesBottomNavigation(
    onAddSaleClick: () -> Unit
) {

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(78.dp),
        color = SalesHistorySurface,
        border = BorderStroke(
            1.dp,
            SalesHistoryPrimary
                .copy(alpha = 0.5f)
        )
    ) {

        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 8.dp),
            verticalAlignment =
                Alignment.CenterVertically
        ) {

            SalesBottomItem(
                label = "Dashboard",
                icon = Icons.Default.Home,
                selected = false,
                modifier =
                    Modifier.weight(1f)
            )

            SalesBottomItem(
                label = "Sales",
                icon =
                    Icons.Default.ShoppingCart,
                selected = true,
                modifier =
                    Modifier.weight(1f)
            )

            Box(
                modifier =
                    Modifier.weight(1f),
                contentAlignment =
                    Alignment.Center
            ) {

                Surface(
                    modifier = Modifier
                        .size(58.dp)
                        .clickable(
                            onClick =
                                onAddSaleClick
                        ),
                    shape = CircleShape,
                    color =
                        SalesHistoryPrimary
                ) {

                    Box(
                        contentAlignment =
                            Alignment.Center
                    ) {

                        Icon(
                            imageVector =
                                Icons.Default.Add,
                            contentDescription =
                                "Record Sale",
                            tint = Color.White,
                            modifier =
                                Modifier.size(
                                    29.dp
                                )
                        )
                    }
                }
            }

            SalesBottomItem(
                label = "Products",
                icon =
                    Icons.Default.ShoppingCart,
                selected = false,
                modifier =
                    Modifier.weight(1f)
            )

            SalesBottomItem(
                label = "Business",
                icon = Icons.Default.Home,
                selected = false,
                modifier =
                    Modifier.weight(1f)
            )
        }
    }
}

@Composable
private fun SalesBottomItem(
    label: String,
    icon: ImageVector,
    selected: Boolean,
    modifier: Modifier = Modifier
) {

    val color =
        if (selected) {
            SalesHistoryPrimary
        } else {
            SalesHistoryMutedText
        }

    Column(
        modifier = modifier,
        horizontalAlignment =
            Alignment.CenterHorizontally
    ) {

        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = color,
            modifier = Modifier.size(22.dp)
        )

        Spacer(
            modifier = Modifier.height(4.dp)
        )

        Text(
            text = label,
            color = color,
            fontSize = 9.sp,
            fontWeight =
                if (selected) {
                    FontWeight.Bold
                } else {
                    FontWeight.Medium
                }
        )
    }
}

private fun formatMoney(
    value: Double
): String {

    return String.format(
        Locale.US,
        "$%.2f",
        value
    )
}

@Preview(
    name = "Sales History - Populated",
    showBackground = true,
    widthDp = 393,
    heightDp = 852
)
@Composable
private fun SalesHistoryPopulatedPreview() {

    BGrothTheme {

        SalesHistoryContent(
            uiState =
                SalesHistoryUiState(
                    sales = listOf(

                        SalesHistoryItem(
                            id = 1,
                            productName =
                                "Arabic coffee 250g",
                            quantity = 2,
                            unitPrice = 45.0,
                            paymentMethod =
                                "Card",
                            time = "10:24 AM",
                            dateLabel =
                                "Today, Sep 7"
                        ),

                        SalesHistoryItem(
                            id = 2,
                            productName =
                                "Thermal cup",
                            quantity = 1,
                            unitPrice = 25.0,
                            paymentMethod =
                                "Cash",
                            time = "09:50 AM",
                            dateLabel =
                                "Today, Sep 7"
                        ),

                        SalesHistoryItem(
                            id = 3,
                            productName =
                                "Potato Chips",
                            quantity = 5,
                            unitPrice = 1.99,
                            paymentMethod =
                                "Card",
                            time = "10:24 AM",
                            dateLabel =
                                "Yesterday, Sep 6"
                        )
                    )
                ),

            onBackClick = {},
            onAddSaleClick = {},
            onSaleClick = {},
            onSearchQueryChange = {},
            onPeriodSelected = {}
        )
    }
}

@Preview(
    name = "Sales History - Empty",
    showBackground = true,
    widthDp = 393,
    heightDp = 852
)
@Composable
private fun SalesHistoryEmptyPreview() {

    BGrothTheme {

        SalesHistoryContent(
            uiState =
                SalesHistoryUiState(),
            onBackClick = {},
            onAddSaleClick = {},
            onSaleClick = {},
            onSearchQueryChange = {},
            onPeriodSelected = {}
        )
    }
}