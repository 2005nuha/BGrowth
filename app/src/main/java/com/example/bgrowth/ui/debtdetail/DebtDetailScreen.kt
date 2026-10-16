package com.example.bgrowth.ui.debtdetail

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
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
val WarningBg = Color(0xFFFFF8E1)
val WarningBorder = Color(0xFFFFE082)

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
                Text("Debt Detail", color = DarkGreen, fontWeight = FontWeight.Bold, fontSize = 20.sp)
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
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    border = BorderStroke(1.dp, BorderLight),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp)
                    ) {
                        // تحديد لون الحالة برمجياً
                        val statusColor = when (uiState.statusText) {
                            "Unpaid" -> RedText
                            "Paid" -> DarkGreen
                            else -> OrangeText
                        }

                        // الاسم والحالة
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = uiState.partyName,
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp,
                                color = Color.Black
                            )
                            Text(
                                text = uiState.statusText,
                                color = statusColor,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Medium
                            )
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = uiState.debtTypeAndDate,
                            color = GrayText,
                            fontSize = 13.sp
                        )

                        Spacer(modifier = Modifier.height(24.dp))

                        Text(
                            text = uiState.totalAmount,
                            color = RedText,
                            fontWeight = FontWeight.Bold,
                            fontSize = 24.sp,
                            modifier = Modifier.align(Alignment.CenterHorizontally)
                        )

                        Spacer(modifier = Modifier.height(24.dp))

                        // شريط التقدم المرسوم بدقة متناهية (Pixel-Perfect Canvas)
                        // شريط التقدم المتصل (Continuous Progress Bar) بدون أي فراغات
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(12.dp)
                                .clip(CircleShape)
                                .background(Color(0xFFEEEEEE)) // الخلفية الرمادية المتصلة بالكامل
                        ) {
                            // الجزء الأخضر يمتد فوق الرمادي بنعومة
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth(uiState.progress)
                                    .fillMaxHeight()
                                    .clip(CircleShape)
                                    .background(DarkGreen)
                            )
                        }
                        Spacer(modifier = Modifier.height(8.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(uiState.paidAmountText, color = GrayText, fontSize = 12.sp)
                            Text(uiState.remainingAmountText, color = GrayText, fontSize = 12.sp)
                        }

                        Spacer(modifier = Modifier.height(20.dp))

                        // مربع التنبيه الأصفر (يظهر فقط إذا كان هناك رسالة تحذير)
                        if (uiState.dueWarningMessage.isNotEmpty()) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(WarningBg)
                                    .border(1.dp, WarningBorder, RoundedCornerShape(8.dp))
                                    .padding(horizontal = 12.dp, vertical = 10.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Outlined.Notifications,
                                    contentDescription = "Warning",
                                    tint = OrangeText,
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = uiState.dueWarningMessage,
                                    color = OrangeText,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(32.dp))

                Text(
                    text = "Payment History",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = Color.Black
                )
                Spacer(modifier = Modifier.height(16.dp))
            }

            // قائمة الدفعات
            items(uiState.paymentHistory) { historyItem ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = historyItem.dateAndMethod,
                        color = GrayText,
                        fontSize = 14.sp
                    )
                    Text(
                        text = historyItem.amount,
                        color = DarkGreen,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )
                }
                HorizontalDivider(color = BorderLight, modifier = Modifier.padding(top = 8.dp))
            }

            item {
                Spacer(modifier = Modifier.height(32.dp))

                Button(
                    onClick = onRecordPayment,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = DarkGreen),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Add", tint = Color.White)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Record Payment", color = Color.White, fontSize = 15.sp, fontWeight = FontWeight.Medium)
                }
            }
        }
    }
}

// ============================== Previews ============================== //

@Preview(showBackground = true, device = "id:pixel_5", name = "1. Partially Paid (Default)")
@Composable
fun DebtDetailPartiallyPaidPreview() {
    val mockUiState = DebtDetailUiState(
        partyName = "Rana Nasser",
        debtTypeAndDate = "Owed to Me · Due Sep 15",
        statusText = "Partially Paid",
        totalAmount = "$120.00",
        paidAmountText = "$48.00 paid",
        remainingAmountText = "$72.00 remaining",
        progress = 0.4f,
        dueWarningMessage = "Due in 3 days",
        paymentHistory = listOf(
            PaymentHistoryItem(1, "Sep 8 — Cash", "+$48.00")
        )
    )
    MaterialTheme { DebtDetailContent(uiState = mockUiState, onRecordPayment = {}, onNavigateBack = {}) }
}

@Preview(showBackground = true, device = "id:pixel_5", name = "2. Unpaid (Empty History)")
@Composable
fun DebtDetailUnpaidPreview() {
    val mockUiState = DebtDetailUiState(
        partyName = "Mohammed Khalil",
        debtTypeAndDate = "I Owe · Due Sep 20",
        statusText = "Unpaid",
        totalAmount = "$150.00",
        paidAmountText = "$0.00 paid",
        remainingAmountText = "$150.00 remaining",
        progress = 0.0f,
        dueWarningMessage = "",
        paymentHistory = emptyList()
    )
    MaterialTheme { DebtDetailContent(uiState = mockUiState, onRecordPayment = {}, onNavigateBack = {}) }
}

@Preview(showBackground = true, device = "id:pixel_5", name = "3. Fully Paid")
@Composable
fun DebtDetailPaidPreview() {
    val mockUiState = DebtDetailUiState(
        partyName = "Sara Ahmed",
        debtTypeAndDate = "Owed to Me · Completed",
        statusText = "Paid",
        totalAmount = "$200.00",
        paidAmountText = "$200.00 paid",
        remainingAmountText = "$0.00 remaining",
        progress = 1.0f,
        dueWarningMessage = "",
        paymentHistory = listOf(
            PaymentHistoryItem(1, "Sep 1 — Bank Transfer", "+$100.00"),
            PaymentHistoryItem(2, "Sep 10 — Cash", "+$100.00")
        )
    )
    MaterialTheme { DebtDetailContent(uiState = mockUiState, onRecordPayment = {}, onNavigateBack = {}) }
}