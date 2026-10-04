package com.example.bgrowth.ui.invoices

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
import androidx.compose.material.icons.filled.Block
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.Description
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel

val DarkGreen = Color(0xFF105942)
val BackgroundColor = Color(0xFFF9F9F9)
val BorderLight = Color(0xFFE5E5E5)
val GrayText = Color(0xFF888888)
val GreenText = Color(0xFF388E3C)
val OrangeText = Color(0xFFF57C00)
val RedIcon = Color(0xFFD32F2F)

@Composable
fun InvoicesScreen(
    viewModel: InvoicesViewModel = viewModel(),
    onNavigateBack: () -> Unit = {},
    onAddInvoiceClick: () -> Unit = {},
    onInvoiceClick: (String) -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsState()

    InvoicesContent(
        uiState = uiState,
        onSearchQueryChange = viewModel::onSearchQueryChange,
        onNavigateBack = onNavigateBack,
        onAddInvoiceClick = onAddInvoiceClick,
        onInvoiceClick = onInvoiceClick
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InvoicesContent(
    uiState: InvoicesUiState,
    onSearchQueryChange: (String) -> Unit,
    onNavigateBack: () -> Unit,
    onAddInvoiceClick: () -> Unit,
    onInvoiceClick: (String) -> Unit
) {
    Scaffold(
        containerColor = BackgroundColor,
        topBar = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 24.dp)
            ) {
                // شريط العنوان
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(onClick = onNavigateBack, modifier = Modifier.size(24.dp)) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = DarkGreen)
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    Text("Invoices", color = DarkGreen, fontWeight = FontWeight.Bold, fontSize = 20.sp)
                }

                Spacer(modifier = Modifier.height(24.dp))

                // حقل البحث مع زر الإعدادات
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedTextField(
                        value = uiState.searchQuery,
                        onValueChange = onSearchQueryChange,
                        placeholder = { Text("Search customers...", color = Color.Gray, fontSize = 14.sp) },
                        leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Search", tint = Color.Black) },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            unfocusedBorderColor = BorderLight,
                            focusedBorderColor = DarkGreen,
                            unfocusedContainerColor = Color.White,
                            focusedContainerColor = Color.White
                        ),
                        singleLine = true
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        border = BorderStroke(1.dp, BorderLight),
                        color = Color.White,
                        modifier = Modifier.size(56.dp)
                    ) {
                        IconButton(onClick = { /* Handle settings */ }) {
                            Icon(Icons.Outlined.Settings, contentDescription = "Settings", tint = Color.Black)
                        }
                    }
                }
            }
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onAddInvoiceClick,
                containerColor = DarkGreen,
                contentColor = Color.White,
                shape = CircleShape
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add Invoice")
            }
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 20.dp),
            contentPadding = PaddingValues(bottom = 80.dp) // لضمان عدم تغطية الـ FAB للعناصر
        ) {
            items(uiState.invoices) { invoice ->
                InvoiceCard(invoice = invoice, onClick = { onInvoiceClick(invoice.id) })
            }
        }
    }
}

@Composable
fun InvoiceCard(invoice: InvoiceItem, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 12.dp)
            .clickable { onClick() },
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(1.dp, BorderLight),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // أيقونة الفاتورة أو رمز الإلغاء
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(BackgroundColor),
                contentAlignment = Alignment.Center
            ) {
                if (invoice.status == InvoiceStatus.CANCELED) {
                    Icon(Icons.Default.Block, contentDescription = "Canceled", tint = RedIcon, modifier = Modifier.size(20.dp))
                } else {
                    Icon(Icons.Outlined.Description, contentDescription = "Invoice", tint = GrayText, modifier = Modifier.size(20.dp))
                }
            }

            Spacer(modifier = Modifier.width(16.dp))

            // تفاصيل الفاتورة
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = invoice.id,
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp,
                    color = if (invoice.status == InvoiceStatus.CANCELED) GrayText else Color.Black
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "${invoice.customerName} - ${invoice.date}",
                    color = GrayText,
                    fontSize = 12.sp
                )
            }

            // السعر والحالة (مع خط يتوسط السعر إذا كانت ملغاة)
            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = invoice.amount,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    color = if (invoice.status == InvoiceStatus.CANCELED) GrayText else Color.Black,
                    textDecoration = if (invoice.status == InvoiceStatus.CANCELED) TextDecoration.LineThrough else null
                )
                Spacer(modifier = Modifier.height(4.dp))

                val statusColor = when (invoice.status) {
                    InvoiceStatus.PAID -> GreenText
                    InvoiceStatus.PENDING -> OrangeText
                    InvoiceStatus.CANCELED -> GrayText
                }
                Text(
                    text = invoice.status.label,
                    color = statusColor,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}

// ============================== Previews ============================== //

@Preview(showBackground = true, device = "id:pixel_5", name = "Invoices List Screen")
@Composable
fun InvoicesScreenPreview() {
    // تعريف البيانات الوهمية مباشرة هنا لتجنب أخطاء بناء الـ ViewModel
    val mockUiState = InvoicesUiState(
        invoices = listOf(
            InvoiceItem("INV-0143", "Sara Ahmed", "Sep 9", "$35.00", InvoiceStatus.PAID),
            InvoiceItem("INV-0142", "Mohammed Khalil", "Sep 8", "$90.00", InvoiceStatus.PENDING),
            InvoiceItem("INV-0141", "Rana Nasser", "Sep 3", "$60.00", InvoiceStatus.CANCELED)
        )
    )

    MaterialTheme {
        InvoicesContent(
            uiState = mockUiState,
            onSearchQueryChange = {},
            onNavigateBack = {},
            onAddInvoiceClick = {},
            onInvoiceClick = {}
        )
    }
}