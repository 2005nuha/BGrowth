package com.example.bgrowth.ui.sales

import androidx.compose.foundation.BorderStroke
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
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.bgrowth.ui.theme.BGrothTheme
import java.util.Locale

private val SalePrimary = Color(0xFF0F5D46)
private val SaleBackground = Color(0xFFF7F8F4)
private val SaleSurface = Color(0xFFFFFFFF)
private val SaleBorder = Color(0xFFD9E3DA)
private val SaleSecondaryText = Color(0xFF5E6B63)
private val SaleMutedText = Color(0xFF8A958E)
private val SaleError = Color(0xFFD9534F)

@Composable
fun RecordSaleScreen(
    onBackClick: () -> Unit,
    onViewAllClick: () -> Unit = {},
    viewModel: RecordSaleViewModel = viewModel()
) {

    val uiState by viewModel.uiState.collectAsState()

    RecordSaleContent(
        uiState = uiState,
        onBackClick = onBackClick,
        onViewAllClick = onViewAllClick,
        onProductNameChange = viewModel::onProductNameChange,
        onQuantityChange = viewModel::onQuantityChange,
        onUnitPriceChange = viewModel::onUnitPriceChange,
        onPaymentMethodChange = viewModel::onPaymentMethodChange,
        onSaveClick = viewModel::saveSale
    )
}

@Composable
private fun RecordSaleContent(
    uiState: RecordSaleUiState,
    onBackClick: () -> Unit,
    onViewAllClick: () -> Unit,
    onProductNameChange: (String) -> Unit,
    onQuantityChange: (String) -> Unit,
    onUnitPriceChange: (String) -> Unit,
    onPaymentMethodChange: (String) -> Unit,
    onSaveClick: () -> Unit
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
                    end = 20.dp,
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
                    tint = SalePrimary
                )
            }

            Text(
                text = "Record a sale",
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold,
                color = SalePrimary
            )
        }

        Spacer(
            modifier = Modifier.height(8.dp)
        )

        RecordSaleForm(
            uiState = uiState,
            onProductNameChange = onProductNameChange,
            onQuantityChange = onQuantityChange,
            onUnitPriceChange = onUnitPriceChange,
            onPaymentMethodChange = onPaymentMethodChange,
            onSaveClick = onSaveClick
        )

        Spacer(
            modifier = Modifier.height(20.dp)
        )

        TodaySalesSection(
            sales = uiState.todaySales,
            onViewAllClick = onViewAllClick
        )
    }
}

@Composable
private fun RecordSaleForm(
    uiState: RecordSaleUiState,
    onProductNameChange: (String) -> Unit,
    onQuantityChange: (String) -> Unit,
    onUnitPriceChange: (String) -> Unit,
    onPaymentMethodChange: (String) -> Unit,
    onSaveClick: () -> Unit
) {

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(
            containerColor = SaleSurface
        ),
        border = BorderStroke(
            width = 1.dp,
            color = SaleBorder
        )
    ) {

        Column(
            modifier = Modifier.padding(18.dp)
        ) {

            OutlinedTextField(
                value = uiState.productName,
                onValueChange = onProductNameChange,
                modifier = Modifier.fillMaxWidth(),
                label = {
                    Text("Product / Service")
                },
                placeholder = {
                    Text("Enter product or service name")
                },
                singleLine = true,
                isError = uiState.productError != null,
                supportingText = {
                    uiState.productError?.let {
                        Text(it)
                    }
                },
                shape = RoundedCornerShape(14.dp),
                colors = saleTextFieldColors()
            )

            Spacer(
                modifier = Modifier.height(14.dp)
            )

            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {

                OutlinedTextField(
                    value = uiState.quantity,
                    onValueChange = onQuantityChange,
                    modifier = Modifier.weight(1f),
                    label = {
                        Text("Quantity")
                    },
                    placeholder = {
                        Text("1")
                    },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number
                    ),
                    singleLine = true,
                    isError = uiState.quantityError != null,
                    supportingText = {
                        uiState.quantityError?.let {
                            Text(it)
                        }
                    },
                    shape = RoundedCornerShape(14.dp),
                    colors = saleTextFieldColors()
                )

                OutlinedTextField(
                    value = uiState.unitPrice,
                    onValueChange = onUnitPriceChange,
                    modifier = Modifier.weight(1f),
                    label = {
                        Text("Unit Price")
                    },
                    placeholder = {
                        Text("0.00")
                    },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Decimal
                    ),
                    singleLine = true,
                    isError = uiState.unitPriceError != null,
                    supportingText = {
                        uiState.unitPriceError?.let {
                            Text(it)
                        }
                    },
                    shape = RoundedCornerShape(14.dp),
                    colors = saleTextFieldColors()
                )
            }

            Spacer(
                modifier = Modifier.height(8.dp)
            )

            OutlinedTextField(
                value = uiState.paymentMethod,
                onValueChange = onPaymentMethodChange,
                modifier = Modifier.fillMaxWidth(),
                label = {
                    Text("Payment Method")
                },
                placeholder = {
                    Text("e.g. Cash, Bank Transfer, Wallet")
                },
                singleLine = true,
                isError = uiState.paymentMethodError != null,
                supportingText = {
                    uiState.paymentMethodError?.let {
                        Text(it)
                    }
                },
                shape = RoundedCornerShape(14.dp),
                colors = saleTextFieldColors()
            )

            Spacer(
                modifier = Modifier.height(18.dp)
            )

            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = SaleBackground
                ),
                border = BorderStroke(
                    1.dp,
                    SaleBorder
                ),
                shape = RoundedCornerShape(14.dp)
            ) {

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement =
                        Arrangement.SpaceBetween,
                    verticalAlignment =
                        Alignment.CenterVertically
                ) {

                    Text(
                        text = "Total",
                        fontSize = 17.sp
                    )

                    Text(
                        text = currency(
                            uiState.total
                        ),
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(
                modifier = Modifier.height(10.dp)
            )

            Button(
                onClick = onSaveClick,
                enabled = !uiState.isLoading,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = SalePrimary
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
                    text = "Save sale",
                    fontSize = 16.sp
                )
            }
        }
    }
}

@Composable
private fun TodaySalesSection(
    sales: List<TodaySaleItem>,
    onViewAllClick: () -> Unit
) {

    Column(
        modifier = Modifier.padding(
            horizontal = 16.dp
        )
    ) {

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement =
                Arrangement.SpaceBetween,
            verticalAlignment =
                Alignment.CenterVertically
        ) {

            Text(
                text = "Today's sales",
                fontSize = 21.sp,
                fontWeight = FontWeight.Bold
            )

            TextButton(
                onClick = onViewAllClick
            ) {
                Text(
                    text = "View all",
                    color = SalePrimary
                )
            }
        }

        if (sales.isEmpty()) {

            EmptySalesCard()

        } else {

            sales.forEach { sale ->

                TodaySaleCard(
                    sale = sale
                )

                Spacer(
                    modifier = Modifier.height(10.dp)
                )
            }
        }
    }
}

@Composable
private fun EmptySalesCard() {

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = SaleSurface
        ),
        border = BorderStroke(
            1.dp,
            SaleBorder
        )
    ) {

        Column(
            modifier = Modifier.padding(24.dp),
            horizontalAlignment =
                Alignment.CenterHorizontally
        ) {

            Text(
                text = "No sales yet today",
                fontSize = 17.sp,
                fontWeight = FontWeight.SemiBold,
                color = SalePrimary
            )

            Spacer(
                modifier = Modifier.height(6.dp)
            )

            Text(
                text = "Your recorded sales will appear here.",
                color = SaleSecondaryText,
                fontSize = 14.sp,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
private fun TodaySaleCard(
    sale: TodaySaleItem
) {

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = SaleSurface
        ),
        border = BorderStroke(
            1.dp,
            SaleBorder
        )
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment =
                Alignment.CenterVertically
        ) {

            Card(
                modifier = Modifier.size(48.dp),
                colors = CardDefaults.cardColors(
                    containerColor =
                        Color(0xFFE1E3E2)
                ),
                shape = RoundedCornerShape(12.dp)
            ) {}

            Spacer(
                modifier = Modifier.size(10.dp)
            )

            Column(
                modifier = Modifier.weight(1f)
            ) {

                Text(
                    text = "${sale.productName} x${sale.quantity}",
                    fontWeight = FontWeight.Medium,
                    fontSize = 15.sp
                )

                Spacer(
                    modifier = Modifier.height(3.dp)
                )

                Text(
                    text = "${sale.time} . ${sale.paymentMethod}",
                    color = SaleMutedText,
                    fontSize = 12.sp
                )
            }

            Text(
                text = currency(sale.total),
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp
            )
        }
    }
}

@Composable
private fun saleTextFieldColors() =
    OutlinedTextFieldDefaults.colors(
        focusedBorderColor = SalePrimary,
        unfocusedBorderColor = SaleBorder,
        cursorColor = SalePrimary,
        focusedContainerColor = SaleSurface,
        unfocusedContainerColor = SaleSurface,
        errorBorderColor = SaleError
    )

private fun currency(
    value: Double
): String {

    return String.format(
        Locale.US,
        "$%.2f",
        value
    )
}

@Preview(
    name = "Record Sale - Empty",
    showBackground = true,
    widthDp = 393,
    heightDp = 852
)
@Composable
private fun RecordSaleEmptyPreview() {

    BGrothTheme {

        RecordSaleContent(
            uiState = RecordSaleUiState(),
            onBackClick = {},
            onViewAllClick = {},
            onProductNameChange = {},
            onQuantityChange = {},
            onUnitPriceChange = {},
            onPaymentMethodChange = {},
            onSaveClick = {}
        )
    }
}

@Preview(
    name = "Record Sale - Populated",
    showBackground = true,
    widthDp = 393,
    heightDp = 852
)
@Composable
private fun RecordSalePopulatedPreview() {

    BGrothTheme {

        RecordSaleContent(
            uiState = RecordSaleUiState(
                productName = "Website Design",
                quantity = "1",
                unitPrice = "250",
                paymentMethod = "Bank Transfer",

                todaySales = listOf(
                    TodaySaleItem(
                        id = 1,
                        productName = "Website Design",
                        quantity = 1,
                        unitPrice = 250.0,
                        paymentMethod = "Bank Transfer",
                        time = "10:24 AM"
                    ),

                    TodaySaleItem(
                        id = 2,
                        productName = "Men's T-Shirt",
                        quantity = 2,
                        unitPrice = 20.0,
                        paymentMethod = "Cash",
                        time = "09:50 AM"
                    ),

                    TodaySaleItem(
                        id = 3,
                        productName = "Consultation",
                        quantity = 1,
                        unitPrice = 100.0,
                        paymentMethod = "Mobile Wallet",
                        time = "09:12 AM"
                    )
                )
            ),

            onBackClick = {},
            onViewAllClick = {},
            onProductNameChange = {},
            onQuantityChange = {},
            onUnitPriceChange = {},
            onPaymentMethodChange = {},
            onSaveClick = {}
        )
    }
}