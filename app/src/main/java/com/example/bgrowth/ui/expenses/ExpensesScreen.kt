package com.example.bgrowth.ui.expenses


import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.Settings
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
val RedText = Color(0xFFD32F2F)
val GrayText = Color(0xFF888888)

@Composable
fun ExpensesScreen(
    viewModel: ExpensesViewModel = viewModel(),
    onNavigateBack: () -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsState()

    ExpensesContent(
        uiState = uiState,
        onSearchQueryChange = viewModel::onSearchQueryChange,
        onDateSelected = viewModel::onDateFilterSelected,
        onCategorySelected = viewModel::onCategoryFilterSelected,
        onNavigateBack = onNavigateBack,
        onSettingsClick = { /* Handle settings */ },
        onEditExpense = { expense -> /* إضافة كود الانتقال لشاشة التعديل هنا لاحقاً */ },
        onDeleteExpense = { expense -> /* إضافة كود دالة الحذف هنا لاحقاً */ }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExpensesContent(
    uiState: ExpensesUiState,
    onSearchQueryChange: (String) -> Unit,
    onDateSelected: (String) -> Unit,
    onCategorySelected: (String) -> Unit,
    onNavigateBack: () -> Unit,
    onSettingsClick: () -> Unit,
    onEditExpense: (ExpenseItem) -> Unit,
    onDeleteExpense: (ExpenseItem) -> Unit
) {
    Scaffold(
        containerColor = BackgroundColor,
        topBar = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 16.dp)
            ) {
                // Top Bar
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(bottom = 24.dp)
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
                        text = "History Expenses",
                        color = DarkGreen,
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp
                    )
                }

                // Search and Settings
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedTextField(
                        value = uiState.searchQuery,
                        onValueChange = onSearchQueryChange,
                        placeholder = { Text("Search by Expense or amount...", color = Color.Gray, fontSize = 14.sp) },
                        leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Search", tint = Color.Black) },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            unfocusedBorderColor = BorderLight,
                            focusedBorderColor = DarkGreen,
                            unfocusedContainerColor = Color.Transparent,
                            focusedContainerColor = Color.Transparent
                        ),
                        singleLine = true
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        border = BorderStroke(1.dp, BorderLight),
                        color = Color.Transparent,
                        modifier = Modifier.size(56.dp)
                    ) {
                        IconButton(onClick = onSettingsClick) {
                            Icon(Icons.Outlined.Settings, contentDescription = "Settings", tint = Color.Black)
                        }
                    }
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
                // Date Filters
                Text(
                    text = "Date",
                    color = GrayText,
                    fontSize = 15.sp,
                    modifier = Modifier.padding(bottom = 8.dp, top = 8.dp)
                )
                LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    items(uiState.dateOptions) { option ->
                        CustomChip(
                            text = option,
                            selected = uiState.selectedDate == option,
                            onClick = { onDateSelected(option) }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Category Filters
                Text(
                    text = "Category",
                    color = GrayText,
                    fontSize = 15.sp,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    items(uiState.categoryOptions) { option ->
                        CustomChip(
                            text = option,
                            selected = uiState.selectedCategory == option,
                            onClick = { onCategorySelected(option) }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Summary Cards
                Card(
                    colors = CardDefaults.cardColors(containerColor = Color.Transparent),
                    border = BorderStroke(1.dp, BorderLight),
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 20.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(
                            modifier = Modifier.weight(1f),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text("Total expenses", color = GrayText, fontSize = 13.sp)
                            Spacer(modifier = Modifier.height(8.dp))
                            Text("$${String.format("%.2f", uiState.totalExpenses)}", color = DarkGreen, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                        }
                        Box(
                            modifier = Modifier
                                .height(40.dp)
                                .width(1.dp)
                                .background(BorderLight)
                        )
                        Column(
                            modifier = Modifier.weight(1f),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text("Avg.expense", color = GrayText, fontSize = 13.sp)
                            Spacer(modifier = Modifier.height(8.dp))
                            Text("$${String.format("%.2f", uiState.avgExpense)}", color = DarkGreen, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
            }

            // Expense History List
            uiState.expenseGroups.forEach { group ->
                item {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = group.dateHeader,
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp,
                            color = Color.Black
                        )
                        Text(
                            text = group.totalAmount,
                            color = RedText,
                            fontWeight = FontWeight.Medium,
                            fontSize = 14.sp
                        )
                    }
                }

                items(group.items) { item ->
                    ExpenseItemCard(
                        item = item,
                        onEditClick = { onEditExpense(item) },
                        onDeleteClick = { onDeleteExpense(item) }
                    )
                }
            }
        }
    }
}

@Composable
fun CustomChip(text: String, selected: Boolean, onClick: () -> Unit) {
    Surface(
        shape = RoundedCornerShape(20.dp),
        color = if (selected) DarkGreen else Color.Transparent,
        border = if (selected) null else BorderStroke(1.dp, BorderLight),
        modifier = Modifier.clickable { onClick() }
    ) {
        Text(
            text = text,
            color = if (selected) Color.White else GrayText,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
            fontSize = 14.sp,
            fontWeight = if (selected) FontWeight.Medium else FontWeight.Normal
        )
    }
}

@Composable
fun ExpenseItemCard(
    item: ExpenseItem,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit
) {
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
                    .background(Color(0xFFE8E8E8))
            )
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

                Spacer(modifier = Modifier.height(8.dp))
                // أزرار التعديل والحذف الجديدة
                Row(verticalAlignment = Alignment.CenterVertically) {
                    // زر التعديل (Edit)
                    Row(
                        modifier = Modifier
                            .clip(RoundedCornerShape(4.dp))
                            .clickable { onEditClick() }
                            .padding(end = 12.dp, top = 4.dp, bottom = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Default.Edit, contentDescription = "Edit", tint = DarkGreen, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Edit", color = DarkGreen, fontSize = 13.sp, fontWeight = FontWeight.Medium)
                    }

                    // زر الحذف (Delete)
                    Row(
                        modifier = Modifier
                            .clip(RoundedCornerShape(4.dp))
                            .clickable { onDeleteClick() }
                            .padding(end = 8.dp, top = 4.dp, bottom = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Default.Delete, contentDescription = "Delete", tint = RedText, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Delete", color = RedText, fontSize = 13.sp, fontWeight = FontWeight.Medium)
                    }
                }
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
fun ExpensesScreenPreview() {
    val mockUiState = ExpensesUiState(
        searchQuery = "",
        selectedDate = "This week",
        selectedCategory = "All",
        dateOptions = listOf("This week", "Today", "This month"),
        categoryOptions = listOf("All", "Rent", "Supplies", "Utilities"),
        totalExpenses = 612.50,
        avgExpense = 22.50,
        expenseGroups = listOf(
            ExpenseGroup(
                dateHeader = "Today ,sep7",
                totalAmount = "-$45.00",
                items = listOf(
                    ExpenseItem("Electricity Bill", "Utilities · Today, 9:15 AM", "-$45.00")
                )
            ),
            ExpenseGroup(
                dateHeader = "Yesterday ,sep6",
                totalAmount = "-$120.00",
                items = listOf(
                    ExpenseItem("Packaging Supplies", "Supplies · Yesterday", "-$120.00"),
                    ExpenseItem("Delivery Fuel", "Transport · Yesterday", "-$16.00")
                )
            )
        )
    )

    MaterialTheme {
        ExpensesContent(
            uiState = mockUiState,
            onSearchQueryChange = {},
            onDateSelected = {},
            onCategorySelected = {},
            onNavigateBack = {},
            onSettingsClick = {},
            onEditExpense = {},
            onDeleteExpense = {}
        )
    }
}

@Preview(showBackground = true, device = "id:pixel_5", name = "2. Filtered: Today")
@Composable
fun ExpensesTodayPreview() {
    val mockUiState = ExpensesUiState(
        searchQuery = "",
        selectedDate = "Today", // هنا تغير الوقت
        selectedCategory = "All",
        dateOptions = listOf("This week", "Today", "This month"),
        categoryOptions = listOf("All", "Rent", "Supplies", "Utilities"),
        totalExpenses = 45.00,
        avgExpense = 45.00,
        expenseGroups = listOf(
            ExpenseGroup(
                dateHeader = "Today, Sep 7",
                totalAmount = "-$45.00",
                items = listOf(ExpenseItem("Electricity Bill", "Utilities · Today, 9:15 AM", "-$45.00"))
            )
        )
    )
    MaterialTheme { ExpensesContent(uiState = mockUiState, onSearchQueryChange = {}, onDateSelected = {}, onCategorySelected = {}, onNavigateBack = {}, onSettingsClick = {}, onEditExpense = {}, onDeleteExpense = {}) }
}

@Preview(showBackground = true, device = "id:pixel_5", name = "3. Filtered: Rent")
@Composable
fun ExpensesRentPreview() {
    val mockUiState = ExpensesUiState(
        searchQuery = "",
        selectedDate = "This month",
        selectedCategory = "Rent", // هنا تغير الصنف
        dateOptions = listOf("This week", "Today", "This month"),
        categoryOptions = listOf("All", "Rent", "Supplies", "Utilities"),
        totalExpenses = 500.00,
        avgExpense = 500.00,
        expenseGroups = listOf(
            ExpenseGroup(
                dateHeader = "Sep 1",
                totalAmount = "-$500.00",
                items = listOf(ExpenseItem("Shop Rent", "Rent · Sep 1", "-$500.00"))
            )
        )
    )
    MaterialTheme { ExpensesContent(uiState = mockUiState, onSearchQueryChange = {}, onDateSelected = {}, onCategorySelected = {}, onNavigateBack = {}, onSettingsClick = {}, onEditExpense = {}, onDeleteExpense = {}) }
}

@Preview(showBackground = true, device = "id:pixel_5", name = "4. Empty Results")
@Composable
fun ExpensesEmptyPreview() {
    val mockUiState = ExpensesUiState(
        searchQuery = "",
        selectedDate = "Today",
        selectedCategory = "Rent",
        dateOptions = listOf("This week", "Today", "This month"),
        categoryOptions = listOf("All", "Rent", "Supplies", "Utilities"),
        totalExpenses = 0.00,
        avgExpense = 0.00,
        expenseGroups = emptyList()
    )
    MaterialTheme { ExpensesContent(uiState = mockUiState, onSearchQueryChange = {}, onDateSelected = {}, onCategorySelected = {}, onNavigateBack = {}, onSettingsClick = {}, onEditExpense = {}, onDeleteExpense = {}) }
}