package com.example.bgrowth.ui.sales

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.bgrowth.data.model.Product
import java.util.Locale

private val PrimaryGreen = Color(0xFF0F5D46)
private val LightBackground = Color(0xFFF7F8F4)
private val BorderColor = Color(0xFFD9E3DA)

@Composable
fun SaleCartSection(
    uiState: RecordSaleUiState,
    onSearchChange: (String) -> Unit,
    onAddProduct: (Product) -> Unit,
    onQuantityChange: (Int, Int) -> Unit,
    onRemoveProduct: (Int) -> Unit,
    onPaidAmountChange: (String) -> Unit,
    onSaveClick: () -> Unit
) {
    val filteredProducts = remember(
        uiState.products,
        uiState.productSearchQuery
    ) {
        uiState.products.filter {
            it.name.contains(
                uiState.productSearchQuery,
                ignoreCase = true
            )
        }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(18.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color.White
            ),
            border = BorderStroke(1.dp, BorderColor)
        ) {
            Column(
                modifier = Modifier.padding(18.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                Text(
                    text = "Select products",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = PrimaryGreen
                )

                OutlinedTextField(
                    value = uiState.productSearchQuery,
                    onValueChange = onSearchChange,
                    label = { Text("Search products") },
                    placeholder = { Text("Enter product name") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    enabled = !uiState.isSaving
                )

                if (uiState.isLoading) {
                    CircularProgressIndicator(
                        color = PrimaryGreen
                    )
                } else if (filteredProducts.isEmpty()) {
                    Text(
                        text = "No products found",
                        color = Color.Gray
                    )
                } else {
                    // The parent RecordSaleScreen already scrolls.
                    // A regular Column avoids nested scrolling conflicts.
                    Column(
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        filteredProducts.take(8).forEach { product ->
                            ProductSelectionRow(
                                product = product,
                                enabled = !uiState.isSaving,
                                onAdd = { onAddProduct(product) }
                            )
                        }
                    }

                    if (filteredProducts.size > 8) {
                        Text(
                            text = "Type to narrow your search",
                            color = Color.Gray,
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(18.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(18.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color.White
            ),
            border = BorderStroke(1.dp, BorderColor)
        ) {
            Column(
                modifier = Modifier.padding(18.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                Text(
                    text = "Sale cart",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = PrimaryGreen
                )

                if (uiState.cartItems.isEmpty()) {
                    Text(
                        text = "No products added yet",
                        color = Color.Gray
                    )
                } else {
                    uiState.cartItems.forEach { item ->
                        CartProductRow(
                            item = item,
                            enabled = !uiState.isSaving,
                            onIncrease = {
                                onQuantityChange(
                                    item.product.id,
                                    item.quantity + 1
                                )
                            },
                            onDecrease = {
                                onQuantityChange(
                                    item.product.id,
                                    item.quantity - 1
                                )
                            },
                            onRemove = {
                                onRemoveProduct(item.product.id)
                            }
                        )

                        HorizontalDivider(color = BorderColor)
                    }
                }

                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = LightBackground
                    ),
                    shape = RoundedCornerShape(14.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Total")

                        Text(
                            text = formatSaleAmount(uiState.cartTotal),
                            fontWeight = FontWeight.Bold,
                            color = PrimaryGreen
                        )
                    }
                }

                OutlinedTextField(
                    value = uiState.paidAmount,
                    onValueChange = onPaidAmountChange,
                    label = { Text("Paid amount") },
                    placeholder = { Text("0.00") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    enabled = !uiState.isSaving
                )

                Text(
                    text = "Leave empty for an unpaid sale",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray
                )

                uiState.errorMessage?.let { message ->
                    Text(
                        text = message,
                        color = MaterialTheme.colorScheme.error
                    )
                }

                Button(
                    onClick = onSaveClick,
                    enabled = !uiState.isSaving &&
                            !uiState.isLoading &&
                            uiState.cartItems.isNotEmpty(),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = PrimaryGreen
                    ),
                    shape = RoundedCornerShape(14.dp)
                ) {
                    if (uiState.isSaving) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(22.dp),
                            color = Color.White,
                            strokeWidth = 2.dp
                        )
                    } else {
                        Text("Save sale")
                    }
                }
            }
        }
    }
}

@Composable
private fun ProductSelectionRow(
    product: Product,
    enabled: Boolean,
    onAdd: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = product.name,
                fontWeight = FontWeight.SemiBold
            )

            Text(
                text = "Stock: ${product.quantity} | Price: ${product.sellingPrice}",
                style = MaterialTheme.typography.bodySmall,
                color = Color.Gray
            )
        }

        TextButton(
            onClick = onAdd,
            enabled = enabled && product.quantity > 0
        ) {
            Text(
                text = "Add",
                color = PrimaryGreen
            )
        }
    }
}

@Composable
private fun CartProductRow(
    item: SaleCartItem,
    enabled: Boolean,
    onIncrease: () -> Unit,
    onDecrease: () -> Unit,
    onRemove: () -> Unit
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = item.product.name,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.weight(1f)
            )

            TextButton(
                onClick = onRemove,
                enabled = enabled
            ) {
                Text("Remove", color = MaterialTheme.colorScheme.error)
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                OutlinedButton(
                    onClick = onDecrease,
                    enabled = enabled && item.quantity > 1
                ) {
                    Text("−")
                }

                Text(
                    text = "${item.quantity}",
                    modifier = Modifier.padding(horizontal = 12.dp)
                )

                OutlinedButton(
                    onClick = onIncrease,
                    enabled = enabled &&
                            item.quantity < item.product.quantity
                ) {
                    Text("+")
                }
            }

            Text(
                text = formatSaleAmount(
                    (item.product.sellingPrice.toDoubleOrNull() ?: 0.0) *
                            item.quantity
                ),
                fontWeight = FontWeight.Bold
            )
        }
    }
}

private fun formatSaleAmount(value: Double): String {
    return String.format(Locale.US, "%.2f", value)
}