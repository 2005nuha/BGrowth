package com.example.bgrowth.ui.addexpence


import androidx.compose.material.icons.Icons
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ElectricBolt
import androidx.compose.material.icons.filled.Inventory2
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
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
fun AddExpenseScreen(
    viewModel: AddExpenseViewModel = viewModel(),
    onNavigateBack: () -> Unit = {},
    onViewAllClick: () -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsState()

    AddExpenseContent(
        uiState = uiState,
        onExpenseNameChange = viewModel::onExpenseNameChange,
        onCategoryChange = viewModel::onCategoryChange,
        onPriceChange = viewModel::onPriceChange,
        onDateChange = viewModel::onDateChange,
        onPaymentMethodChange = viewModel::onPaymentMethodChange,
        onToggleCategoryDropdown = viewModel::toggleCategoryDropdown,
        onTogglePaymentMethodDropdown = viewModel::togglePaymentMethodDropdown,
        onSaveExpense = viewModel::onSaveExpense,
        onNavigateBack = onNavigateBack,
        onViewAllClick = onViewAllClick
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddExpenseContent(
    uiState: AddExpenseUiState,
    onExpenseNameChange: (String) -> Unit,
    onCategoryChange: (String) -> Unit,
    onPriceChange: (String) -> Unit,
    onDateChange: (String) -> Unit,
    onPaymentMethodChange: (String) -> Unit,
    onToggleCategoryDropdown: () -> Unit,
    onTogglePaymentMethodDropdown: () -> Unit,
    onSaveExpense: () -> Unit,
    onNavigateBack: () -> Unit,
    onViewAllClick: () -> Unit
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
                IconButton(
                    onClick = onNavigateBack,
                    modifier = Modifier.size(24.dp)
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = DarkGreen
                    )
                }
                Spacer(modifier = Modifier.width(16.dp))
                Text(
                    text = "Add Expense",
                    color = DarkGreen,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp
                )
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
                // البطاقة الخاصة بنموذج الإضافة
                Card(
                    colors = CardDefaults.cardColors(containerColor = Color.Transparent),
                    border = BorderStroke(1.dp, BorderLight),
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        // حقل اسم المصروف
                        Text("Add Expense", fontWeight = FontWeight.Medium, fontSize = 14.sp, color = Color.Black)
                        Spacer(modifier = Modifier.height(8.dp))
                        OutlinedTextField(
                            value = uiState.expenseName,
                            onValueChange = onExpenseNameChange,
                            placeholder = { Text("e.g. Electricity Bill", color = Color.Gray, fontSize = 14.sp) },
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

                        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text("Category", fontWeight = FontWeight.Medium, fontSize = 14.sp, color = Color.Black)
                                Spacer(modifier = Modifier.height(8.dp))
                                CustomDropdownField(
                                    value = uiState.category,
                                    expanded = uiState.isCategoryDropdownExpanded,
                                    onExpandedChange = onToggleCategoryDropdown,
                                    onItemSelected = onCategoryChange,
                                    items = listOf("Utilities", "Supplies", "Transport", "Rent")
                                )
                            }
                            Column(modifier = Modifier.weight(1f)) {
                                Text("Price", fontWeight = FontWeight.Medium, fontSize = 14.sp, color = Color.Black)
                                Spacer(modifier = Modifier.height(8.dp))
                                OutlinedTextField(
                                    value = uiState.price,
                                    onValueChange = onPriceChange,
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
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
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text("Date", fontWeight = FontWeight.Medium, fontSize = 14.sp, color = Color.Black)
                                Spacer(modifier = Modifier.height(8.dp))
                                OutlinedTextField(
                                    value = uiState.date,
                                    onValueChange = onDateChange,
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
                            }
                            Column(modifier = Modifier.weight(1f)) {
                                Text("Payment method", fontWeight = FontWeight.Medium, fontSize = 14.sp, color = Color.Black)
                                Spacer(modifier = Modifier.height(8.dp))
                                CustomDropdownField(
                                    value = uiState.paymentMethod,
                                    expanded = uiState.isPaymentMethodDropdownExpanded,
                                    onExpandedChange = onTogglePaymentMethodDropdown,
                                    onItemSelected = onPaymentMethodChange,
                                    items = listOf("Cash", "Card", "Bank Transfer")
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(24.dp))

                        Button(
                            onClick = onSaveExpense,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = DarkGreen),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Icon(Icons.Default.Add, contentDescription = "Add", tint = Color.White)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Save Expense", color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Medium)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(32.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Recent Expenses",
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        color = Color.Black
                    )
                    TextButton(onClick = onViewAllClick, contentPadding = PaddingValues(0.dp)) {
                        Text(
                            text = "View all",
                            color = DarkGreen,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))
            }

            items(uiState.recentExpenses) { item ->
                RecentExpenseItemCard(item = item)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomDropdownField(
    value: String,
    expanded: Boolean,
    onExpandedChange: () -> Unit,
    onItemSelected: (String) -> Unit,
    items: List<String>
) {
    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { onExpandedChange() }
    ) {
        OutlinedTextField(
            value = value,
            onValueChange = {},
            readOnly = true,
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
            },
            modifier = Modifier
                .menuAnchor()
                .fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedBorderColor = BorderLight,
                focusedBorderColor = DarkGreen,
                unfocusedContainerColor = Color.Transparent,
                focusedContainerColor = Color.Transparent
            )
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = onExpandedChange
        ) {
            items.forEach { selectionOption ->
                DropdownMenuItem(
                    text = { Text(selectionOption) },
                    onClick = {
                        onItemSelected(selectionOption)
                        onExpandedChange()
                    }
                )
            }
        }
    }
}

@Composable
fun RecentExpenseItemCard(item: RecentExpenseItem) {
    val (icon, iconTint) = when (item.id) {
        1 -> Icons.Default.ElectricBolt to Color(0xFFFBC02D)
        2 -> Icons.Default.Inventory2 to Color(0xFF8D6E63)
        else -> Icons.Default.LocalShipping to Color(0xFFE53935)
    }

    Card(
        colors = CardDefaults.cardColors(containerColor = Color.Transparent),
        border = BorderStroke(1.dp, BorderLight),
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 12.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color(0xFFE8E8E8)),
                contentAlignment = Alignment.Center
            ) {
                Icon(imageVector = icon, contentDescription = null, tint = iconTint)
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = item.title,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 15.sp,
                    color = Color.Black
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = item.subtitle,
                    color = GrayText,
                    fontSize = 12.sp
                )
            }
            Text(
                text = item.amount,
                color = RedText,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp
            )
        }
    }
}

@Preview(showBackground = true, device = "id:pixel_5")
@Composable
fun AddExpenseScreenPreview() {
    val mockUiState = AddExpenseUiState(
        expenseName = "",
        category = "Utilities",
        price = "45",
        date = "Today",
        paymentMethod = "Cash",
        recentExpenses = listOf(
            RecentExpenseItem(1, "Electricity Bill", "Utilities · Today, 9:15 AM", "-$45.00"),
            RecentExpenseItem(2, "Packaging Supplies", "Supplies · Yesterday", "-$120.00"),
            RecentExpenseItem(3, "Delivery Fuel", "Transport · Yesterday", "-$16.00")
        )
    )

    MaterialTheme {
        AddExpenseContent(
            uiState = mockUiState,
            onExpenseNameChange = {},
            onCategoryChange = {},
            onPriceChange = {},
            onDateChange = {},
            onPaymentMethodChange = {},
            onToggleCategoryDropdown = {},
            onTogglePaymentMethodDropdown = {},
            onSaveExpense = {},
            onNavigateBack = {},
            onViewAllClick = {}
        )
    }
}