package com.example.bgrowth.ui.invoicedetail

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.outlined.Block
import androidx.compose.material.icons.outlined.Share
import androidx.compose.material.icons.outlined.Timer
import androidx.compose.material.icons.outlined.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
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
val OrangeText = Color(0xFFF57C00)
val OrangeBg = Color(0xFFFFF3E0)
val RedText = Color(0xFFD32F2F)
val RedBg = Color(0xFFFFEBEE)
val StampColor = Color(0x66D32F2F) // لون أحمر شفاف قليلاً للختم

@Composable
fun InvoiceDetailScreen(
    viewModel: InvoiceDetailViewModel = viewModel(),
    onNavigateBack: () -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsState()

    InvoiceDetailContent(
        uiState = uiState,
        onShowCancelDialog = viewModel::onShowCancelDialog,
        onConfirmCancel = viewModel::onConfirmCancel,
        onMarkAsPaid = viewModel::onMarkAsPaid,
        onNavigateBack = onNavigateBack
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InvoiceDetailContent(
    uiState: InvoiceDetailUiState,
    onShowCancelDialog: (Boolean) -> Unit,
    onConfirmCancel: () -> Unit,
    onMarkAsPaid: () -> Unit,
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
                Text("Invoice Detail", color = DarkGreen, fontWeight = FontWeight.Bold, fontSize = 20.sp)
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
                // الفاتورة بالكامل في صندوق واحد لنتمكن من وضع الختم فوقها
                Box(modifier = Modifier.fillMaxWidth()) {

                    // بطاقة الفاتورة البيضاء
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        border = BorderStroke(1.dp, BorderLight),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Column(modifier = Modifier.padding(20.dp)) {

                            // الهيدر (الشعار ومعلومات الفاتورة)
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.Top
                            ) {
                                Column {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Text("BGrowth", color = DarkGreen, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                                    }
                                    Spacer(modifier = Modifier.height(2.dp))
                                    Text("Ahmad's Coffee Shop", color = GrayText, fontSize = 12.sp)
                                }
                                Column(horizontalAlignment = Alignment.End) {
                                    Text(uiState.invoiceId, color = GrayText, fontSize = 12.sp)
                                    Text(uiState.date, color = GrayText, fontSize = 12.sp)
                                    Spacer(modifier = Modifier.height(4.dp))

                                    val statusColor = if (uiState.status == InvoiceStatus.PENDING) OrangeText else GrayText
                                    Text(uiState.status.name.lowercase().replaceFirstChar { it.uppercase() }, color = statusColor, fontSize = 12.sp, fontWeight = FontWeight.Medium)
                                }
                            }

                            Spacer(modifier = Modifier.height(24.dp))

                            // فاتورة إلى
                            Text("Bill To", color = GrayText, fontSize = 12.sp)
                            Spacer(modifier = Modifier.height(4.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(uiState.billToName, fontWeight = FontWeight.Bold, fontSize = 14.sp, color = Color.Black)
                                Text(uiState.billToPhone, fontWeight = FontWeight.Bold, fontSize = 14.sp, color = Color.Black)
                            }

                            Spacer(modifier = Modifier.height(16.dp))
                            HorizontalDivider(color = BorderLight)
                            Spacer(modifier = Modifier.height(16.dp))

                            // رأس جدول العناصر
                            Row(modifier = Modifier.fillMaxWidth()) {
                                Text("ITEM", color = GrayText, fontSize = 10.sp, modifier = Modifier.weight(2f))
                                Text("QTY", color = GrayText, fontSize = 10.sp, modifier = Modifier.weight(1f), textAlign = TextAlign.Center)
                                Text("TOTAL", color = GrayText, fontSize = 10.sp, modifier = Modifier.weight(1f), textAlign = TextAlign.End)
                            }
                            Spacer(modifier = Modifier.height(8.dp))

                            // عناصر الفاتورة
                            uiState.items.forEach { item ->
                                Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                                    Text(item.name, color = Color.Black, fontSize = 12.sp, modifier = Modifier.weight(2f))
                                    Text(item.qty.toString(), color = Color.Black, fontSize = 12.sp, modifier = Modifier.weight(1f), textAlign = TextAlign.Center)
                                    Text(item.total, color = Color.Black, fontSize = 12.sp, modifier = Modifier.weight(1f), textAlign = TextAlign.End)
                                }
                                Spacer(modifier = Modifier.height(8.dp))
                            }

                            Spacer(modifier = Modifier.height(8.dp))
                            HorizontalDivider(color = BorderLight)
                            Spacer(modifier = Modifier.height(16.dp))

                            // الإجمالي
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text("Total", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = Color.Black)
                                Text(uiState.totalAmount, fontWeight = FontWeight.Bold, fontSize = 16.sp, color = DarkGreen)
                            }
                        }
                    }

                    // ختم الإلغاء (يظهر فوق الفاتورة في المنتصف إذا كانت ملغاة)
                    if (uiState.status == InvoiceStatus.CANCELED) {
                        Box(
                            modifier = Modifier
                                .align(Alignment.Center)
                                .rotate(-15f) // ميلان الختم
                                .border(4.dp, StampColor, RoundedCornerShape(8.dp))
                                .padding(horizontal = 24.dp, vertical = 8.dp)
                        ) {
                            Text(
                                text = "CANCELED",
                                color = StampColor,
                                fontSize = 32.sp,
                                fontWeight = FontWeight.ExtraBold,
                                letterSpacing = 4.sp
                            )
                        }
                    }
                } // نهاية الصندوق المشترك

                Spacer(modifier = Modifier.height(24.dp))

                // صندوق التحذير والملاحظات
                if (uiState.status == InvoiceStatus.PENDING) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(8.dp))
                            .background(OrangeBg)
                            .border(1.dp, OrangeText.copy(alpha = 0.5f), RoundedCornerShape(8.dp))
                            .padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Outlined.Timer, contentDescription = null, tint = OrangeText, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Awaiting payment — mark as paid once settled", color = OrangeText, fontSize = 12.sp)
                    }
                } else if (uiState.status == InvoiceStatus.CANCELED) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(8.dp))
                            .background(RedBg)
                            .border(1.dp, RedText.copy(alpha = 0.5f), RoundedCornerShape(8.dp))
                            .padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Outlined.Block, contentDescription = null, tint = RedText, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("This invoice was cancelled on ${uiState.cancelDate} and can't be edited or reissued.", color = RedText, fontSize = 12.sp)
                    }
                }

                Spacer(modifier = Modifier.height(32.dp))

                // الأزرار السفلية بناءً على الحالة
                if (uiState.status == InvoiceStatus.PENDING) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Button(
                            onClick = onMarkAsPaid,
                            modifier = Modifier.weight(1f).height(50.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = DarkGreen),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Icon(Icons.Default.Check, contentDescription = null, tint = Color.White, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Mark as Paid", fontSize = 14.sp)
                        }

                        OutlinedButton(
                            onClick = { /* Share Logic */ },
                            modifier = Modifier.weight(0.4f).height(50.dp),
                            shape = RoundedCornerShape(12.dp),
                            border = BorderStroke(1.dp, BorderLight),
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.Black)
                        ) {
                            Icon(Icons.Outlined.Share, contentDescription = null, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Share", fontSize = 14.sp)
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    TextButton(
                        onClick = { onShowCancelDialog(true) },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Cancel Invoice", color = RedText, fontSize = 14.sp)
                    }
                } else if (uiState.status == InvoiceStatus.CANCELED) {
                    Button(
                        onClick = { /* Create New Logic */ },
                        modifier = Modifier.fillMaxWidth().height(50.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = DarkGreen),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Icon(Icons.Default.Add, contentDescription = null, tint = Color.White, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Create New Invoice For This Sale", fontSize = 14.sp)
                    }
                }
            }
        }

        // نافذة التأكيد للإلغاء (Dialog)
        if (uiState.showCancelDialog) {
            AlertDialog(
                onDismissRequest = { onShowCancelDialog(false) },
                containerColor = Color.White,
                icon = { Icon(Icons.Outlined.Warning, contentDescription = "Warning", tint = OrangeText) },
                title = { Text("Cancel this invoice?", fontWeight = FontWeight.Bold, fontSize = 18.sp) },
                text = {
                    Text(
                        "This can't be undone. Once cancelled, ${uiState.invoiceId} can never be reissued or edited — you'll need to create a brand new invoice for this sale if needed.",
                        color = GrayText,
                        fontSize = 14.sp,
                        textAlign = TextAlign.Center
                    )
                },
                confirmButton = {
                    OutlinedButton(
                        onClick = onConfirmCancel,
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = RedText),
                        border = BorderStroke(1.dp, RedText)
                    ) {
                        Text("Yes, Cancel It")
                    }
                },
                dismissButton = {
                    OutlinedButton(
                        onClick = { onShowCancelDialog(false) },
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.Black),
                        border = BorderStroke(1.dp, BorderLight)
                    ) {
                        Text("Keep Invoice")
                    }
                }
            )
        }
    }
}

// ============================== Previews ============================== //

@Preview(showBackground = true, device = "id:pixel_5", name = "1. Pending Invoice")
@Composable
fun InvoicePendingPreview() {
    MaterialTheme {
        InvoiceDetailContent(
            uiState = InvoiceDetailUiState(status = InvoiceStatus.PENDING),
            onShowCancelDialog = {}, onConfirmCancel = {}, onMarkAsPaid = {}, onNavigateBack = {}
        )
    }
}

@Preview(showBackground = true, device = "id:pixel_5", name = "2. Canceled Invoice")
@Composable
fun InvoiceCanceledPreview() {
    MaterialTheme {
        InvoiceDetailContent(
            uiState = InvoiceDetailUiState(status = InvoiceStatus.CANCELED, cancelDate = "Sep 4"),
            onShowCancelDialog = {}, onConfirmCancel = {}, onMarkAsPaid = {}, onNavigateBack = {}
        )
    }
}

@Preview(showBackground = true, device = "id:pixel_5", name = "3. Cancel Dialog")
@Composable
fun InvoiceDialogPreview() {
    MaterialTheme {
        InvoiceDetailContent(
            uiState = InvoiceDetailUiState(status = InvoiceStatus.PENDING, showCancelDialog = true),
            onShowCancelDialog = {}, onConfirmCancel = {}, onMarkAsPaid = {}, onNavigateBack = {}
        )
    }
}