package com.example.bgrowth.ui.profitloss

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlin.math.abs

val DarkGreen = Color(0xFF105942)
val BackgroundColor = Color(0xFFF9F9F9)
val BorderLight = Color(0xFFE5E5E5)
val ChartSalesColor = Color(0xFF75A490)
val ChartExpensesColor = Color(0xFFECA3A3)
val RedText = Color(0xFFD32F2F)

@Composable
fun ProfitLossScreen(
    viewModel: ProfitLossViewModel = viewModel(),
    onNavigateBack: () -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsState()

    ProfitLossContent(
        uiState = uiState,
        onPeriodSelected = viewModel::onPeriodSelected,
        onDismissDatePicker = viewModel::onDismissDatePicker,
        onApplyDateRange = viewModel::onApplyDateRange,
        onNavigateBack = onNavigateBack
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfitLossContent(
    uiState: ProfitLossUiState,
    onPeriodSelected: (String) -> Unit,
    onDismissDatePicker: () -> Unit,
    onApplyDateRange: () -> Unit,
    onNavigateBack: () -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    Scaffold(
        containerColor = BackgroundColor,
        topBar = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 24.dp)
            ) {
                IconButton(onClick = onNavigateBack, modifier = Modifier.size(24.dp)) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = DarkGreen)
                }
                Spacer(modifier = Modifier.width(16.dp))
                Text("Profit & loss", color = DarkGreen, fontWeight = FontWeight.Bold, fontSize = 20.sp)
            }
        },
        floatingActionButton = {
            if (uiState.isEmpty) {
                FloatingActionButton(
                    onClick = { },
                    containerColor = DarkGreen,
                    shape = CircleShape
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Add", tint = Color.White)
                }
            }
        }
    ) { paddingValues ->
        Column(modifier = Modifier.fillMaxSize().padding(paddingValues)) {
            SegmentedControl(
                options = uiState.periods,
                selectedOption = uiState.selectedPeriod,
                onOptionSelected = onPeriodSelected
            )
            Spacer(modifier = Modifier.height(24.dp))

            if (uiState.isLoading) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = DarkGreen)
                }
            } else if (uiState.isEmpty) {
                EmptyProfitView()
            } else {
                LazyColumn(contentPadding = PaddingValues(bottom = 24.dp)) {
                    item {
                        Row(
                            modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp),
                            horizontalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            SummaryCard(
                                title = "Total Sales",
                                amount = "$${uiState.totalSales}",
                                change = uiState.salesChange,
                                isPositive = true,
                                modifier = Modifier.weight(1f)
                            )
                            SummaryCard(
                                title = "Total Expenses",
                                amount = "$${uiState.totalExpenses}",
                                change = uiState.expensesChange,
                                isPositive = false,
                                modifier = Modifier.weight(1f)
                            )
                        }
                        Spacer(modifier = Modifier.height(16.dp))

                        NetProfitOrLossSection(uiState)

                        if (uiState.chartData.isNotEmpty()) {
                            Spacer(modifier = Modifier.height(16.dp))
                            ChartCard(uiState = uiState)
                        }

                        if (uiState.topExpenses.isNotEmpty()) {
                            Spacer(modifier = Modifier.height(16.dp))
                            TopExpensesCard(expenses = uiState.topExpenses)
                        }
                    }
                }
            }
        }

        if (uiState.showCustomDatePicker) {
            ModalBottomSheet(
                onDismissRequest = onDismissDatePicker,
                sheetState = sheetState,
                containerColor = Color.White
            ) {
                MockDateRangePickerSheet(onApply = onApplyDateRange)
            }
        }
    }
}

@Composable
fun SegmentedControl(options: List<String>, selectedOption: String, onOptionSelected: (String) -> Unit) {
    Surface(
        shape = RoundedCornerShape(24.dp),
        color = Color(0xFFEEEEEE),
        modifier = Modifier.fillMaxWidth().height(50.dp).padding(horizontal = 20.dp)
    ) {
        Row(modifier = Modifier.fillMaxSize().padding(4.dp), verticalAlignment = Alignment.CenterVertically) {
            options.forEach { option ->
                val isSelected = option == selectedOption
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .clip(RoundedCornerShape(20.dp))
                        .background(if (isSelected) DarkGreen else Color.Transparent)
                        .clickable { onOptionSelected(option) },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = option,
                        color = if (isSelected) Color.White else Color.Black,
                        fontWeight = if (isSelected) FontWeight.Medium else FontWeight.Normal,
                        fontSize = 14.sp
                    )
                }
            }
        }
    }
}

@Composable
fun SummaryCard(title: String, amount: String, change: String, isPositive: Boolean, modifier: Modifier) {
    val leftBorderColor = if (isPositive) DarkGreen else RedText
    val changeColor = if (isPositive) DarkGreen else RedText
    val arrowText = if (isPositive) "↗" else "↘"

    Card(
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(0.dp),
        border = BorderStroke(1.dp, BorderLight),
        modifier = modifier
    ) {
        Row(modifier = Modifier.height(IntrinsicSize.Min)) {
            Box(modifier = Modifier.width(6.dp).fillMaxHeight().background(leftBorderColor))
            Column(modifier = Modifier.padding(16.dp).weight(1f)) {
                Text(title, color = Color.Black, fontSize = 13.sp, fontWeight = FontWeight.Medium)
                Spacer(modifier = Modifier.height(8.dp))
                Text(amount, color = Color.Black, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(4.dp))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                    Text("$arrowText $change", color = changeColor, fontSize = 12.sp, fontWeight = FontWeight.Medium)
                }
            }
        }
    }
}

@Composable
fun EmptyProfitView() {
    Column(
        modifier = Modifier.fillMaxSize().padding(horizontal = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(modifier = Modifier.size(80.dp).clip(CircleShape).background(Color(0xFFEAF5EF)), contentAlignment = Alignment.Center) {
            Row(verticalAlignment = Alignment.Bottom, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                Box(modifier = Modifier.width(6.dp).height(20.dp).background(DarkGreen))
                Box(modifier = Modifier.width(6.dp).height(30.dp).background(DarkGreen))
                Box(modifier = Modifier.width(6.dp).height(16.dp).background(DarkGreen))
            }
        }
        Spacer(modifier = Modifier.height(24.dp))
        Text("No profit data yet", fontWeight = FontWeight.Bold, fontSize = 20.sp, color = DarkGreen)
        Spacer(modifier = Modifier.height(12.dp))
        Text("Once you record a sale or an expense this month, your net profit will appear here automatically.", fontSize = 14.sp, color = Color.Gray, textAlign = TextAlign.Center)
    }
}

@Composable
fun NetProfitOrLossSection(uiState: ProfitLossUiState) {
    val isLoss = uiState.netProfit < 0
    val cardBg = if (isLoss) Color(0xFFFDEBEA) else Color(0xFFEAF5EF)
    val cardBorder = if (isLoss) Color(0xFFF5C6C5) else Color(0xFFC7E2D5)
    val textColor = if (isLoss) RedText else DarkGreen
    val titleText = if (isLoss) "NET LOSS __ ${uiState.netProfitLabel}" else "NET PROFIT __ ${uiState.netProfitLabel}"
    val amountText = if (isLoss) "-$${abs(uiState.netProfit)}" else "$${uiState.netProfit}"

    Column {
        Card(
            colors = CardDefaults.cardColors(containerColor = cardBg),
            border = BorderStroke(1.dp, cardBorder),
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                    Text(titleText, color = textColor, fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
                }
                Spacer(modifier = Modifier.height(8.dp))
                Text(amountText, color = textColor, fontSize = 24.sp, fontWeight = FontWeight.Bold)
            }
        }

        if (isLoss) {
            Spacer(modifier = Modifier.height(16.dp))
            Surface(
                color = Color(0xFFFFF8E1),
                shape = RoundedCornerShape(12.dp),
                border = BorderStroke(1.dp, Color(0xFFFFECB3)),
                modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp)
            ) {
                Row(modifier = Modifier.padding(16.dp)) {
                    Icon(Icons.Default.Warning, contentDescription = "Warning", tint = Color(0xFFF57F17))
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text("You're operating at a loss this period", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = Color(0xFFF57F17))
                        Spacer(modifier = Modifier.height(4.dp))
                        Text("Consider reviewing your expenses to bring profitability back on track.", fontSize = 12.sp, color = Color(0xFFF57F17))
                    }
                }
            }
        }
    }
}

@Composable
fun ChartCard(uiState: ProfitLossUiState) {
    Card(
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(1.dp, BorderLight),
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Sales vs Expenses", color = Color.Black, fontSize = 16.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(12.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(modifier = Modifier.size(10.dp).background(ChartSalesColor))
                Spacer(modifier = Modifier.width(6.dp))
                Text("Sales", fontSize = 12.sp, color = Color.DarkGray)
                Spacer(modifier = Modifier.width(16.dp))
                Box(modifier = Modifier.size(10.dp).background(ChartExpensesColor))
                Spacer(modifier = Modifier.width(6.dp))
                Text("Expenses", fontSize = 12.sp, color = Color.DarkGray)
            }
            Spacer(modifier = Modifier.height(24.dp))

            Row(modifier = Modifier.fillMaxWidth().height(120.dp), horizontalArrangement = Arrangement.SpaceAround, verticalAlignment = Alignment.Bottom) {
                uiState.chartData.forEach { data ->
                    Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Bottom, modifier = Modifier.fillMaxHeight()) {
                        Row(verticalAlignment = Alignment.Bottom, modifier = Modifier.weight(1f)) {
                            Box(modifier = Modifier.width(10.dp).fillMaxHeight(data.salesValue).background(ChartSalesColor, RoundedCornerShape(topStart = 4.dp, topEnd = 4.dp)))
                            Box(modifier = Modifier.width(10.dp).fillMaxHeight(data.expensesValue).background(ChartExpensesColor, RoundedCornerShape(topStart = 4.dp, topEnd = 4.dp)))
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(data.label, fontSize = 12.sp, color = Color.DarkGray)
                    }
                }
            }
            Spacer(modifier = Modifier.height(24.dp))
            Text("Profit Margin: ${uiState.profitMargin}", fontSize = 12.sp, color = Color.DarkGray)
        }
    }
}

@Composable
fun TopExpensesCard(expenses: List<ExpenseCategoryData>) {
    Card(
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(1.dp, BorderLight),
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Top Expense Categories", color = Color.Black, fontSize = 16.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(16.dp))

            expenses.forEach { expense ->
                Row(modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp), verticalAlignment = Alignment.CenterVertically) {
                    Text(expense.name, fontSize = 13.sp, color = Color.DarkGray, modifier = Modifier.width(80.dp))
                    Box(modifier = Modifier.weight(1f).height(8.dp).clip(RoundedCornerShape(4.dp)).background(Color(0xFFEEEEEE))) {
                        Box(modifier = Modifier.fillMaxWidth(expense.percentage / 100f).fillMaxHeight().clip(RoundedCornerShape(4.dp)).background(Color(expense.color)))
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Text("${expense.percentage}%", fontSize = 13.sp, color = Color.Black, modifier = Modifier.width(32.dp))
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MockDateRangePickerSheet(onApply: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 16.dp)
    ) {
        Box(
            modifier = Modifier
                .width(40.dp)
                .height(4.dp)
                .clip(RoundedCornerShape(2.dp))
                .background(Color(0xFFE0E0E0))
                .align(Alignment.CenterHorizontally)
        )
        Spacer(modifier = Modifier.height(24.dp))

        Text("Select Date Range", fontWeight = FontWeight.Bold, fontSize = 15.sp, color = Color.Black)
        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = onApply,
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            colors = ButtonDefaults.buttonColors(containerColor = DarkGreen),
            shape = RoundedCornerShape(8.dp)
        ) {
            Text("Apply Range", color = Color.White, fontSize = 15.sp, fontWeight = FontWeight.Medium)
        }
    }
}