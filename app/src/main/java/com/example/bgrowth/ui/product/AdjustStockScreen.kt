package com.example.bgrowth.ui.product

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel

private val StockPrimary = Color(0xFF0F5D46)
private val StockBorder = Color(0xFFD9E3DA)
private val StockError = Color(0xFFD9534F)

@Composable
fun AdjustStockScreen(
    productId: Int,
    onBackClick: () -> Unit,
    onStockUpdated: () -> Unit,
    viewModel: AdjustStockViewModel = viewModel()
) {

    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(productId) {
        viewModel.loadProduct(productId)
    }

    LaunchedEffect(uiState.isStockUpdated) {

        if (uiState.isStockUpdated) {

            viewModel.consumeStockUpdated()

            onStockUpdated()
        }
    }

    AdjustStockContent(
        uiState = uiState,
        onBackClick = onBackClick,
        onMovementTypeChange =
            viewModel::onMovementTypeChange,
        onQuantityChange =
            viewModel::onQuantityChange,
        onReasonChange =
            viewModel::onReasonChange,
        onSaveClick =
            viewModel::adjustStock
    )
}

@Composable
private fun AdjustStockContent(
    uiState: AdjustStockUiState,
    onBackClick: () -> Unit,
    onMovementTypeChange: (StockMovementType) -> Unit,
    onQuantityChange: (String) -> Unit,
    onReasonChange: (String) -> Unit,
    onSaveClick: () -> Unit
) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(
                rememberScrollState()
            )
            .padding(bottom = 24.dp)
    ) {

        AdjustStockHeader(
            enabled =
                !uiState.isLoading &&
                        !uiState.isSaving,
            onBackClick = onBackClick
        )

        when {

            uiState.isLoading -> {

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 120.dp),
                    contentAlignment =
                        Alignment.Center
                ) {

                    CircularProgressIndicator(
                        color = StockPrimary
                    )
                }
            }

            else -> {

                Spacer(
                    modifier =
                        Modifier.height(18.dp)
                )

                CurrentStockCard(
                    productName =
                        uiState.productName,
                    currentQuantity =
                        uiState.currentQuantity
                )

                Spacer(
                    modifier =
                        Modifier.height(16.dp)
                )

                StockMovementCard(
                    uiState = uiState,
                    onMovementTypeChange =
                        onMovementTypeChange,
                    onQuantityChange =
                        onQuantityChange,
                    onReasonChange =
                        onReasonChange
                )

                uiState.errorMessage?.let { message ->

                    Spacer(
                        modifier =
                            Modifier.height(12.dp)
                    )

                    Text(
                        text = message,
                        color = StockError,
                        modifier =
                            Modifier.padding(
                                horizontal = 16.dp
                            )
                    )
                }

                Spacer(
                    modifier =
                        Modifier.height(24.dp)
                )

                ActionButtons(
                    isSaving =
                        uiState.isSaving,
                    onCancelClick =
                        onBackClick,
                    onSaveClick =
                        onSaveClick
                )
            }
        }
    }
}

@Composable
private fun AdjustStockHeader(
    enabled: Boolean,
    onBackClick: () -> Unit
) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                start = 8.dp,
                end = 16.dp,
                top = 12.dp
            ),
        verticalAlignment =
            Alignment.CenterVertically
    ) {

        IconButton(
            onClick = onBackClick,
            enabled = enabled
        ) {

            Icon(
                imageVector =
                    Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Back",
                tint = StockPrimary
            )
        }

        Column {

            Text(
                text = "Adjust Stock",
                color = StockPrimary,
                fontWeight = FontWeight.Bold,
                fontSize = 22.sp
            )

            Text(
                text = "Update product inventory",
                color =
                    MaterialTheme.colorScheme
                        .onSurfaceVariant,
                fontSize = 12.sp
            )
        }
    }
}

@Composable
private fun CurrentStockCard(
    productName: String,
    currentQuantity: Int
) {

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        shape =
            RoundedCornerShape(16.dp),
        border =
            BorderStroke(
                1.dp,
                StockBorder
            ),
        colors =
            CardDefaults.cardColors(
                containerColor = Color.White
            )
    ) {

        Column(
            modifier =
                Modifier.padding(18.dp)
        ) {

            Text(
                text =
                    productName.ifBlank {
                        "Product"
                    },
                fontWeight = FontWeight.SemiBold,
                fontSize = 17.sp
            )

            Spacer(
                modifier =
                    Modifier.height(12.dp)
            )

            Text(
                text = "Current Stock",
                color =
                    MaterialTheme.colorScheme
                        .onSurfaceVariant,
                fontSize = 12.sp
            )

            Text(
                text =
                    currentQuantity.toString(),
                color = StockPrimary,
                fontWeight = FontWeight.Bold,
                fontSize = 32.sp
            )

            Text(
                text = "units",
                color =
                    MaterialTheme.colorScheme
                        .onSurfaceVariant,
                fontSize = 12.sp
            )
        }
    }
}

@Composable
private fun StockMovementCard(
    uiState: AdjustStockUiState,
    onMovementTypeChange: (StockMovementType) -> Unit,
    onQuantityChange: (String) -> Unit,
    onReasonChange: (String) -> Unit
) {

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        shape =
            RoundedCornerShape(16.dp),
        border =
            BorderStroke(
                1.dp,
                StockBorder
            ),
        colors =
            CardDefaults.cardColors(
                containerColor = Color.White
            )
    ) {

        Column(
            modifier =
                Modifier.padding(16.dp)
        ) {

            Text(
                text = "Adjustment Type",
                fontWeight = FontWeight.Bold,
                fontSize = 15.sp
            )

            Spacer(
                modifier =
                    Modifier.height(12.dp)
            )

            MovementTypeButton(
                title = "Stock In",
                description =
                    "Add units to the current stock",
                selected =
                    uiState.movementType ==
                            StockMovementType.IN,
                enabled =
                    !uiState.isSaving,
                onClick = {
                    onMovementTypeChange(
                        StockMovementType.IN
                    )
                }
            )

            Spacer(
                modifier =
                    Modifier.height(8.dp)
            )

            MovementTypeButton(
                title = "Stock Out",
                description =
                    "Remove units from the current stock",
                selected =
                    uiState.movementType ==
                            StockMovementType.OUT,
                enabled =
                    !uiState.isSaving,
                onClick = {
                    onMovementTypeChange(
                        StockMovementType.OUT
                    )
                }
            )

            Spacer(
                modifier =
                    Modifier.height(8.dp)
            )

            MovementTypeButton(
                title = "Set Quantity",
                description =
                    "Set stock to an exact quantity",
                selected =
                    uiState.movementType ==
                            StockMovementType.ADJUSTMENT,
                enabled =
                    !uiState.isSaving,
                onClick = {
                    onMovementTypeChange(
                        StockMovementType.ADJUSTMENT
                    )
                }
            )

            Spacer(
                modifier =
                    Modifier.height(18.dp)
            )

            OutlinedTextField(
                value = uiState.quantity,
                onValueChange =
                    onQuantityChange,
                modifier =
                    Modifier.fillMaxWidth(),
                label = {

                    Text(
                        when (
                            uiState.movementType
                        ) {

                            StockMovementType.IN ->
                                "Quantity to Add"

                            StockMovementType.OUT ->
                                "Quantity to Remove"

                            StockMovementType.ADJUSTMENT ->
                                "New Stock Quantity"
                        }
                    )
                },
                keyboardOptions =
                    KeyboardOptions(
                        keyboardType =
                            KeyboardType.Number
                    ),
                singleLine = true,
                enabled =
                    !uiState.isSaving,
                isError =
                    uiState.quantityError != null,
                supportingText = {

                    uiState.quantityError?.let {
                        Text(it)
                    }
                },
                shape =
                    RoundedCornerShape(12.dp)
            )

            Spacer(
                modifier =
                    Modifier.height(10.dp)
            )

            OutlinedTextField(
                value = uiState.reason,
                onValueChange =
                    onReasonChange,
                modifier =
                    Modifier.fillMaxWidth(),
                label = {
                    Text("Reason")
                },
                placeholder = {
                    Text(
                        "Optional reason for adjustment"
                    )
                },
                supportingText = {

                    Text(
                        "${uiState.reason.length}/255"
                    )
                },
                minLines = 3,
                maxLines = 4,
                enabled =
                    !uiState.isSaving,
                shape =
                    RoundedCornerShape(12.dp)
            )
        }
    }
}

@Composable
private fun MovementTypeButton(
    title: String,
    description: String,
    selected: Boolean,
    enabled: Boolean,
    onClick: () -> Unit
) {

    OutlinedButton(
        onClick = onClick,
        enabled = enabled,
        modifier =
            Modifier.fillMaxWidth(),
        shape =
            RoundedCornerShape(12.dp),
        border =
            BorderStroke(
                width =
                    if (selected) {
                        2.dp
                    } else {
                        1.dp
                    },
                color =
                    if (selected) {
                        StockPrimary
                    } else {
                        StockBorder
                    }
            ),
        colors =
            ButtonDefaults
                .outlinedButtonColors(
                    containerColor =
                        if (selected) {
                            StockPrimary.copy(
                                alpha = 0.08f
                            )
                        } else {
                            Color.Transparent
                        },
                    contentColor =
                        StockPrimary
                )
    ) {

        Column(
            modifier =
                Modifier.fillMaxWidth()
        ) {

            Text(
                text = title,
                fontWeight =
                    FontWeight.SemiBold
            )

            Text(
                text = description,
                color =
                    MaterialTheme.colorScheme
                        .onSurfaceVariant,
                fontSize = 11.sp
            )
        }
    }
}

@Composable
private fun ActionButtons(
    isSaving: Boolean,
    onCancelClick: () -> Unit,
    onSaveClick: () -> Unit
) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        horizontalArrangement =
            Arrangement.spacedBy(10.dp)
    ) {

        OutlinedButton(
            onClick = onCancelClick,
            enabled = !isSaving,
            modifier = Modifier
                .weight(1f)
                .height(52.dp),
            shape =
                RoundedCornerShape(14.dp),
            border =
                BorderStroke(
                    1.dp,
                    StockBorder
                )
        ) {

            Text("Cancel")
        }

        Button(
            onClick = onSaveClick,
            enabled = !isSaving,
            modifier = Modifier
                .weight(2f)
                .height(52.dp),
            shape =
                RoundedCornerShape(14.dp),
            colors =
                ButtonDefaults.buttonColors(
                    containerColor =
                        StockPrimary
                )
        ) {

            if (isSaving) {

                CircularProgressIndicator(
                    modifier =
                        Modifier.height(22.dp),
                    strokeWidth = 2.dp,
                    color = Color.White
                )

            } else {

                Text("Update Stock")
            }
        }
    }
}