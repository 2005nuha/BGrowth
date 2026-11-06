package com.example.bgrowth.ui.debts

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel

private val DebtsDarkGreen = Color(0xFF105942)
private val DebtsBackgroundColor = Color(0xFFF9F9F9)
private val DebtsBorderLight = Color(0xFFE5E5E5)
private val DebtsGrayText = Color(0xFF888888)
private val DebtsRedText = Color(0xFFD32F2F)
private val DebtsOrangeText = Color(0xFFF57C00)

@Composable
fun DebtsScreen(
    viewModel: DebtsViewModel = viewModel(),
    onNavigateBack: () -> Unit = {},
    onAddDebtClick: () -> Unit = {},
    onDebtClick: (Int) -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsState()

    DebtsContent(
        uiState = uiState,
        onTabSelected = viewModel::onTabSelected,
        onNavigateBack = onNavigateBack,
        onAddDebtClick = onAddDebtClick,
        onDebtClick = onDebtClick
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DebtsContent(
    uiState: DebtsUiState,
    onTabSelected: (String) -> Unit,
    onNavigateBack: () -> Unit,
    onAddDebtClick: () -> Unit,
    onDebtClick: (Int) -> Unit
) {
    Scaffold(
        containerColor = DebtsBackgroundColor,
        topBar = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 24.dp)
            ) {
                IconButton(onClick = onNavigateBack, modifier = Modifier.size(24.dp)) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = DebtsDarkGreen)
                }
                Spacer(modifier = Modifier.width(16.dp))
                Text("Debts Tracker", color = DebtsDarkGreen, fontWeight = FontWeight.Bold, fontSize = 20.sp)
            }
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onAddDebtClick,
                containerColor = DebtsDarkGreen,
                contentColor = Color.White
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add Debt")
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 20.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color(0xFFEEEEEE))
                    .padding(4.dp)
            ) {
                listOf("Owed to Me", "I Owe").forEach { tab ->
                    val selected = uiState.selectedTab == tab
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(8.dp))
                            .background(if (selected) Color.White else Color.Transparent)
                            .clickable { onTabSelected(tab) }
                            .padding(vertical = 10.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = tab,
                            fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal,
                            color = if (selected) DebtsDarkGreen else DebtsGrayText,
                            fontSize = 14.sp
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(bottom = 80.dp)
            ) {
                items(uiState.debtsList) { item ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onDebtClick(item.id) },
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        border = BorderStroke(1.dp, DebtsBorderLight),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(40.dp)
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(Color(0xFFE8E8E8)),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(item.initials, fontWeight = FontWeight.Bold, fontSize = 14.sp, color = Color.Black)
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(item.name, fontWeight = FontWeight.Bold, fontSize = 15.sp, color = Color.Black)
                                Text(item.dueDate, fontSize = 12.sp, color = DebtsGrayText)
                            }
                            Column(horizontalAlignment = Alignment.End) {
                                Text(item.amount, fontWeight = FontWeight.Bold, fontSize = 15.sp, color = DebtsDarkGreen)
                                val statusColor = if (item.isUnpaid) DebtsRedText else DebtsOrangeText
                                Text(item.status, fontSize = 11.sp, color = statusColor, fontWeight = FontWeight.Medium)
                            }
                        }
                    }
                }
            }
        }
    }
}