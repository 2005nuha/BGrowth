package com.example.bgrowth.ui.product

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.AlertDialog
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
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.bgrowth.ui.theme.BGrothTheme
import java.util.Locale
import androidx.compose.runtime.LaunchedEffect

private val ProductsPrimary = Color(0xFF0F5D46)
private val ProductsSurface = Color.White
private val ProductsBorder = Color(0xFFD9E3DA)
private val ProductsSecondaryText = Color(0xFF5E6B63)
private val ProductsMutedText = Color(0xFF8A958E)
private val ProductsLowStock = Color(0xFFF39C12)
private val ProductsOutOfStock = Color(0xFFD9534F)

@Composable
fun ProductsScreen(
    onBackClick: () -> Unit,
    onAddProductClick: () -> Unit,
    onProductClick: (Int) -> Unit = {},
    onAdjustStockClick: (Int) -> Unit = {},
    refreshRequested: Boolean = false,
    onRefreshConsumed: () -> Unit = {},
    viewModel: ProductsViewModel = viewModel()
) {

    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(refreshRequested) {

        if (refreshRequested) {

            viewModel.loadProducts()

            onRefreshConsumed()
        }
    }

    ProductsContent(
        uiState = uiState,
        onBackClick = onBackClick,
        onAddProductClick = onAddProductClick,
        onProductClick = onProductClick,
        onAdjustStockClick = onAdjustStockClick,
        onSearchQueryChange = viewModel::onSearchQueryChange,
        onCategorySelected = viewModel::onCategorySelected,
        onOpenProductMenu = viewModel::openProductMenu,
        onCloseProductMenu = viewModel::closeProductMenu,
        onRequestDeleteProduct = viewModel::requestDeleteProduct,
        onCancelDeleteProduct = viewModel::cancelDeleteProduct,
        onConfirmDeleteProduct = viewModel::confirmDeleteProduct
    )
}

@Composable
private fun ProductsContent(
    uiState: ProductsUiState,
    onBackClick: () -> Unit,
    onAddProductClick: () -> Unit,
    onProductClick: (Int) -> Unit,
    onAdjustStockClick: (Int) -> Unit,
    onSearchQueryChange: (String) -> Unit,
    onCategorySelected: (String?) -> Unit,
    onOpenProductMenu: (Int) -> Unit,
    onCloseProductMenu: () -> Unit,
    onRequestDeleteProduct: (ProductListItem) -> Unit,
    onCancelDeleteProduct: () -> Unit,
    onConfirmDeleteProduct: () -> Unit
) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                MaterialTheme.colorScheme.background
            )
    ) {

        ProductsHeader(
            onBackClick = onBackClick
        )

        SearchSection(
            searchQuery = uiState.searchQuery,
            onSearchQueryChange = onSearchQueryChange
        )

        if (uiState.categories.isNotEmpty()) {

            CategoryChips(
                categories = uiState.categories,
                selectedCategory = uiState.selectedCategory,
                onCategorySelected = onCategorySelected
            )
        }

        Spacer(
            modifier = Modifier.height(12.dp)
        )

        when {

            uiState.isLoading -> {
                LoadingProductsState()
            }

            uiState.errorMessage != null -> {

                Text(
                    text = uiState.errorMessage,
                    color = ProductsOutOfStock,
                    modifier = Modifier.padding(
                        horizontal = 16.dp,
                        vertical = 8.dp
                    )
                )
            }

            uiState.filteredProducts.isEmpty() -> {

                EmptyProductsState(
                    hasSearchOrFilter =
                        uiState.searchQuery.isNotBlank() ||
                                uiState.selectedCategory != null
                )
            }

            else -> {

                ProductsList(
                    products = uiState.filteredProducts,
                    openedMenuProductId =
                        uiState.openedMenuProductId,
                    onProductClick = onProductClick,
                    onAdjustStockClick = onAdjustStockClick,
                    onOpenProductMenu = onOpenProductMenu,
                    onCloseProductMenu = onCloseProductMenu,
                    onRequestDeleteProduct =
                        onRequestDeleteProduct
                )
            }
        }

        Spacer(
            modifier = Modifier.weight(1f)
        )

        AddProductBottomButton(
            onClick = onAddProductClick
        )
    }

    uiState.productPendingDelete?.let { product ->

        DeleteProductDialog(
            product = product,
            isDeleting = uiState.isDeleting,
            onClose = onCancelDeleteProduct,
            onConfirmDelete = onConfirmDeleteProduct
        )
    }
}

@Composable
private fun ProductsHeader(
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
        verticalAlignment = Alignment.CenterVertically
    ) {

        IconButton(
            onClick = onBackClick
        ) {

            Icon(
                imageVector =
                    Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Back",
                tint = ProductsPrimary
            )
        }

        Column {

            Text(
                text = "Products",
                color = ProductsPrimary,
                fontWeight = FontWeight.Bold,
                fontSize = 22.sp
            )

            Text(
                text = "Manage your products and stock",
                color = ProductsSecondaryText,
                fontSize = 12.sp
            )
        }
    }
}

@Composable
private fun SearchSection(
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit
) {

    OutlinedTextField(
        value = searchQuery,
        onValueChange = onSearchQueryChange,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        placeholder = {
            Text("Search Products...")
        },
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = null
            )
        },
        singleLine = true,
        shape = RoundedCornerShape(12.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = ProductsPrimary,
            unfocusedBorderColor = ProductsBorder
        )
    )
}

@Composable
private fun CategoryChips(
    categories: List<String>,
    selectedCategory: String?,
    onCategorySelected: (String?) -> Unit
) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                start = 16.dp,
                top = 10.dp
            )
            .horizontalScroll(
                rememberScrollState()
            ),
        horizontalArrangement =
            Arrangement.spacedBy(8.dp)
    ) {

        CategoryChip(
            text = "All",
            selected = selectedCategory == null,
            onClick = {
                onCategorySelected(null)
            }
        )

        categories.forEach { category ->

            CategoryChip(
                text = category,
                selected =
                    selectedCategory == category,
                onClick = {
                    onCategorySelected(category)
                }
            )
        }

        Spacer(
            modifier = Modifier.width(8.dp)
        )
    }
}

@Composable
private fun CategoryChip(
    text: String,
    selected: Boolean,
    onClick: () -> Unit
) {

    Surface(
        modifier = Modifier.clickable(
            onClick = onClick
        ),
        shape = RoundedCornerShape(20.dp),
        color =
            if (selected) {
                ProductsPrimary
            } else {
                ProductsSurface
            },
        border =
            if (selected) {
                null
            } else {
                BorderStroke(
                    1.dp,
                    ProductsBorder
                )
            }
    ) {

        Text(
            text = text,
            color =
                if (selected) {
                    Color.White
                } else {
                    ProductsSecondaryText
                },
            fontSize = 12.sp,
            modifier = Modifier.padding(
                horizontal = 14.dp,
                vertical = 8.dp
            )
        )
    }
}

@Composable
private fun ProductsList(
    products: List<ProductListItem>,
    openedMenuProductId: Int?,
    onProductClick: (Int) -> Unit,
    onAdjustStockClick: (Int) -> Unit,
    onOpenProductMenu: (Int) -> Unit,
    onCloseProductMenu: () -> Unit,
    onRequestDeleteProduct: (ProductListItem) -> Unit
) {

    Column(
        modifier = Modifier.padding(
            horizontal = 16.dp
        ),
        verticalArrangement =
            Arrangement.spacedBy(10.dp)
    ) {

        products.forEach { product ->

            ProductCard(
                product = product,
                menuExpanded =
                    openedMenuProductId == product.id,

                onClick = {
                    onProductClick(product.id)
                },

                onAdjustStockClick = {
                    onAdjustStockClick(product.id)
                },

                onMenuClick = {
                    onOpenProductMenu(product.id)
                },

                onDismissMenu =
                    onCloseProductMenu,

                onDeleteClick = {
                    onRequestDeleteProduct(product)
                }
            )
        }
    }
}

@Composable
private fun ProductCard(
    product: ProductListItem,
    menuExpanded: Boolean,
    onClick: () -> Unit,
    onAdjustStockClick: () -> Unit,
    onMenuClick: () -> Unit,
    onDismissMenu: () -> Unit,
    onDeleteClick: () -> Unit
) {

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(
                onClick = onClick
            ),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(
            containerColor = ProductsSurface
        ),
        border = BorderStroke(
            1.dp,
            ProductsBorder
        )
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment =
                Alignment.CenterVertically
        ) {

            Box(
                modifier = Modifier
                    .size(52.dp)
                    .background(
                        color = Color(0xFFE5E8E6),
                        shape = RoundedCornerShape(12.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {

                Text(
                    text =
                        product.name
                            .firstOrNull()
                            ?.uppercase()
                            ?: "P",
                    color = ProductsPrimary,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(
                modifier = Modifier.width(10.dp)
            )

            Column(
                modifier = Modifier.weight(1f)
            ) {

                Text(
                    text = product.name,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 14.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(
                    modifier = Modifier.height(2.dp)
                )

                Text(
                    text =
                        product.categoryName
                            .ifBlank {
                                "Uncategorized"
                            },
                    color = ProductsMutedText,
                    fontSize = 11.sp,
                    maxLines = 1
                )
            }

            Column(
                horizontalAlignment = Alignment.End
            ) {

                Text(
                    text = formatPrice(
                        product.price
                    ),
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp
                )

                Spacer(
                    modifier = Modifier.height(3.dp)
                )

                StockStatusText(
                    product = product
                )
            }

            Spacer(
                modifier = Modifier.width(4.dp)
            )

            Box {

                IconButton(
                    onClick = onMenuClick
                ) {

                    Icon(
                        imageVector =
                            Icons.Default.MoreVert,
                        contentDescription =
                            "Product menu",
                        tint = ProductsMutedText
                    )
                }

                DropdownMenu(
                    expanded = menuExpanded,
                    onDismissRequest =
                        onDismissMenu
                ) {

                    DropdownMenuItem(
                        text = {
                            Text("Edit Product")
                        },
                        onClick = {
                            onDismissMenu()
                            onClick()
                        }
                    )

                    DropdownMenuItem(
                        text = {
                            Text("Adjust Stock")
                        },
                        onClick = {
                            onDismissMenu()
                            onAdjustStockClick()
                        }
                    )

                    DropdownMenuItem(
                        text = {
                            Text(
                                text = "Delete Product",
                                color =
                                    ProductsOutOfStock
                            )
                        },
                        onClick = {
                            onDismissMenu()
                            onDeleteClick()
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun StockStatusText(
    product: ProductListItem
) {

    when {

        product.isOutOfStock -> {

            Text(
                text = "Out of stock",
                color = ProductsOutOfStock,
                fontSize = 10.sp,
                fontWeight = FontWeight.Medium
            )
        }

        product.isLowStock -> {

            Text(
                text =
                    "Low stock (${product.stockQuantity})",
                color = ProductsLowStock,
                fontSize = 10.sp,
                fontWeight = FontWeight.Medium
            )
        }

        else -> {

            Text(
                text =
                    "${product.stockQuantity} units",
                color = ProductsSecondaryText,
                fontSize = 10.sp
            )
        }
    }
}

@Composable
private fun LoadingProductsState() {

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 100.dp),
        contentAlignment = Alignment.Center
    ) {

        CircularProgressIndicator(
            color = ProductsPrimary
        )
    }
}

@Composable
private fun EmptyProductsState(
    hasSearchOrFilter: Boolean
) {

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                top = 100.dp,
                start = 32.dp,
                end = 32.dp
            ),
        horizontalAlignment =
            Alignment.CenterHorizontally
    ) {

        Box(
            modifier = Modifier
                .size(64.dp)
                .background(
                    color = Color(0xFFEEF3ED),
                    shape = CircleShape
                ),
            contentAlignment =
                Alignment.Center
        ) {

            Text(
                text = "□",
                color = ProductsPrimary,
                fontSize = 30.sp
            )
        }

        Spacer(
            modifier = Modifier.height(16.dp)
        )

        Text(
            text =
                if (hasSearchOrFilter) {
                    "No matching products"
                } else {
                    "No products yet"
                },
            color = ProductsPrimary,
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp
        )

        Spacer(
            modifier = Modifier.height(6.dp)
        )

        Text(
            text =
                if (hasSearchOrFilter) {
                    "Try changing your search or category filter."
                } else {
                    "Add your first product to start tracking sales and stock."
                },
            color = ProductsSecondaryText,
            fontSize = 12.sp
        )
    }
}

@Composable
private fun AddProductBottomButton(
    onClick: () -> Unit
) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                bottom = 18.dp
            ),
        horizontalArrangement =
            Arrangement.Center
    ) {

        Surface(
            modifier = Modifier
                .size(58.dp)
                .clickable(
                    onClick = onClick
                ),
            shape = CircleShape,
            color = ProductsPrimary
        ) {

            Box(
                contentAlignment =
                    Alignment.Center
            ) {

                Icon(
                    imageVector =
                        Icons.Default.Add,
                    contentDescription =
                        "Add Product",
                    tint = Color.White,
                    modifier =
                        Modifier.size(28.dp)
                )
            }
        }
    }
}

@Composable
private fun DeleteProductDialog(
    product: ProductListItem,
    isDeleting: Boolean,
    onClose: () -> Unit,
    onConfirmDelete: () -> Unit
) {

    AlertDialog(
        onDismissRequest = {
            if (!isDeleting) {
                onClose()
            }
        },
        title = {

            Text(
                text = product.name,
                fontWeight =
                    FontWeight.SemiBold
            )
        },
        text = {

            Text(
                text =
                    "Are you sure you want to delete this product?"
            )
        },
        dismissButton = {

            OutlinedButton(
                onClick = onClose,
                enabled = !isDeleting
            ) {
                Text("Close")
            }
        },
        confirmButton = {

            Button(
                onClick = onConfirmDelete,
                enabled = !isDeleting,
                colors =
                    ButtonDefaults.buttonColors(
                        containerColor =
                            ProductsOutOfStock
                    )
            ) {

                if (isDeleting) {

                    CircularProgressIndicator(
                        modifier =
                            Modifier.size(18.dp),
                        strokeWidth = 2.dp,
                        color = Color.White
                    )

                } else {

                    Text("Delete Product")
                }
            }
        },
        containerColor = ProductsSurface
    )
}

private fun formatPrice(
    value: Double
): String {

    return String.format(
        Locale.US,
        "%.2f",
        value
    )
}

@Preview(
    name = "Products - Populated",
    showBackground = true,
    widthDp = 393,
    heightDp = 852
)
@Composable
private fun ProductsPopulatedPreview() {

    BGrothTheme {

        ProductsContent(
            uiState = ProductsUiState(
                products = listOf(
                    ProductListItem(
                        id = 1,
                        name = "Potato Chips",
                        categoryId = 1,
                        categoryName = "Snacks",
                        price = 1.80,
                        stockQuantity = 24,
                        minStockLevel = 10
                    ),
                    ProductListItem(
                        id = 2,
                        name = "Thermal Cup",
                        categoryId = 2,
                        categoryName = "Cups & Mugs",
                        price = 18.00,
                        stockQuantity = 4,
                        minStockLevel = 5
                    ),
                    ProductListItem(
                        id = 3,
                        name = "Turkish Coffee",
                        categoryId = 3,
                        categoryName = "Coffee & Tea",
                        price = 21.00,
                        stockQuantity = 0,
                        minStockLevel = 5
                    )
                )
            ),
            onBackClick = {},
            onAddProductClick = {},
            onProductClick = {},
            onAdjustStockClick = {},
            onSearchQueryChange = {},
            onCategorySelected = {},
            onOpenProductMenu = {},
            onCloseProductMenu = {},
            onRequestDeleteProduct = {},
            onCancelDeleteProduct = {},
            onConfirmDeleteProduct = {}
        )
    }
}

@Preview(
    name = "Products - Empty",
    showBackground = true,
    widthDp = 393,
    heightDp = 852
)
@Composable
private fun ProductsEmptyPreview() {

    BGrothTheme {

        ProductsContent(
            uiState = ProductsUiState(),
            onBackClick = {},
            onAddProductClick = {},
            onProductClick = {},
            onAdjustStockClick = {},
            onSearchQueryChange = {},
            onCategorySelected = {},
            onOpenProductMenu = {},
            onCloseProductMenu = {},
            onRequestDeleteProduct = {},
            onCancelDeleteProduct = {},
            onConfirmDeleteProduct = {}
        )
    }
}