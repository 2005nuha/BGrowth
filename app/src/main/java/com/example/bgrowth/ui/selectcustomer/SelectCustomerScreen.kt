package com.example.bgrowth.ui.selectcustomer

import androidx.compose.ui.graphics.PathEffect
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
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel

val DarkGreen = Color(0xFF105942)
val BackgroundColor = Color(0xFFF9F9F9)
val BorderLight = Color(0xFFE5E5E5)
val GrayText = Color(0xFF888888)

@Composable
fun SelectCustomerScreen(
    viewModel: SelectCustomerViewModel = viewModel(),
    onNavigateBack: () -> Unit = {},
    onAddNewCustomerClick: () -> Unit = {},
    onContinueClick: (String) -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsState()

    SelectCustomerContent(
        uiState = uiState,
        onSearchQueryChange = viewModel::onSearchQueryChange,
        onCustomerSelected = viewModel::onCustomerSelected,
        onNavigateBack = onNavigateBack,
        onAddNewCustomerClick = onAddNewCustomerClick,
        onContinueClick = onContinueClick
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SelectCustomerContent(
    uiState: SelectCustomerUiState,
    onSearchQueryChange: (String) -> Unit,
    onCustomerSelected: (String) -> Unit,
    onNavigateBack: () -> Unit,
    onAddNewCustomerClick: () -> Unit,
    onContinueClick: (String) -> Unit
) {
    Scaffold(
        containerColor = BackgroundColor,
        topBar = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 24.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(onClick = onNavigateBack, modifier = Modifier.size(24.dp)) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = DarkGreen)
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    Text("Select Customer", color = DarkGreen, fontWeight = FontWeight.Bold, fontSize = 20.sp)
                }

                Spacer(modifier = Modifier.height(24.dp))

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
        bottomBar = {
            // زر المتابعة في الأسفل
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(BackgroundColor)
                    .padding(horizontal = 20.dp, vertical = 16.dp)
            ) {
                Button(
                    onClick = {
                        uiState.selectedCustomerId?.let { onContinueClick(it) }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = DarkGreen),
                    shape = RoundedCornerShape(12.dp),
                    enabled = uiState.selectedCustomerId != null
                ) {
                    Text("Continue", color = Color.White, fontSize = 15.sp, fontWeight = FontWeight.Medium)
                    Spacer(modifier = Modifier.width(8.dp))
                    Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = "Continue", tint = Color.White, modifier = Modifier.size(18.dp))
                }
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
                // زر إضافة عميل جديد بالحدود المتقطعة (Dashed Border)
                val strokeColor = DarkGreen
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 24.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .clickable { onAddNewCustomerClick() }
                        .drawBehind {
                            drawRoundRect(
                                color = strokeColor,
                                style = Stroke(
                                    width = 1.dp.toPx(),
                                    pathEffect = PathEffect.dashPathEffect(floatArrayOf(15f, 15f), 0f)
                                ),
                                cornerRadius = CornerRadius(12.dp.toPx())
                            )
                        }
                        .padding(vertical = 16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Add, contentDescription = "Add", tint = DarkGreen, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Add New Customer", color = DarkGreen, fontWeight = FontWeight.Medium, fontSize = 14.sp)
                    }
                }
            }

            // قائمة العملاء للاختيار
            items(uiState.customers) { customer ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 12.dp)
                        .clickable { onCustomerSelected(customer.id) },
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    border = BorderStroke(1.dp, BorderLight),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // الدائرة الرمادية للاحرف
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape)
                                .background(BorderLight),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(text = customer.initials, fontWeight = FontWeight.Bold, color = Color.Black)
                        }

                        Spacer(modifier = Modifier.width(16.dp))

                        // معلومات العميل
                        Column(modifier = Modifier.weight(1f)) {
                            Text(text = customer.name, fontWeight = FontWeight.Bold, fontSize = 15.sp, color = Color.Black)
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(text = customer.phone, color = GrayText, fontSize = 12.sp)
                        }

                        // زر الراديو للاختيار
                        RadioButton(
                            selected = uiState.selectedCustomerId == customer.id,
                            onClick = { onCustomerSelected(customer.id) },
                            colors = RadioButtonDefaults.colors(selectedColor = DarkGreen, unselectedColor = GrayText)
                        )
                    }
                }
            }
        }
    }
}

// ============================== Previews ============================== //

@Preview(showBackground = true, device = "id:pixel_5", name = "Select Customer Screen")
@Composable
fun SelectCustomerScreenPreview() {
    val mockUiState = SelectCustomerUiState(
        customers = listOf(
            CustomerSelectionItem("1", "Sara Ahmed", "059 123 4567", "SA"),
            CustomerSelectionItem("2", "Mohammed Khalil", "059 987 6543", "MK"),
            CustomerSelectionItem("3", "Rana Nasser", "059 123 4567", "RN")
        ),
        selectedCustomerId = "1"
    )
    MaterialTheme {
        SelectCustomerContent(
            uiState = mockUiState,
            onSearchQueryChange = {},
            onCustomerSelected = {},
            onNavigateBack = {},
            onAddNewCustomerClick = {},
            onContinueClick = {}
        )
    }
}