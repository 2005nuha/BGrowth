package com.example.bgrowth.ui.adddebt

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel

val DarkGreen = Color(0xFF105942)
val BackgroundColor = Color(0xFFF9F9F9)
val BorderLight = Color(0xFFE5E5E5)
val GrayText = Color(0xFF888888)
val LightGreenBg = Color(0xFFEAF5EF)

@Composable
fun AddDebtScreen(
    viewModel: AddDebtViewModel = viewModel(),
    onNavigateBack: () -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsState()

    AddDebtContent(
        uiState = uiState,
        onDebtTypeChange = viewModel::onDebtTypeChange,
        onPartyNameChange = viewModel::onPartyNameChange,
        onAmountChange = viewModel::onAmountChange,
        onDueDateChange = viewModel::onDueDateChange,
        onNotesChange = viewModel::onNotesChange,
        onToggleDropdown = viewModel::toggleDropdown,
        onSaveDebt = viewModel::onSaveDebt,
        onNavigateBack = onNavigateBack
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddDebtContent(
    uiState: AddDebtUiState,
    onDebtTypeChange: (String) -> Unit,
    onPartyNameChange: (String) -> Unit,
    onAmountChange: (String) -> Unit,
    onDueDateChange: (String) -> Unit,
    onNotesChange: (String) -> Unit,
    onToggleDropdown: (Boolean) -> Unit,
    onSaveDebt: () -> Unit,
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
                Text("Add Debt", color = DarkGreen, fontWeight = FontWeight.Bold, fontSize = 20.sp)
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
                    colors = CardDefaults.cardColors(containerColor = Color.Transparent),
                    border = BorderStroke(1.dp, BorderLight),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        // Debt Type Segmented Buttons
                        Text("Debt Type", fontWeight = FontWeight.Medium, fontSize = 14.sp, color = Color.Black)
                        Spacer(modifier = Modifier.height(12.dp))
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                            DebtTypeButton(
                                title = "Owed to Me",
                                subtitle = "(a customer owes me)",
                                isSelected = uiState.debtType == "Owed to Me",
                                onClick = { onDebtTypeChange("Owed to Me") },
                                modifier = Modifier.weight(1f)
                            )
                            DebtTypeButton(
                                title = "I Owe",
                                subtitle = "(I owe a supplier)",
                                isSelected = uiState.debtType == "I Owe",
                                onClick = { onDebtTypeChange("I Owe") },
                                modifier = Modifier.weight(1f)
                            )
                        }

                        Spacer(modifier = Modifier.height(20.dp))

                        // Party Dropdown
                        Text("Party (Customer/Supplier)", fontWeight = FontWeight.Medium, fontSize = 14.sp, color = Color.Black)
                        Spacer(modifier = Modifier.height(8.dp))
                        ExposedDropdownMenuBox(
                            expanded = uiState.isDropdownExpanded,
                            onExpandedChange = onToggleDropdown
                        ) {
                            OutlinedTextField(
                                value = uiState.partyName,
                                onValueChange = onPartyNameChange,
                                placeholder = { Text("Sara Ahmed", color = Color.LightGray, fontSize = 14.sp) },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .menuAnchor(),
                                shape = RoundedCornerShape(12.dp),
                                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = uiState.isDropdownExpanded) },
                                colors = OutlinedTextFieldDefaults.colors(
                                    unfocusedBorderColor = BorderLight,
                                    focusedBorderColor = DarkGreen,
                                    unfocusedContainerColor = Color.Transparent,
                                    focusedContainerColor = Color.Transparent
                                ),
                                singleLine = true
                            )
                            ExposedDropdownMenu(
                                expanded = uiState.isDropdownExpanded,
                                onDismissRequest = { onToggleDropdown(false) },
                                modifier = Modifier.background(Color.White)
                            ) {
                                uiState.partiesList.forEach { selectionOption ->
                                    DropdownMenuItem(
                                        text = { Text(selectionOption, fontSize = 14.sp) },
                                        onClick = {
                                            onPartyNameChange(selectionOption)
                                            onToggleDropdown(false)
                                        }
                                    )
                                }
                                HorizontalDivider(color = BorderLight)
                                DropdownMenuItem(
                                    text = {
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            Icon(Icons.Default.Add, contentDescription = null, tint = DarkGreen, modifier = Modifier.size(18.dp))
                                            Spacer(modifier = Modifier.width(8.dp))
                                            Text("Add New", color = DarkGreen, fontSize = 14.sp, fontWeight = FontWeight.Medium)
                                        }
                                    },
                                    onClick = {
                                        onToggleDropdown(false)
                                    }
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(20.dp))

                        // Amount and Due Date
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text("Amount", fontWeight = FontWeight.Medium, fontSize = 14.sp, color = Color.Black)
                                Spacer(modifier = Modifier.height(8.dp))
                                OutlinedTextField(
                                    value = uiState.amount,
                                    onValueChange = onAmountChange,
                                    placeholder = { Text("00.0", color = Color.LightGray, fontSize = 14.sp) },
                                    modifier = Modifier.fillMaxWidth(),
                                    shape = RoundedCornerShape(12.dp),
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
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
                                Text("Due Date (optional)", fontWeight = FontWeight.Medium, fontSize = 14.sp, color = Color.Black)
                                Spacer(modifier = Modifier.height(8.dp))
                                OutlinedTextField(
                                    value = uiState.dueDate,
                                    onValueChange = onDueDateChange,
                                    placeholder = { Text("Select date", color = Color.LightGray, fontSize = 14.sp) },
                                    modifier = Modifier.fillMaxWidth(),
                                    shape = RoundedCornerShape(12.dp),
                                    readOnly = true, // نجعله للقراءة فقط حتى يتم برمجته ليفتح Calendar لاحقاً
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

                        Spacer(modifier = Modifier.height(20.dp))

                        // Notes
                        Text("Notes (optional)", fontWeight = FontWeight.Medium, fontSize = 14.sp, color = Color.Black)
                        Spacer(modifier = Modifier.height(8.dp))
                        OutlinedTextField(
                            value = uiState.notes,
                            onValueChange = onNotesChange,
                            placeholder = { Text("e.g. Owed for bulk order", color = Color.LightGray, fontSize = 14.sp) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(100.dp),
                            shape = RoundedCornerShape(12.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                unfocusedBorderColor = BorderLight,
                                focusedBorderColor = DarkGreen,
                                unfocusedContainerColor = Color.Transparent,
                                focusedContainerColor = Color.Transparent
                            )
                        )

                        Spacer(modifier = Modifier.height(24.dp))

                        // Save Button
                        Button(
                            onClick = onSaveDebt,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = DarkGreen),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Icon(Icons.Default.Add, contentDescription = "Save", tint = Color.White)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Save Debt", color = Color.White, fontSize = 15.sp, fontWeight = FontWeight.Medium)
                        }
                    }
                }
            }
        }
    }
}

// زر نوع الدين المخصص (Custom Segmented Button)
@Composable
fun DebtTypeButton(
    title: String,
    subtitle: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .height(60.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(if (isSelected) LightGreenBg else Color.Transparent)
            .border(
                width = 1.dp,
                color = if (isSelected) DarkGreen else BorderLight,
                shape = RoundedCornerShape(12.dp)
            )
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = title,
                fontWeight = FontWeight.Bold,
                fontSize = 13.sp,
                color = if (isSelected) DarkGreen else GrayText
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = subtitle,
                fontSize = 9.sp,
                color = if (isSelected) DarkGreen.copy(alpha = 0.8f) else GrayText.copy(alpha = 0.8f)
            )
        }
    }
}

// ============================== Previews ============================== //

@Preview(showBackground = true, device = "id:pixel_5", name = "Add Debt Screen")
@Composable
fun AddDebtScreenPreview() {
    MaterialTheme {
        AddDebtContent(
            uiState = AddDebtUiState(partyName = "Sara Ahmed"),
            onDebtTypeChange = {},
            onPartyNameChange = {},
            onAmountChange = {},
            onDueDateChange = {},
            onNotesChange = {},
            onToggleDropdown = {},
            onSaveDebt = {},
            onNavigateBack = {}
        )
    }
}


@Preview(showBackground = true, device = "id:pixel_5", name = "Add Debt (Dropdown Open)")
@Composable
fun AddDebtDropdownOpenPreview() {
    MaterialTheme {
        AddDebtContent(
            uiState = AddDebtUiState(
                partyName = "Sara Ahmed",
                isDropdownExpanded = true // 👈 هذا السطر هو الذي يفتح القائمة للمعاينة
            ),
            onDebtTypeChange = {},
            onPartyNameChange = {},
            onAmountChange = {},
            onDueDateChange = {},
            onNotesChange = {},
            onToggleDropdown = {},
            onSaveDebt = {},
            onNavigateBack = {}
        )
    }
}