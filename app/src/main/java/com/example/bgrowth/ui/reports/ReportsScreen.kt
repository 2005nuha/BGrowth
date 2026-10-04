package com.example.bgrowth.ui.reports

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel

val DarkGreen = Color(0xFF105942)
val BackgroundColor = Color(0xFFF9F9F9)
val BorderLight = Color(0xFFE5E5E5)
val GrayText = Color(0xFF888888)
val LightGreenBg = Color(0xFFE8F5E9)

val C_Sales = Color(0xFF8FB399)
val C_Expenses = Color(0xFFFFD580)
val C_NetProfit = Color(0xFF3F705B)
val C_OwedToMe = Color(0xFF6D9F8D)
val C_IOwe = Color(0xFFD32F2F)

val ChartSales = Color(0xFF6D9F8D)
val ChartExpenses = Color(0xFFEF9A9A)

@Composable
fun ReportsScreen(
    viewModel: ReportsViewModel = viewModel(),
    onNavigateBack: () -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsState()

    ReportsContent(
        uiState = uiState,
        onPeriodSelectorClick = viewModel::onPeriodSelectorClick,
        onBottomSheetPeriodSelected = viewModel::onBottomSheetPeriodSelected,
        onApplyPeriod = viewModel::onApplyPeriod,
        onNavigateBack = onNavigateBack
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReportsContent(
    uiState: ReportsUiState,
    onPeriodSelectorClick: (Boolean) -> Unit,
    onBottomSheetPeriodSelected: (ReportPeriod) -> Unit,
    onApplyPeriod: () -> Unit,
    onNavigateBack: () -> Unit
) {
    Scaffold(
        containerColor = BackgroundColor,
        topBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 24.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(onClick = onNavigateBack, modifier = Modifier.size(24.dp)) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = DarkGreen)
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    Text("Reports", color = DarkGreen, fontWeight = FontWeight.Bold, fontSize = 20.sp)
                }
                Text("↓ Export", color = DarkGreen, fontWeight = FontWeight.Bold, fontSize = 14.sp)
            }
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 20.dp),
            contentPadding = PaddingValues(bottom = 24.dp)
        ) {
            item {
                // البطاقة الأولى
                Card(
                    modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    border = BorderStroke(1.dp, BorderLight),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Column(modifier = Modifier.padding(20.dp)) {
                        Text("Business Performance Report", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = Color.Black)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text("Ahmad's Coffee Shop", color = GrayText, fontSize = 14.sp)

                        Spacer(modifier = Modifier.height(16.dp))
                        HorizontalDivider(color = BorderLight)
                        Spacer(modifier = Modifier.height(16.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("Period", color = GrayText, fontSize = 14.sp)
                            Text(
                                text = uiState.selectedPeriod.displayLabel + " ▾",
                                color = DarkGreen,
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp,
                                modifier = Modifier.clickable { onPeriodSelectorClick(true) }
                            )
                        }
                    }
                }

                // البطاقة الثانية (قائمة الأرقام)
                Card(
                    modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    border = BorderStroke(1.dp, BorderLight),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Column(modifier = Modifier.padding(20.dp)) {
                        MetricRow(C_Sales, "Total Sales", "$3,240.00", Color.Black)
                        HorizontalDivider(color = BorderLight, modifier = Modifier.padding(vertical = 12.dp))

                        MetricRow(C_Expenses, "Total Expenses", "$1,390.00", Color.Black)
                        HorizontalDivider(color = BorderLight, modifier = Modifier.padding(vertical = 12.dp))

                        MetricRow(C_NetProfit, "Net Profit", "$1,850.00", DarkGreen)
                        HorizontalDivider(color = BorderLight, modifier = Modifier.padding(vertical = 12.dp))

                        MetricRow(C_OwedToMe, "Outstanding — Owed to Me", "$285.00", DarkGreen)
                        HorizontalDivider(color = BorderLight, modifier = Modifier.padding(vertical = 12.dp))

                        MetricRow(C_IOwe, "Outstanding — I Owe", "$150.00", C_IOwe)
                    }
                }

                // البطاقة الثالثة (تتغير بناءً على الاختيار)
                if (uiState.selectedPeriod == ReportPeriod.THIS_YEAR) {
                    DonutChartCard()
                } else {
                    BarChartCard(uiState.selectedPeriod.title)
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Generated on Sep 12, 2026 · BGrowth",
                    color = GrayText,
                    fontSize = 10.sp,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center
                )
            }
        }
    }

    // القائمة السفلية (Bottom Sheet)
    if (uiState.showPeriodSelector) {
        ModalBottomSheet(
            onDismissRequest = { onPeriodSelectorClick(false) },
            containerColor = Color.White,
            shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
        ) {
            Column(modifier = Modifier.padding(24.dp).fillMaxWidth()) {
                Text("Select Report Period", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = Color.Black)
                Spacer(modifier = Modifier.height(16.dp))

                ReportPeriod.entries.forEach { period ->
                    val isSelected = uiState.bottomSheetSelectedPeriod == period
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(8.dp))
                            .background(if (isSelected) LightGreenBg else Color.Transparent)
                            .clickable { onBottomSheetPeriodSelected(period) }
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(period.title, color = Color.Black, fontSize = 16.sp)
                        if (isSelected) {
                            Icon(Icons.Default.Check, contentDescription = null, tint = DarkGreen)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(BackgroundColor)
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.AutoMirrored.Filled.KeyboardArrowLeft, contentDescription = null, tint = DarkGreen)
                    Text(uiState.bottomSheetMonth, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    Icon(Icons.AutoMirrored.Filled.KeyboardArrowRight, contentDescription = null, tint = DarkGreen)
                }

                Spacer(modifier = Modifier.height(24.dp))

                Button(
                    onClick = onApplyPeriod,
                    modifier = Modifier.fillMaxWidth().height(50.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = DarkGreen),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Apply", fontSize = 16.sp, color = Color.White)
                }
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

// ======================== المكونات المساعدة ======================== //

@Composable
fun MetricRow(iconColor: Color, title: String, amount: String, amountColor: Color) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(modifier = Modifier.size(14.dp).clip(RoundedCornerShape(2.dp)).background(iconColor))
            Spacer(modifier = Modifier.width(12.dp))
            Text(title, color = Color.Black, fontSize = 14.sp)
        }
        Text(amount, fontWeight = FontWeight.Bold, fontSize = 14.sp, color = amountColor)
    }
}

@Composable
fun BarChartCard(periodTitle: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(1.dp, BorderLight),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Text("Financial Performance — $periodTitle", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = Color.Black)
            Spacer(modifier = Modifier.height(12.dp))
            Row {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(modifier = Modifier.size(10.dp).background(ChartSales))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Sales", fontSize = 10.sp, color = GrayText)
                }
                Spacer(modifier = Modifier.width(16.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(modifier = Modifier.size(10.dp).background(ChartExpenses))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Expenses", fontSize = 10.sp, color = GrayText)
                }
            }
            Spacer(modifier = Modifier.height(24.dp))
            Row(
                modifier = Modifier.fillMaxWidth().height(120.dp),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.Bottom
            ) {
                BarPair("W1", 40.dp, 60.dp)
                BarPair("W2", 30.dp, 80.dp)
                BarPair("W3", 20.dp, 30.dp)
                BarPair("W4", 30.dp, 70.dp)
            }
        }
    }
}

@Composable
fun BarPair(label: String, h1: Dp, h2: Dp) {
    Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Bottom) {
        Row(verticalAlignment = Alignment.Bottom, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
            Box(Modifier.width(10.dp).height(h1).clip(RoundedCornerShape(topStart = 4.dp, topEnd = 4.dp)).background(ChartSales))
            Box(Modifier.width(10.dp).height(h2).clip(RoundedCornerShape(topStart = 4.dp, topEnd = 4.dp)).background(ChartExpenses))
        }
        Spacer(Modifier.height(8.dp))
        Text(label, fontSize = 10.sp, color = GrayText)
    }
}

@Composable
fun DonutChartCard() {
    Column {
        Card(
            modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            border = BorderStroke(1.dp, BorderLight),
            shape = RoundedCornerShape(12.dp)
        ) {
            Column(modifier = Modifier.padding(20.dp).fillMaxWidth()) {
                Text("Sales vs Expenses — This Year", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = DarkGreen, modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center)
                Spacer(modifier = Modifier.height(24.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Canvas(modifier = Modifier.size(110.dp)) {
                            drawArc(
                                color = ChartExpenses,
                                startAngle = -90f,
                                sweepAngle = 110f,
                                useCenter = false,
                                style = Stroke(width = 40f, cap = StrokeCap.Butt)
                            )
                            drawArc(
                                color = ChartSales,
                                startAngle = 20f,
                                sweepAngle = 250f,
                                useCenter = false,
                                style = Stroke(width = 40f, cap = StrokeCap.Butt)
                            )
                        }
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("$4,630", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = Color.Black)
                            Text("Total", fontSize = 10.sp, color = GrayText)
                        }
                    }
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(modifier = Modifier.size(10.dp).background(ChartSales))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Sales", fontSize = 12.sp, color = GrayText, modifier = Modifier.width(60.dp))
                            Text("$3,240", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = DarkGreen)
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(modifier = Modifier.size(10.dp).background(ChartExpenses))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Expenses", fontSize = 12.sp, color = GrayText, modifier = Modifier.width(60.dp))
                            Text("$1,390", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = C_IOwe)
                        }
                    }
                }
            }
        }

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            border = BorderStroke(1.dp, BorderLight),
            shape = RoundedCornerShape(12.dp)
        ) {
            Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                Text("🏆", fontSize = 16.sp)
                Spacer(modifier = Modifier.width(12.dp))
                Text("Best month so far: ", fontSize = 12.sp, color = GrayText)
                Text("September", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = DarkGreen)
                Text(" — $3,240.00 in sales", fontSize = 12.sp, color = GrayText)
            }
        }
    }
}

// ============================== Previews ============================== //

@Preview(showBackground = true, device = "id:pixel_5", name = "1. Reports (Month)")
@Composable
fun ReportsMonthPreview() {
    MaterialTheme {
        ReportsContent(
            uiState = ReportsUiState(selectedPeriod = ReportPeriod.THIS_MONTH),
            onPeriodSelectorClick = {}, onBottomSheetPeriodSelected = {}, onApplyPeriod = {}, onNavigateBack = {}
        )
    }
}

@Preview(showBackground = true, device = "id:pixel_5", name = "2. Reports (Year)")
@Composable
fun ReportsYearPreview() {
    MaterialTheme {
        ReportsContent(
            uiState = ReportsUiState(selectedPeriod = ReportPeriod.THIS_YEAR),
            onPeriodSelectorClick = {}, onBottomSheetPeriodSelected = {}, onApplyPeriod = {}, onNavigateBack = {}
        )
    }
}

@Preview(showBackground = true, device = "id:pixel_5", name = "3. Bottom Sheet")
@Composable
fun ReportsBottomSheetPreview() {
    MaterialTheme {
        ReportsContent(
            uiState = ReportsUiState(showPeriodSelector = true),
            onPeriodSelectorClick = {}, onBottomSheetPeriodSelected = {}, onApplyPeriod = {}, onNavigateBack = {}
        )
    }
}