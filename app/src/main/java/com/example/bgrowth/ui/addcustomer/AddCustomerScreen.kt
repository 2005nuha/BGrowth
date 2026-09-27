package com.example.bgrowth.ui.addcustomer

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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

@Composable
fun AddCustomerScreen(
    viewModel: AddCustomerViewModel = viewModel(),
    onNavigateBack: () -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsState()

    AddCustomerContent(
        uiState = uiState,
        onFullNameChange = viewModel::onFullNameChange,
        onPhoneNumberChange = viewModel::onPhoneNumberChange,
        onNotesChange = viewModel::onNotesChange,
        onSaveCustomer = viewModel::onSaveCustomer,
        onNavigateBack = onNavigateBack
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddCustomerContent(
    uiState: AddCustomerUiState,
    onFullNameChange: (String) -> Unit,
    onPhoneNumberChange: (String) -> Unit,
    onNotesChange: (String) -> Unit,
    onSaveCustomer: () -> Unit,
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
                Text("Add Customers", color = DarkGreen, fontWeight = FontWeight.Bold, fontSize = 20.sp)
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 20.dp)
        ) {
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
                    // Full Name Field
                    Text("Full Name", fontWeight = FontWeight.Medium, fontSize = 14.sp, color = Color.Black)
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = uiState.fullName,
                        onValueChange = onFullNameChange,
                        placeholder = { Text("e.g Sara Ahmed", color = Color.LightGray, fontSize = 14.sp) },
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

                    // Phone Number
                    Text("Phone Number", fontWeight = FontWeight.Medium, fontSize = 14.sp, color = Color.Black)
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = uiState.phoneNumber,
                        onValueChange = onPhoneNumberChange,
                        placeholder = { Text("05X XXX XXXX", color = Color.LightGray, fontSize = 14.sp) },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                        colors = OutlinedTextFieldDefaults.colors(
                            unfocusedBorderColor = BorderLight,
                            focusedBorderColor = DarkGreen,
                            unfocusedContainerColor = Color.Transparent,
                            focusedContainerColor = Color.Transparent
                        ),
                        singleLine = true
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Notes Field
                    Text("Notes (optional)", fontWeight = FontWeight.Medium, fontSize = 14.sp, color = Color.Black)
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = uiState.notes,
                        onValueChange = onNotesChange,
                        placeholder = { Text("Any additional info", color = Color.LightGray, fontSize = 14.sp) },
                        modifier = Modifier.fillMaxWidth().height(100.dp),
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
                        onClick = onSaveCustomer,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = DarkGreen),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Icon(Icons.Default.Add, contentDescription = "Add", tint = Color.White)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Save Customer", color = Color.White, fontSize = 15.sp, fontWeight = FontWeight.Medium)
                    }
                }
            }
        }
    }
}


@Preview(showBackground = true, device = "id:pixel_5", name = "Add Customer Screen")
@Composable
fun AddCustomerScreenPreview() {
    MaterialTheme {
        AddCustomerContent(
            uiState = AddCustomerUiState(),
            onFullNameChange = {},
            onPhoneNumberChange = {},
            onNotesChange = {},
            onSaveCustomer = {},
            onNavigateBack = {}
        )
    }
}