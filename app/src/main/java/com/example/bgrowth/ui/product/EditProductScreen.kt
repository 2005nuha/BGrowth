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
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel

private val EditProductPrimary = Color(0xFF0F5D46)
private val EditProductSurface = Color.White
private val EditProductBorder = Color(0xFFD9E3DA)
private val EditProductError = Color(0xFFD9534F)

@Composable
fun EditProductScreen(
    productId: Int,
    onBackClick: () -> Unit,
    onProductUpdated: () -> Unit,
    viewModel: EditProductViewModel = viewModel()
) {

    val uiState by
    viewModel.uiState.collectAsState()

    LaunchedEffect(productId) {
        viewModel.loadProduct(
            productId = productId
        )
    }

    LaunchedEffect(
        uiState.isProductUpdated
    ) {

        if (uiState.isProductUpdated) {

            viewModel.consumeProductUpdated()

            onProductUpdated()
        }
    }

    EditProductContent(
        uiState = uiState,
        onBackClick = onBackClick,
        onProductNameChange =
            viewModel::onProductNameChange,
        onCategorySelected =
            viewModel::onCategorySelected,
        onDescriptionChange =
            viewModel::onDescriptionChange,
        onPriceChange =
            viewModel::onPriceChange,
        onCostChange =
            viewModel::onCostChange,
        onMinStockLevelChange =
            viewModel::onMinStockLevelChange,
        onUpdateProduct =
            viewModel::updateProduct
    )
}

@Composable
private fun EditProductContent(
    uiState: EditProductUiState,
    onBackClick: () -> Unit,
    onProductNameChange: (String) -> Unit,
    onCategorySelected: (CategoryOption?) -> Unit,
    onDescriptionChange: (String) -> Unit,
    onPriceChange: (String) -> Unit,
    onCostChange: (String) -> Unit,
    onMinStockLevelChange: (String) -> Unit,
    onUpdateProduct: () -> Unit
) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(
                rememberScrollState()
            )
            .padding(bottom = 24.dp)
    ) {

        EditProductHeader(
            onBackClick = onBackClick,
            enabled =
                !uiState.isLoading &&
                        !uiState.isSaving
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
                        color = EditProductPrimary
                    )
                }
            }

            else -> {

                Spacer(
                    modifier =
                        Modifier.height(14.dp)
                )

                EditBasicInformationCard(
                    uiState = uiState,
                    onProductNameChange =
                        onProductNameChange,
                    onCategorySelected =
                        onCategorySelected,
                    onDescriptionChange =
                        onDescriptionChange
                )

                Spacer(
                    modifier =
                        Modifier.height(14.dp)
                )

                EditPriceAndStockCard(
                    uiState = uiState,
                    onPriceChange =
                        onPriceChange,
                    onCostChange =
                        onCostChange,
                    onMinStockLevelChange =
                        onMinStockLevelChange
                )

                uiState.errorMessage?.let {
                        message ->

                    Spacer(
                        modifier =
                            Modifier.height(12.dp)
                    )

                    Text(
                        text = message,
                        color = EditProductError,
                        modifier =
                            Modifier.padding(
                                horizontal = 16.dp
                            )
                    )
                }

                Spacer(
                    modifier =
                        Modifier.height(20.dp)
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            horizontal = 16.dp
                        ),
                    horizontalArrangement =
                        Arrangement.spacedBy(
                            10.dp
                        )
                ) {

                    OutlinedButton(
                        onClick = onBackClick,
                        enabled =
                            !uiState.isSaving,
                        modifier = Modifier
                            .weight(1f)
                            .height(52.dp),
                        shape =
                            RoundedCornerShape(
                                14.dp
                            ),
                        border =
                            BorderStroke(
                                1.dp,
                                EditProductBorder
                            )
                    ) {

                        Text("Cancel")
                    }

                    Button(
                        onClick =
                            onUpdateProduct,
                        enabled =
                            !uiState.isSaving,
                        modifier = Modifier
                            .weight(2f)
                            .height(52.dp),
                        shape =
                            RoundedCornerShape(
                                14.dp
                            ),
                        colors =
                            ButtonDefaults
                                .buttonColors(
                                    containerColor =
                                        EditProductPrimary
                                )
                    ) {

                        if (uiState.isSaving) {

                            CircularProgressIndicator(
                                strokeWidth = 2.dp,
                                color = Color.White,
                                modifier =
                                    Modifier.height(
                                        22.dp
                                    )
                            )

                        } else {

                            Text(
                                text =
                                    "Save Changes"
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun EditProductHeader(
    onBackClick: () -> Unit,
    enabled: Boolean
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
                tint = EditProductPrimary
            )
        }

        Column {

            Text(
                text = "Edit Product",
                color = EditProductPrimary,
                fontWeight = FontWeight.Bold,
                fontSize = 22.sp
            )

            Text(
                text =
                    "Update product information",
                color =
                    MaterialTheme
                        .colorScheme
                        .onSurfaceVariant,
                fontSize = 12.sp
            )
        }
    }
}

@Composable
private fun EditBasicInformationCard(
    uiState: EditProductUiState,
    onProductNameChange: (String) -> Unit,
    onCategorySelected: (CategoryOption?) -> Unit,
    onDescriptionChange: (String) -> Unit
) {

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        shape =
            RoundedCornerShape(16.dp),
        colors =
            CardDefaults.cardColors(
                containerColor =
                    EditProductSurface
            ),
        border =
            BorderStroke(
                1.dp,
                EditProductBorder
            )
    ) {

        Column(
            modifier =
                Modifier.padding(16.dp)
        ) {

            Text(
                text = "Basic Information",
                fontWeight = FontWeight.Bold,
                fontSize = 15.sp
            )

            Spacer(
                modifier =
                    Modifier.height(14.dp)
            )

            OutlinedTextField(
                value =
                    uiState.productName,
                onValueChange =
                    onProductNameChange,
                modifier =
                    Modifier.fillMaxWidth(),
                label = {
                    Text("Product Name")
                },
                singleLine = true,
                enabled =
                    !uiState.isSaving,
                isError =
                    uiState.productNameError != null,
                supportingText = {

                    uiState.productNameError
                        ?.let {
                            Text(it)
                        }
                },
                shape =
                    RoundedCornerShape(12.dp),
                colors =
                    editProductTextFieldColors()
            )

            Spacer(
                modifier =
                    Modifier.height(10.dp)
            )

            CategorySelector(
                selectedCategoryName =
                    uiState.categoryName,
                categories =
                    uiState.categories,
                enabled =
                    !uiState.isSaving,
                onCategorySelected =
                    onCategorySelected
            )

            Spacer(
                modifier =
                    Modifier.height(10.dp)
            )

            OutlinedTextField(
                value =
                    uiState.description,
                onValueChange =
                    onDescriptionChange,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp),
                label = {
                    Text("Description")
                },
                placeholder = {
                    Text(
                        "Short description (Optional)"
                    )
                },
                enabled =
                    !uiState.isSaving,
                shape =
                    RoundedCornerShape(12.dp),
                colors =
                    editProductTextFieldColors()
            )
        }
    }
}

@Composable
private fun CategorySelector(
    selectedCategoryName: String,
    categories: List<CategoryOption>,
    enabled: Boolean,
    onCategorySelected: (CategoryOption?) -> Unit
) {

    var expanded by remember {
        mutableStateOf(false)
    }

    Box(
        modifier =
            Modifier.fillMaxWidth()
    ) {

        OutlinedTextField(
            value =
                selectedCategoryName
                    .ifBlank {
                        "Uncategorized"
                    },
            onValueChange = {},
            modifier =
                Modifier.fillMaxWidth(),
            label = {
                Text("Category")
            },
            readOnly = true,
            enabled = enabled,
            shape =
                RoundedCornerShape(12.dp),
            colors =
                editProductTextFieldColors()
        )

        Box(
            modifier = Modifier
                .matchParentSize()
                .padding(top = 8.dp)
        ) {

            Button(
                onClick = {
                    expanded = true
                },
                enabled = enabled,
                modifier =
                    Modifier.matchParentSize(),
                colors =
                    ButtonDefaults.buttonColors(
                        containerColor =
                            Color.Transparent,
                        disabledContainerColor =
                            Color.Transparent
                    )
            ) {}
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = {
                expanded = false
            }
        ) {

            DropdownMenuItem(
                text = {
                    Text("Uncategorized")
                },
                onClick = {
                    expanded = false
                    onCategorySelected(null)
                }
            )

            categories.forEach {
                    category ->

                DropdownMenuItem(
                    text = {
                        Text(category.name)
                    },
                    onClick = {
                        expanded = false

                        onCategorySelected(
                            category
                        )
                    }
                )
            }
        }
    }
}

@Composable
private fun EditPriceAndStockCard(
    uiState: EditProductUiState,
    onPriceChange: (String) -> Unit,
    onCostChange: (String) -> Unit,
    onMinStockLevelChange: (String) -> Unit
) {

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        shape =
            RoundedCornerShape(16.dp),
        colors =
            CardDefaults.cardColors(
                containerColor =
                    EditProductSurface
            ),
        border =
            BorderStroke(
                1.dp,
                EditProductBorder
            )
    ) {

        Column(
            modifier =
                Modifier.padding(16.dp)
        ) {

            Text(
                text = "Price & Stock",
                fontWeight = FontWeight.Bold,
                fontSize = 15.sp
            )

            Spacer(
                modifier =
                    Modifier.height(14.dp)
            )

            Row(
                horizontalArrangement =
                    Arrangement.spacedBy(
                        10.dp
                    )
            ) {

                OutlinedTextField(
                    value =
                        uiState.price,
                    onValueChange =
                        onPriceChange,
                    modifier =
                        Modifier.weight(1f),
                    label = {
                        Text("Price")
                    },
                    keyboardOptions =
                        KeyboardOptions(
                            keyboardType =
                                KeyboardType.Decimal
                        ),
                    singleLine = true,
                    enabled =
                        !uiState.isSaving,
                    isError =
                        uiState.priceError != null,
                    supportingText = {

                        uiState.priceError
                            ?.let {
                                Text(it)
                            }
                    },
                    shape =
                        RoundedCornerShape(
                            12.dp
                        ),
                    colors =
                        editProductTextFieldColors()
                )

                OutlinedTextField(
                    value =
                        uiState.cost,
                    onValueChange =
                        onCostChange,
                    modifier =
                        Modifier.weight(1f),
                    label = {
                        Text("Cost")
                    },
                    keyboardOptions =
                        KeyboardOptions(
                            keyboardType =
                                KeyboardType.Decimal
                        ),
                    singleLine = true,
                    enabled =
                        !uiState.isSaving,
                    isError =
                        uiState.costError != null,
                    supportingText = {

                        uiState.costError
                            ?.let {
                                Text(it)
                            }
                    },
                    shape =
                        RoundedCornerShape(
                            12.dp
                        ),
                    colors =
                        editProductTextFieldColors()
                )
            }

            Spacer(
                modifier =
                    Modifier.height(10.dp)
            )

            OutlinedTextField(
                value =
                    uiState.minStockLevel,
                onValueChange =
                    onMinStockLevelChange,
                modifier =
                    Modifier.fillMaxWidth(),
                label = {
                    Text(
                        "Minimum Stock Level"
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
                    uiState.minStockLevelError != null,
                supportingText = {

                    uiState.minStockLevelError
                        ?.let {
                            Text(it)
                        }
                },
                shape =
                    RoundedCornerShape(12.dp),
                colors =
                    editProductTextFieldColors()
            )

            Spacer(
                modifier =
                    Modifier.height(8.dp)
            )

            Text(
                text =
                    "Current stock is changed from Adjust Stock, not from Edit Product.",
                color =
                    MaterialTheme
                        .colorScheme
                        .onSurfaceVariant,
                fontSize = 11.sp
            )
        }
    }
}

@Composable
private fun editProductTextFieldColors() =
    OutlinedTextFieldDefaults.colors(
        focusedBorderColor =
            EditProductPrimary,
        unfocusedBorderColor =
            EditProductBorder,
        cursorColor =
            EditProductPrimary,
        focusedContainerColor =
            EditProductSurface,
        unfocusedContainerColor =
            EditProductSurface,
        errorBorderColor =
            EditProductError
    )