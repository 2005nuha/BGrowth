package com.example.bgrowth.ui.recordpayment

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
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

val DarkGreen = Color(0xFF105942)
val BackgroundColor = Color(0xFFF9F9F9)
val BorderLight = Color(0xFFE5E5E5)
val GrayText = Color(0xFF888888)
val RedText = Color(0xFFD32F2F)
val HeaderBg = Color(0xFFFFEBEE) // لون أحمر فاتح للبطاقة العلوية

@Composable
fun RecordPaymentScreen(
    viewModel: RecordPaymentViewModel = viewModel(),
    onNavigateBack: () -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsState()

    RecordPaymentContent(
        uiState = uiState,
        onPaymentAmountChange = viewModel::onPaymentAmountChange,
        onAmountTypeSelected = viewModel::onAmountTypeSelected,
        onPaymentMethodDropdownExpanded = viewModel::onPaymentMethodDropdownExpanded,
        onPaymentMethodSelected = viewModel::onPaymentMethodSelected,
        onConfirmPayment = viewModel::onConfirmPayment,
        onNavigateBack = onNavigateBack
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecordPaymentContent(
    uiState: RecordPaymentUiState,
    onPaymentAmountChange: (String) -> Unit,
    onAmountTypeSelected: (AmountType) -> Unit,
    onPaymentMethodDropdownExpanded: (Boolean) -> Unit,
    onPaymentMethodSelected: (String) -> Unit,
    onConfirmPayment: () -> Unit,
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
                Text("Record Payment", color = DarkGreen, fontWeight = FontWeight.Bold, fontSize = 20.sp)
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
                // البطاقة العلوية (الاسم والرصيد)
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(HeaderBg)
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = uiState.partyName,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            color = Color.Black
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Remaining balance",
                            color = GrayText,
                            fontSize = 12.sp
                        )
                    }
                    Text(
                        text = "$${String.format("%.2f", uiState.remainingBalance)}",
                        color = RedText,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )
                }

                Spacer(modifier = Modifier.height(32.dp))

                // حقل إدخال المبلغ
                Text("Payment Amount", color = Color.Black, fontSize = 14.sp)
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = uiState.paymentAmount,
                    onValueChange = onPaymentAmountChange,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        unfocusedBorderColor = BorderLight,
                        focusedBorderColor = DarkGreen,
                        unfocusedContainerColor = Color.Transparent,
                        focusedContainerColor = Color.Transparent
                    ),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(16.dp))

                // أزرار الاختيار السريع (Full, Half, Custom)
                SegmentedAmountControl(
                    selectedType = uiState.selectedAmountType,
                    fullAmount = uiState.remainingBalance,
                    onTypeSelected = onAmountTypeSelected
                )

                Spacer(modifier = Modifier.height(32.dp))

                // صف التاريخ وطريقة الدفع
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // التاريخ
                    Column(modifier = Modifier.weight(1f)) {
                        Text("Date", color = Color.Black, fontSize = 14.sp)
                        Spacer(modifier = Modifier.height(8.dp))
                        OutlinedTextField(
                            value = uiState.selectedDate,
                            onValueChange = {},
                            readOnly = true,
                            shape = RoundedCornerShape(12.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                unfocusedBorderColor = BorderLight,
                                focusedBorderColor = BorderLight
                            )
                        )
                    }
                    // طريقة الدفع
                    Column(modifier = Modifier.weight(1f)) {
                        Text("Payment Method", color = Color.Black, fontSize = 14.sp)
                        Spacer(modifier = Modifier.height(8.dp))
                        ExposedDropdownMenuBox(
                            expanded = uiState.isPaymentMethodDropdownExpanded,
                            onExpandedChange = onPaymentMethodDropdownExpanded
                        ) {
                            OutlinedTextField(
                                value = uiState.paymentMethod,
                                onValueChange = {},
                                readOnly = true,
                                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = uiState.isPaymentMethodDropdownExpanded) },
                                shape = RoundedCornerShape(12.dp),
                                colors = OutlinedTextFieldDefaults.colors(
                                    unfocusedBorderColor = BorderLight,
                                    focusedBorderColor = DarkGreen
                                ),
                                modifier = Modifier.menuAnchor()
                            )
                            ExposedDropdownMenu(
                                expanded = uiState.isPaymentMethodDropdownExpanded,
                                onDismissRequest = { onPaymentMethodDropdownExpanded(false) },
                                modifier = Modifier.background(Color.White)
                            ) {
                                uiState.paymentMethodOptions.forEach { method ->
                                    DropdownMenuItem(
                                        text = { Text(method) },
                                        onClick = { onPaymentMethodSelected(method) }
                                    )
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(32.dp))

                // رسالة الملاحظة الديناميكية
                val hintText = if (uiState.selectedAmountType == AmountType.FULL || uiState.paymentAmount.toDoubleOrNull() == uiState.remainingBalance) {
                    "This payment will fully settle the debt and mark it as Paid."
                } else {
                    "This is a partial payment. The remaining balance will be updated."
                }

                Text(
                    text = hintText,
                    color = GrayText,
                    fontSize = 12.sp,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                // زر التأكيد
                Button(
                    onClick = onConfirmPayment,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = DarkGreen),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Icon(Icons.Default.Check, contentDescription = "Confirm", tint = Color.White)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Confirm Payment", color = Color.White, fontSize = 15.sp, fontWeight = FontWeight.Medium)
                }
            }
        }
    }
}

// مكون مخصص لأزرار الاختيار (Segmented Buttons)
@Composable
fun SegmentedAmountControl(
    selectedType: AmountType,
    fullAmount: Double,
    onTypeSelected: (AmountType) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(48.dp)
            .border(1.dp, BorderLight, RoundedCornerShape(12.dp))
            .clip(RoundedCornerShape(12.dp))
    ) {
        SegmentedButton(
            text = "Full ($${String.format("%.2f", fullAmount)})",
            isSelected = selectedType == AmountType.FULL,
            modifier = Modifier.weight(1f),
            onClick = { onTypeSelected(AmountType.FULL) }
        )
        Box(modifier = Modifier.width(1.dp).fillMaxHeight().background(BorderLight))
        SegmentedButton(
            text = "Half ($${String.format("%.2f", fullAmount / 2)})",
            isSelected = selectedType == AmountType.HALF,
            modifier = Modifier.weight(1f),
            onClick = { onTypeSelected(AmountType.HALF) }
        )
        Box(modifier = Modifier.width(1.dp).fillMaxHeight().background(BorderLight))
        SegmentedButton(
            text = "Custom",
            isSelected = selectedType == AmountType.CUSTOM,
            modifier = Modifier.weight(1f),
            onClick = { onTypeSelected(AmountType.CUSTOM) }
        )
    }
}

@Composable
fun SegmentedButton(
    text: String,
    isSelected: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Box(
        modifier = modifier
            .fillMaxHeight()
            .background(if (isSelected) DarkGreen else Color.Transparent)
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            color = if (isSelected) Color.White else Color.Black,
            fontSize = 11.sp,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
        )
    }
}

// ============================== Previews ============================== //

@Preview(showBackground = true, device = "id:pixel_5", name = "Record Payment (Full)")
@Composable
fun RecordPaymentScreenFullPreview() {
    MaterialTheme {
        RecordPaymentContent(
            uiState = RecordPaymentUiState(),
            onPaymentAmountChange = {},
            onAmountTypeSelected = {},
            onPaymentMethodDropdownExpanded = {},
            onPaymentMethodSelected = {},
            onConfirmPayment = {},
            onNavigateBack = {}
        )
    }
}

@Preview(showBackground = true, device = "id:pixel_5", name = "Record Payment (Half Dropdown)")
@Composable
fun RecordPaymentScreenHalfPreview() {
    val mockUiState = RecordPaymentUiState(
        paymentAmount = "36.00",
        selectedAmountType = AmountType.HALF,
        isPaymentMethodDropdownExpanded = true // لإظهار القائمة المنسدلة في الـ Preview
    )
    MaterialTheme {
        RecordPaymentContent(
            uiState = mockUiState,
            onPaymentAmountChange = {},
            onAmountTypeSelected = {},
            onPaymentMethodDropdownExpanded = {},
            onPaymentMethodSelected = {},
            onConfirmPayment = {},
            onNavigateBack = {}
        )
    }
}