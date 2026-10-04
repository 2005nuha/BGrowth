package com.example.bgrowth.ui.customers


import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
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
val RedText = Color(0xFFD32F2F)
val GrayText = Color(0xFF888888)

@Composable
fun CustomersScreen(
    viewModel: CustomersViewModel = viewModel(),
    onNavigateBack: () -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsState()

    CustomersContent(
        uiState = uiState,
        onSearchQueryChange = viewModel::onSearchQueryChange,
        onNavigateBack = onNavigateBack
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomersContent(
    uiState: CustomersUiState,
    onSearchQueryChange: (String) -> Unit,
    onNavigateBack: () -> Unit
) {
    Scaffold(
        containerColor = BackgroundColor,
        floatingActionButton = {
            FloatingActionButton(
                onClick = { /* التنقل لشاشة الإضافة لاحقاً */ },
                containerColor = DarkGreen,
                shape = CircleShape
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add Customer", tint = Color.White)
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // الشريط العلوي والنص الفرعي
            Column(modifier = Modifier.padding(horizontal = 20.dp, vertical = 24.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(onClick = onNavigateBack, modifier = Modifier.size(24.dp)) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = DarkGreen)
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    Text("Customers", color = DarkGreen, fontWeight = FontWeight.Bold, fontSize = 20.sp)
                }
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Manage your customer relationships",
                    color = GrayText,
                    fontSize = 13.sp,
                    modifier = Modifier.padding(start = 40.dp)
                )
            }

            if (uiState.isEmpty) {
                EmptyCustomersView()
            } else {
                // مربع البحث وزر الإعدادات
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    OutlinedTextField(
                        value = uiState.searchQuery,
                        onValueChange = onSearchQueryChange,
                        placeholder = { Text("Search customers...", color = Color.LightGray, fontSize = 14.sp) },
                        leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Search", tint = Color.Gray) },
                        modifier = Modifier
                            .weight(1f)
                            .height(50.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            unfocusedBorderColor = BorderLight,
                            focusedBorderColor = DarkGreen,
                            unfocusedContainerColor = Color.White,
                            focusedContainerColor = Color.White
                        ),
                        singleLine = true
                    )

                    Card(
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        border = BorderStroke(1.dp, BorderLight),
                        modifier = Modifier.size(50.dp)
                    ) {
                        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            Icon(Icons.Default.Settings, contentDescription = "Filter", tint = Color.Black)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // قائمة العملاء
                LazyColumn(
                    contentPadding = PaddingValues(start = 20.dp, end = 20.dp, bottom = 80.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(uiState.customers) { customer ->
                        CustomerItemCard(customer = customer)
                    }
                }
            }
        }
    }
}

@Composable
fun CustomerItemCard(customer: CustomerItem) {
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
            // الأيقونة الدائرية (الأحرف)
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color(0xFFE8E8E8)),
                contentAlignment = Alignment.Center
            ) {
                Text(customer.initials, fontWeight = FontWeight.Bold, fontSize = 16.sp, color = Color.Black)
            }

            Spacer(modifier = Modifier.width(16.dp))

            // الاسم ورقم الجوال
            Column(modifier = Modifier.weight(1f)) {
                Text(customer.name, fontWeight = FontWeight.Bold, fontSize = 14.sp, color = Color.Black)
                Spacer(modifier = Modifier.height(4.dp))
                Text(customer.phone, fontSize = 12.sp, color = GrayText)
            }

            // الديون
            Text(
                text = customer.balance,
                fontWeight = FontWeight.SemiBold,
                fontSize = 12.sp,
                color = if (customer.hasBalance) RedText else Color.LightGray
            )
        }
    }
}

@Composable
fun EmptyCustomersView() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier
                .size(80.dp)
                .clip(CircleShape)
                .background(Color(0xFFEAF5EF)),
            contentAlignment = Alignment.Center
        ) {
            Icon(Icons.Default.People, contentDescription = null, tint = DarkGreen, modifier = Modifier.size(32.dp))
        }
        Spacer(modifier = Modifier.height(24.dp))
        Text("No customers yet", fontWeight = FontWeight.Bold, fontSize = 20.sp, color = DarkGreen)
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            "Add your first customer to start tracking their purchases, debts, and invoices.",
            fontSize = 14.sp,
            color = Color.Gray,
            textAlign = TextAlign.Center
        )
    }
}


@Preview(showBackground = true, device = "id:pixel_5", name = "1. Customers List")
@Composable
fun CustomersListPreview() {
    val mockUiState = CustomersUiState(
        isEmpty = false,
        customers = listOf(
            CustomerItem(1, "SA", "Sara Ahmed", "059 123 4567", "$45.00 due", true),
            CustomerItem(2, "MK", "Mohammed Khalil", "059 123 4567", "No balance", false),
            CustomerItem(3, "RN", "Rana Nasser", "059 123 4567", "$120.00 due", true)
        )
    )
    MaterialTheme { CustomersContent(uiState = mockUiState, onSearchQueryChange = {}, onNavigateBack = {}) }
}

@Preview(showBackground = true, device = "id:pixel_5", name = "2. Customers Empty")
@Composable
fun CustomersEmptyPreview() {
    val mockUiState = CustomersUiState(isEmpty = true)
    MaterialTheme { CustomersContent(uiState = mockUiState, onSearchQueryChange = {}, onNavigateBack = {}) }
}