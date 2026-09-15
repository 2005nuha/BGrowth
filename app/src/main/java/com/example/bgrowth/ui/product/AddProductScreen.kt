package com.example.bgrowth.ui.product

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.bgrowth.ui.theme.BGrothTheme

private val ProductPrimary = Color(0xFF0F5D46)
private val ProductSurface = Color.White
private val ProductBorder = Color(0xFFD9E3DA)
private val ProductSecondaryText = Color(0xFF5E6B63)
private val ProductMutedText = Color(0xFF8A958E)
private val ProductError = Color(0xFFD9534F)

@Composable
fun AddProductScreen(
    onBackClick: () -> Unit,
    onCancelClick: () -> Unit,
    onProductSaved: () -> Unit,
    viewModel: AddProductViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(uiState.isProductSaved) {
        if (uiState.isProductSaved) {
            viewModel.consumeProductSaved()
            onProductSaved()
        }
    }

    AddProductContent(
        uiState = uiState,
        onBackClick = onBackClick,
        onCancelClick = onCancelClick,
        onProductNameChange = viewModel::onProductNameChange,
        onCategoryNameChange = viewModel::onCategoryNameChange,
        onDescriptionChange = viewModel::onDescriptionChange,
        onPriceChange = viewModel::onPriceChange,
        onCostChange = viewModel::onCostChange,
        onOpeningStockChange = viewModel::onOpeningStockChange,
        onMinStockLevelChange = viewModel::onMinStockLevelChange,
        onTrackStockChange = viewModel::onTrackStockChange,
        onSaveProduct = viewModel::saveProduct
    )
}

@Composable
private fun AddProductContent(
    uiState: AddProductUiState,
    onBackClick: () -> Unit,
    onCancelClick: () -> Unit,
    onProductNameChange: (String) -> Unit,
    onCategoryNameChange: (String) -> Unit,
    onDescriptionChange: (String) -> Unit,
    onPriceChange: (String) -> Unit,
    onCostChange: (String) -> Unit,
    onOpeningStockChange: (String) -> Unit,
    onMinStockLevelChange: (String) -> Unit,
    onTrackStockChange: (Boolean) -> Unit,
    onSaveProduct: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(bottom = 24.dp)
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    start = 8.dp,
                    end = 16.dp,
                    top = 12.dp
                ),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = onBackClick
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = ProductPrimary
                )
            }

            Text(
                text = "Add Product",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = ProductPrimary
            )
        }

        Spacer(
            modifier = Modifier.height(8.dp)
        )

        LogoUploadCard()

        Spacer(
            modifier = Modifier.height(14.dp)
        )

        BasicInformationCard(
            uiState = uiState,
            onProductNameChange = onProductNameChange,
            onCategoryNameChange = onCategoryNameChange,
            onDescriptionChange = onDescriptionChange
        )

        Spacer(
            modifier = Modifier.height(14.dp)
        )

        PriceAndStockCard(
            uiState = uiState,
            onPriceChange = onPriceChange,
            onCostChange = onCostChange,
            onOpeningStockChange = onOpeningStockChange,
            onMinStockLevelChange = onMinStockLevelChange
        )

        Spacer(
            modifier = Modifier.height(14.dp)
        )

        TrackStockCard(
            enabled = uiState.trackStock,
            onCheckedChange = onTrackStockChange
        )

        Spacer(
            modifier = Modifier.height(18.dp)
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {

            OutlinedButton(
                onClick = onCancelClick,
                modifier = Modifier
                    .weight(1f)
                    .height(52.dp),
                shape = RoundedCornerShape(14.dp),
                border = BorderStroke(
                    width = 1.dp,
                    color = ProductBorder
                )
            ) {
                Text(
                    text = "Cancel",
                    color = MaterialTheme.colorScheme.onSurface
                )
            }

            Button(
                onClick = onSaveProduct,
                modifier = Modifier
                    .weight(2f)
                    .height(52.dp),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = ProductPrimary
                )
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = null
                )

                Spacer(
                    modifier = Modifier.size(6.dp)
                )

                Text(
                    text = "Save Product"
                )
            }
        }

        uiState.errorMessage?.let { message ->

            Spacer(
                modifier = Modifier.height(12.dp)
            )

            Text(
                text = message,
                color = ProductError,
                modifier = Modifier.padding(
                    horizontal = 16.dp
                )
            )
        }
    }
}

@Composable
private fun LogoUploadCard() {

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .clickable {
                // TODO: Add image picker later
            },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = ProductSurface
        ),
        border = BorderStroke(
            width = 1.dp,
            color = ProductPrimary.copy(
                alpha = 0.35f
            )
        )
    ) {

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 22.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "Add logo",
                tint = ProductPrimary,
                modifier = Modifier.size(28.dp)
            )

            Spacer(
                modifier = Modifier.height(8.dp)
            )

            Text(
                text = "Add Logo (Optional)",
                fontSize = 13.sp,
                color = ProductMutedText
            )
        }
    }
}

@Composable
private fun BasicInformationCard(
    uiState: AddProductUiState,
    onProductNameChange: (String) -> Unit,
    onCategoryNameChange: (String) -> Unit,
    onDescriptionChange: (String) -> Unit
) {

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = ProductSurface
        ),
        border = BorderStroke(
            1.dp,
            ProductBorder
        )
    ) {

        Column(
            modifier = Modifier.padding(16.dp)
        ) {

            Text(
                text = "Basic Information",
                fontWeight = FontWeight.Bold,
                fontSize = 15.sp
            )

            Spacer(
                modifier = Modifier.height(14.dp)
            )

            OutlinedTextField(
                value = uiState.productName,
                onValueChange = onProductNameChange,
                modifier = Modifier.fillMaxWidth(),
                label = {
                    Text("Product Name")
                },
                placeholder = {
                    Text("e.g. Product name")
                },
                singleLine = true,
                isError = uiState.productNameError != null,
                supportingText = {
                    uiState.productNameError?.let {
                        Text(it)
                    }
                },
                shape = RoundedCornerShape(12.dp),
                colors = productTextFieldColors()
            )

            Spacer(
                modifier = Modifier.height(10.dp)
            )

            OutlinedTextField(
                value = uiState.categoryName,
                onValueChange = onCategoryNameChange,
                modifier = Modifier.fillMaxWidth(),
                label = {
                    Text("Category")
                },
                placeholder = {
                    Text(
                        "e.g. Clothing, Electronics, Services"
                    )
                },
                singleLine = true,
                isError = uiState.categoryError != null,
                supportingText = {
                    uiState.categoryError?.let {
                        Text(it)
                    }
                },
                shape = RoundedCornerShape(12.dp),
                colors = productTextFieldColors()
            )

            Spacer(
                modifier = Modifier.height(10.dp)
            )

            OutlinedTextField(
                value = uiState.description,
                onValueChange = onDescriptionChange,
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
                shape = RoundedCornerShape(12.dp),
                colors = productTextFieldColors()
            )
        }
    }
}

@Composable
private fun PriceAndStockCard(
    uiState: AddProductUiState,
    onPriceChange: (String) -> Unit,
    onCostChange: (String) -> Unit,
    onOpeningStockChange: (String) -> Unit,
    onMinStockLevelChange: (String) -> Unit
) {

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = ProductSurface
        ),
        border = BorderStroke(
            1.dp,
            ProductBorder
        )
    ) {

        Column(
            modifier = Modifier.padding(16.dp)
        ) {

            Text(
                text = "Price & Stock",
                fontWeight = FontWeight.Bold,
                fontSize = 15.sp
            )

            Spacer(
                modifier = Modifier.height(14.dp)
            )

            Row(
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {

                OutlinedTextField(
                    value = uiState.price,
                    onValueChange = onPriceChange,
                    modifier = Modifier.weight(1f),
                    label = {
                        Text("Price")
                    },
                    placeholder = {
                        Text("0.00")
                    },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Decimal
                    ),
                    singleLine = true,
                    isError = uiState.priceError != null,
                    supportingText = {
                        uiState.priceError?.let {
                            Text(it)
                        }
                    },
                    shape = RoundedCornerShape(12.dp),
                    colors = productTextFieldColors()
                )

                OutlinedTextField(
                    value = uiState.cost,
                    onValueChange = onCostChange,
                    modifier = Modifier.weight(1f),
                    label = {
                        Text("Cost (optional)")
                    },
                    placeholder = {
                        Text("0.00")
                    },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Decimal
                    ),
                    singleLine = true,
                    isError = uiState.costError != null,
                    supportingText = {
                        uiState.costError?.let {
                            Text(it)
                        }
                    },
                    shape = RoundedCornerShape(12.dp),
                    colors = productTextFieldColors()
                )
            }

            Spacer(
                modifier = Modifier.height(10.dp)
            )

            Row(
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {

                OutlinedTextField(
                    value = uiState.openingStock,
                    onValueChange = onOpeningStockChange,
                    modifier = Modifier.weight(1f),
                    label = {
                        Text("Opening Stock")
                    },
                    placeholder = {
                        Text("0")
                    },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number
                    ),
                    singleLine = true,
                    enabled = uiState.trackStock,
                    isError =
                        uiState.openingStockError != null,
                    supportingText = {
                        uiState.openingStockError?.let {
                            Text(it)
                        }
                    },
                    shape = RoundedCornerShape(12.dp),
                    colors = productTextFieldColors()
                )

                OutlinedTextField(
                    value = uiState.minStockLevel,
                    onValueChange = onMinStockLevelChange,
                    modifier = Modifier.weight(1f),
                    label = {
                        Text("Min Stock Level")
                    },
                    placeholder = {
                        Text("0")
                    },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number
                    ),
                    singleLine = true,
                    enabled = uiState.trackStock,
                    isError =
                        uiState.minStockLevelError != null,
                    supportingText = {
                        uiState.minStockLevelError?.let {
                            Text(it)
                        }
                    },
                    shape = RoundedCornerShape(12.dp),
                    colors = productTextFieldColors()
                )
            }

            Spacer(
                modifier = Modifier.height(8.dp)
            )

            Text(
                text = "You'll get a low-stock alert once quantity goes below this level.",
                color = ProductMutedText,
                fontSize = 11.sp
            )
        }
    }
}

@Composable
private fun TrackStockCard(
    enabled: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = ProductSurface
        ),
        border = BorderStroke(
            1.dp,
            ProductBorder
        )
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Column(
                modifier = Modifier.weight(1f)
            ) {

                Text(
                    text = "Track Stock",
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp
                )

                Spacer(
                    modifier = Modifier.height(3.dp)
                )

                Text(
                    text = "Automatically update stock on each sale",
                    fontSize = 12.sp,
                    color = ProductSecondaryText
                )
            }

            Switch(
                checked = enabled,
                onCheckedChange = onCheckedChange,
                colors = SwitchDefaults.colors(
                    checkedThumbColor = Color.White,
                    checkedTrackColor = ProductPrimary
                )
            )
        }
    }
}

@Composable
private fun productTextFieldColors() =
    OutlinedTextFieldDefaults.colors(
        focusedBorderColor = ProductPrimary,
        unfocusedBorderColor = ProductBorder,
        cursorColor = ProductPrimary,
        focusedContainerColor = ProductSurface,
        unfocusedContainerColor = ProductSurface,
        errorBorderColor = ProductError
    )

@Preview(
    name = "Add Product - Empty",
    showBackground = true,
    widthDp = 393,
    heightDp = 852
)
@Composable
private fun AddProductEmptyPreview() {

    BGrothTheme {

        AddProductContent(
            uiState = AddProductUiState(),
            onBackClick = {},
            onCancelClick = {},
            onProductNameChange = {},
            onCategoryNameChange = {},
            onDescriptionChange = {},
            onPriceChange = {},
            onCostChange = {},
            onOpeningStockChange = {},
            onMinStockLevelChange = {},
            onTrackStockChange = {},
            onSaveProduct = {}
        )
    }
}

@Preview(
    name = "Add Product - Filled",
    showBackground = true,
    widthDp = 393,
    heightDp = 852
)
@Composable
private fun AddProductFilledPreview() {

    BGrothTheme {

        AddProductContent(
            uiState = AddProductUiState(
                productName = "Premium T-Shirt",
                categoryName = "Men's Clothing",
                description = "Black cotton t-shirt",
                price = "50",
                cost = "30",
                openingStock = "25",
                minStockLevel = "5",
                trackStock = true
            ),
            onBackClick = {},
            onCancelClick = {},
            onProductNameChange = {},
            onCategoryNameChange = {},
            onDescriptionChange = {},
            onPriceChange = {},
            onCostChange = {},
            onOpeningStockChange = {},
            onMinStockLevelChange = {},
            onTrackStockChange = {},
            onSaveProduct = {}
        )
    }
}