package com.example.bgrowth.ui.debts.detail


import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel

private val DetailDarkGreen = Color(0xFF105942)
private val DetailBackgroundColor = Color(0xFFF9F9F9)
private val DetailBorderLight = Color(0xFFE5E5E5)
private val DetailGrayText = Color(0xFF888888)
private val DetailRedText = Color(0xFFD32F2F)
private val DetailOrangeText = Color(0xFFF57C00)
private val DetailWarningBg = Color(0xFFFFF8E1)
private val DetailWarningBorder = Color(0xFFFFE082)

@Composable
fun DebtDetailScreen(
    viewModel: DebtDetailViewModel = viewModel(),
    onNavigateBack: () -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsState()

    DebtDetailContent(
        uiState = uiState,
        onRecordPayment = viewModel::onRecordPayment,
        onNavigateBack = onNavigateBack
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DebtDetailContent(
    uiState: DebtDetailUiState,
    onRecordPayment: () -> Unit,
    onNavigateBack: () -> Unit
) {
    Scaffold(
        containerColor = DetailBackgroundColor,
        topBar = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 24.dp)
            ) {
                IconButton(onClick = onNavigateBack, modifier = Modifier.size(24.dp)) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = DetailDarkGreen)
                }
                Spacer(modifier = Modifier.width(16.dp))
                Text("Debt Details", color = DetailDarkGreen, fontWeight = FontWeight.Bold, fontSize = 20.sp)
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
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    border = BorderStroke(1.dp, DetailBorderLight),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Column(modifier = Modifier.padding(20.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(48.dp)
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(Color(0xFFE8E8E8)),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(uiState.initials, fontWeight = FontWeight.Bold, fontSize = 16.sp, color = Color.Black)
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(uiState.partyName, fontWeight = FontWeight.Bold, fontSize = 16.sp, color = Color.Black)
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(uiState.debtTypeAndDate, fontSize = 12.sp, color = DetailGrayText)
                            }
                            val statusBg = if (uiState.isUnpaid) Color(0xFFFDEBEA) else Color(0xFFFFF3E0)
                            val statusColor = if (uiState.isUnpaid) DetailRedText else DetailOrangeText
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(4.dp))
                                    .background(statusBg)
                                    .padding(horizontal = 8.dp, vertical = 4.dp)
                            ) {
                                Text(uiState.statusText, color = statusColor, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            }
                        }

                        Spacer(modifier = Modifier.height(20.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column {
                                Text("Paid Amount", fontSize = 12.sp, color = DetailGrayText)
                                Text(uiState.paidAmountText, fontSize = 16.sp, fontWeight = FontWeight.Bold, color = DetailDarkGreen)
                            }
                            Column(horizontalAlignment = Alignment.End) {
                                Text("Remaining Amount", fontSize = 12.sp, color = DetailGrayText)
                                Text(uiState.remainingAmountText, fontSize = 16.sp, fontWeight = FontWeight.Bold, color = DetailRedText)
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        LinearProgressIndicator(
                            progress = { uiState.progress },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(8.dp)
                                .clip(RoundedCornerShape(4.dp)),
                            color = DetailDarkGreen,
                            trackColor = Color(0xFFE0E0E0)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                if (!uiState.dueWarningMessage.isNullOrEmpty()) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(8.dp))
                            .background(DetailWarningBg)
                            .border(1.dp, DetailWarningBorder, RoundedCornerShape(8.dp))
                            .padding(horizontal = 12.dp, vertical = 10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            Icons.Outlined.Notifications,
                            contentDescription = "Warning",
                            tint = DetailOrangeText,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = uiState.dueWarningMessage,
                            color = DetailOrangeText,
                            fontSize = 12.sp
                        )
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                }

                Button(
                    onClick = onRecordPayment,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = DetailDarkGreen),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Icon(Icons.Default.Add, contentDescription = null, tint = Color.White)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Record Payment", color = Color.White, fontSize = 15.sp, fontWeight = FontWeight.Medium)
                }

                Spacer(modifier = Modifier.height(24.dp))

                Text("Payment History", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = Color.Black)
                Spacer(modifier = Modifier.height(12.dp))
            }

            items(uiState.paymentHistory) { item ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    border = BorderStroke(1.dp, DetailBorderLight),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(item.dateAndMethod, fontSize = 13.sp, color = Color.Black)
                        Text(item.amount, fontWeight = FontWeight.Bold, fontSize = 14.sp, color = DetailDarkGreen)
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true, device = "id:pixel_5", name = "1. Partially Paid Debt")
@Composable
fun DebtDetailPartiallyPaidPreview() {
    val mockUiState = DebtDetailUiState(
        partyName = "Sara Ahmed",
        initials = "SA",
        debtTypeAndDate = "Customer Debt • Due Sep 20",
        totalAmount = "$150.00",
        paidAmountText = "$45.00",
        remainingAmountText = "$105.00",
        progress = 0.3f,
        statusText = "Partially Paid",
        isUnpaid = false,
        dueWarningMessage = "Payment is overdue by 3 days",
        paymentHistory = listOf(
            PaymentHistoryItem(1, "Sep 10 • Cash", "$45.00")
        )
    )
    MaterialTheme { DebtDetailContent(uiState = mockUiState, onRecordPayment = {}, onNavigateBack = {}) }
}