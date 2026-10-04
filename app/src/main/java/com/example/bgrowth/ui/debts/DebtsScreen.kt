package com.example.bgrowth.ui.debts

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel

val DarkGreen = Color(0xFF105942)
val BackgroundColor = Color(0xFFF9F9F9)
val BorderLight = Color(0xFFE5E5E5)
val GrayText = Color(0xFF888888)
val RedText = Color(0xFFD32F2F)
val OrangeText = Color(0xFFF57C00)

@Composable
fun DebtsScreen(
    viewModel: DebtsViewModel = viewModel(),
    onNavigateBack: () -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsState()

    DebtsContent(
        uiState = uiState,
        onTabSelected = viewModel::onTabSelected,
        onNavigateBack = onNavigateBack
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DebtsContent(
    uiState: DebtsUiState,
    onTabSelected: (String) -> Unit,
    onNavigateBack: () -> Unit
) {
    Scaffold(
        containerColor = BackgroundColor,
        floatingActionButton = {
            FloatingActionButton(
                onClick = { /* التنقل لشاشة الإضافة */ },
                containerColor = DarkGreen,
                shape = CircleShape
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add Debt", tint = Color.White)
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // الشريط العلوي (Header)
            Column(modifier = Modifier.padding(horizontal = 20.dp, vertical = 24.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(onClick = onNavigateBack, modifier = Modifier.size(24.dp)) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = DarkGreen)
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    Text("Debts", color = DarkGreen, fontWeight = FontWeight.Bold, fontSize = 20.sp)
                }
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Track what you're owed and what you owe",
                    color = GrayText,
                    fontSize = 13.sp,
                    modifier = Modifier.padding(start = 40.dp)
                )
            }

            // تبويبات الأقسام (Tabs)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                val tabs = listOf("Owed to Me", "I Owe")
                tabs.forEach { tab ->
                    val isSelected = uiState.selectedTab == tab
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(45.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(if (isSelected) DarkGreen else Color.Transparent)
                            .clickable { onTabSelected(tab) },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = tab,
                            color = if (isSelected) Color.White else Color.Black,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                            fontSize = 14.sp
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // كروت الملخص (Summary Cards)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                SummaryCard(
                    title = "Total Owed to Me",
                    amount = uiState.totalOwedToMe,
                    borderColor = DarkGreen,
                    modifier = Modifier.weight(1f)
                )
                SummaryCard(
                    title = "Total I Owe",
                    amount = uiState.totalIOwe,
                    borderColor = RedText,
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // قائمة الديون أو حالة "فارغ"
            if (uiState.isEmpty) {
                EmptyDebtsView(selectedTab = uiState.selectedTab)
            } else {
                LazyColumn(
                    contentPadding = PaddingValues(start = 20.dp, end = 20.dp, bottom = 80.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(uiState.debtsList) { debt ->
                        DebtItemCard(debt = debt)
                    }
                }
            }
        }
    }
}

@Composable
fun SummaryCard(title: String, amount: String, borderColor: Color, modifier: Modifier = Modifier) {
    Card(
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(12.dp),
        border = BorderStroke(1.dp, BorderLight),
        modifier = modifier
    ) {
        Row(modifier = Modifier.height(IntrinsicSize.Min)) {
            // الخط الجانبي الملون
            Box(
                modifier = Modifier
                    .width(6.dp)
                    .fillMaxHeight()
                    .background(borderColor)
            )
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(title, color = Color.Black, fontSize = 13.sp, fontWeight = FontWeight.Medium)
                Spacer(modifier = Modifier.height(8.dp))
                Text(amount, color = Color.Black, fontSize = 20.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
fun DebtItemCard(debt: DebtItem) {
    Card(
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(12.dp),
        border = BorderStroke(1.dp, BorderLight),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // الأيقونة الرمادية
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color(0xFFE8E8E8)),
                contentAlignment = Alignment.Center
            ) {
                Text(debt.initials, fontWeight = FontWeight.Bold, fontSize = 16.sp, color = Color.Black)
            }

            Spacer(modifier = Modifier.width(16.dp))

            // الاسم والتاريخ
            Column(modifier = Modifier.weight(1f)) {
                Text(debt.name, fontWeight = FontWeight.Bold, fontSize = 15.sp, color = Color.Black)
                Spacer(modifier = Modifier.height(4.dp))
                Text(debt.dueDate, fontSize = 12.sp, color = GrayText)
            }

            // المبلغ والحالة (Label)
            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = debt.amount,
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp,
                    color = RedText
                )
                Spacer(modifier = Modifier.height(4.dp))

                val statusColor = if (debt.isUnpaid) RedText else OrangeText
                val statusBg = if (debt.isUnpaid) Color(0xFFFDEBEA) else Color(0xFFFFF3E0)

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(4.dp))
                        .background(statusBg)
                        .padding(horizontal = 6.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = debt.statusText,
                        color = statusColor,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Composable
fun EmptyDebtsView(selectedTab: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 40.dp, start = 32.dp, end = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val tabText = if (selectedTab == "Owed to Me") "customers" else "suppliers"
        Text(
            text = "No debts recorded",
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp,
            color = Color.Black
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "You don't have any pending debts for $tabText at the moment.",
            fontSize = 14.sp,
            color = GrayText,
            textAlign = TextAlign.Center
        )
    }
}

// ============================== Previews ============================== //

@Preview(showBackground = true, device = "id:pixel_5", name = "1. Debts (Owed to Me)")
@Composable
fun DebtsOwedToMePreview() {
    val mockUiState = DebtsUiState(
        selectedTab = "Owed to Me",
        debtsList = listOf(
            DebtItem(1, "SA", "Sara Ahmed", "Due Sep 20", "$45.00", true, "Unpaid"),
            DebtItem(2, "MK", "Mohammed Khalil", "Due Sep 15", "$120.00", true, "Unpaid"),
            DebtItem(3, "RN", "Rana Nasser", "No due date", "$120.00", false, "Partially Paid")
        )
    )
    MaterialTheme { DebtsContent(uiState = mockUiState, onTabSelected = {}, onNavigateBack = {}) }
}

@Preview(showBackground = true, device = "id:pixel_5", name = "2. Debts (I Owe)")
@Composable
fun DebtsIOwePreview() {
    val mockUiState = DebtsUiState(
        selectedTab = "I Owe",
        debtsList = listOf(
            DebtItem(4, "JS", "John Supplier", "Due Oct 1", "$150.00", true, "Unpaid")
        )
    )
    MaterialTheme { DebtsContent(uiState = mockUiState, onTabSelected = {}, onNavigateBack = {}) }
}

@Preview(showBackground = true, device = "id:pixel_5", name = "3. Debts (Empty State)")
@Composable
fun DebtsEmptyPreview() {
    val mockUiState = DebtsUiState(
        selectedTab = "Owed to Me",
        totalOwedToMe = "$0.00",
        totalIOwe = "$0.00",
        isEmpty = true
    )
    MaterialTheme { DebtsContent(uiState = mockUiState, onTabSelected = {}, onNavigateBack = {}) }
}