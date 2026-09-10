package com.example.bgrowth.ui.dashboard

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.bgrowth.ui.theme.BGrowthAccent
import com.example.bgrowth.ui.theme.BGrowthBorder
import com.example.bgrowth.ui.theme.BGrowthError
import com.example.bgrowth.ui.theme.BGrowthMutedText
import com.example.bgrowth.ui.theme.BGrowthSecondaryText
import com.example.bgrowth.ui.theme.BGrothTheme
import java.text.NumberFormat
import java.util.Locale

@Composable
fun DashboardScreen(
    onMenuClick: () -> Unit,
    onAddProductClick: () -> Unit,
    onRecordSaleClick: () -> Unit,
    onAddExpenseClick: () -> Unit,
    onViewReportsClick: () -> Unit,
    onLowStockClick: () -> Unit,
    onMainAddClick: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: DashboardViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    DashboardScreen(
        uiState = uiState,
        onMenuClick = onMenuClick,
        onAddProductClick = onAddProductClick,
        onRecordSaleClick = onRecordSaleClick,
        onAddExpenseClick = onAddExpenseClick,
        onViewReportsClick = onViewReportsClick,
        onLowStockClick = onLowStockClick,
        onMainAddClick = onMainAddClick,
        modifier = modifier
    )
}

@Composable
fun DashboardScreen(
    uiState: DashboardUiState,
    onMenuClick: () -> Unit,
    onAddProductClick: () -> Unit,
    onRecordSaleClick: () -> Unit,
    onAddExpenseClick: () -> Unit,
    onViewReportsClick: () -> Unit,
    onLowStockClick: () -> Unit,
    onMainAddClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val colors = MaterialTheme.colorScheme

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(colors.background)
            .safeDrawingPadding()
    ) {
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            contentAlignment = Alignment.TopCenter
        ) {
            if (uiState.isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center),
                    color = colors.primary
                )
            } else {
                DashboardContent(
                    uiState = uiState,
                    onMenuClick = onMenuClick,
                    onAddProductClick = onAddProductClick,
                    onRecordSaleClick = onRecordSaleClick,
                    onAddExpenseClick = onAddExpenseClick,
                    onViewReportsClick = onViewReportsClick,
                    onLowStockClick = onLowStockClick
                )
            }
        }

        BottomNavigationBar(onMainAddClick = onMainAddClick)
    }
}

@Composable
private fun DashboardContent(
    uiState: DashboardUiState,
    onMenuClick: () -> Unit,
    onAddProductClick: () -> Unit,
    onRecordSaleClick: () -> Unit,
    onAddExpenseClick: () -> Unit,
    onViewReportsClick: () -> Unit,
    onLowStockClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxHeight()
            .widthIn(max = 720.dp)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp)
    ) {
        DashboardHeader(
            userName = uiState.userName,
            onMenuClick = onMenuClick
        )

        uiState.errorMessage?.let { message ->
            ErrorMessageCard(message = message)
            Spacer(modifier = Modifier.height(16.dp))
        }

        if (uiState.hasBusinessData) {
            PopulatedDashboardContent(
                uiState = uiState,
                onLowStockClick = onLowStockClick
            )
        } else {
            EmptyDashboardCard(
                onAddProductClick = onAddProductClick,
                onRecordSaleClick = onRecordSaleClick
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        SectionTitle(text = "Quick Actions")
        Spacer(modifier = Modifier.height(12.dp))
        QuickActionsGrid(
            onRecordSaleClick = onRecordSaleClick,
            onAddExpenseClick = onAddExpenseClick,
            onAddProductClick = onAddProductClick,
            onViewReportsClick = onViewReportsClick
        )

        if (!uiState.hasBusinessData) {
            Spacer(modifier = Modifier.height(20.dp))
            NextStepsCard()
        }

        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Composable
private fun DashboardHeader(
    userName: String,
    onMenuClick: () -> Unit
) {
    val colors = MaterialTheme.colorScheme

    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(68.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            BrandLogo()
            Spacer(modifier = Modifier.width(10.dp))
            Text(
                text = "BGrowth",
                color = colors.primary,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = (-0.4).sp
            )
            Spacer(modifier = Modifier.weight(1f))
            IconButton(onClick = onMenuClick) {
                Icon(
                    imageVector = Icons.Filled.Menu,
                    contentDescription = "Open menu",
                    tint = colors.onBackground
                )
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        Text(
            text = "Hi ${userName.ifBlank { "there" }}!",
            color = colors.onBackground,
            fontSize = 26.sp,
            lineHeight = 32.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(5.dp))
        Text(
            text = "Here's a quick look at how your business is doing today.",
            color = BGrowthSecondaryText,
            fontSize = 14.sp,
            lineHeight = 21.sp
        )
        Spacer(modifier = Modifier.height(22.dp))
    }
}

@Composable
private fun BrandLogo() {
    val colors = MaterialTheme.colorScheme
    Box(
        modifier = Modifier
            .size(40.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(colors.primary),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "B",
            color = Color.White,
            fontSize = 27.sp,
            lineHeight = 30.sp,
            fontWeight = FontWeight.Black
        )
        Row(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(start = 7.dp, bottom = 6.dp),
            horizontalArrangement = Arrangement.spacedBy(2.dp),
            verticalAlignment = Alignment.Bottom
        ) {
            listOf(4.dp, 7.dp, 10.dp).forEach { barHeight ->
                Box(
                    modifier = Modifier
                        .width(3.dp)
                        .height(barHeight)
                        .background(BGrowthAccent, RoundedCornerShape(topStart = 2.dp, topEnd = 2.dp))
                )
            }
        }
    }
}

@Composable
private fun EmptyDashboardCard(
    onAddProductClick: () -> Unit,
    onRecordSaleClick: () -> Unit
) {
    val colors = MaterialTheme.colorScheme
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        color = colors.surface,
        border = BorderStroke(1.dp, BGrowthBorder),
        shadowElevation = 1.dp
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 22.dp, vertical = 26.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(68.dp)
                    .background(colors.surfaceVariant, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Filled.Home,
                    contentDescription = null,
                    modifier = Modifier.size(32.dp),
                    tint = colors.primary
                )
            }
            Spacer(modifier = Modifier.height(18.dp))
            Text(
                text = "Your dashboard is ready",
                color = colors.onSurface,
                fontSize = 20.sp,
                lineHeight = 26.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Start by adding your first product or recording your first sale.",
                color = BGrowthSecondaryText,
                fontSize = 14.sp,
                lineHeight = 21.sp,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(22.dp))
            Button(
                onClick = onAddProductClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = colors.primary,
                    contentColor = colors.onPrimary
                ),
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 0.dp)
            ) {
                Icon(
                    imageVector = Icons.Filled.Add,
                    contentDescription = null,
                    modifier = Modifier.size(19.dp)
                )
                Spacer(modifier = Modifier.width(7.dp))
                Text("Add First Product", fontWeight = FontWeight.SemiBold)
            }
            Spacer(modifier = Modifier.height(10.dp))
            OutlinedButton(
                onClick = onRecordSaleClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(14.dp),
                border = BorderStroke(1.dp, colors.primary),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = colors.primary)
            ) {
                Text("Record First sale", fontWeight = FontWeight.SemiBold)
            }
        }
    }
}

@Composable
private fun PopulatedDashboardContent(
    uiState: DashboardUiState,
    onLowStockClick: () -> Unit
) {
    SectionTitle(text = "Today's Summary")
    Spacer(modifier = Modifier.height(12.dp))

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        SummaryCard(
            title = "Today's Sales",
            value = formatCurrency(uiState.todaySales),
            trendPercent = uiState.salesTrendPercent,
            positiveTrendIsFavorable = true,
            icon = Icons.Filled.ShoppingCart,
            modifier = Modifier.weight(1f)
        )
        SummaryCard(
            title = "Today's Expenses",
            value = formatCurrency(uiState.todayExpenses),
            trendPercent = uiState.expensesTrendPercent,
            positiveTrendIsFavorable = false,
            icon = Icons.AutoMirrored.Filled.List,
            modifier = Modifier.weight(1f)
        )
    }

    Spacer(modifier = Modifier.height(12.dp))
    NetProfitCard(value = uiState.netProfit)
    Spacer(modifier = Modifier.height(12.dp))
    LowStockCard(count = uiState.lowStockCount, onClick = onLowStockClick)
}

@Composable
private fun SummaryCard(
    title: String,
    value: String,
    trendPercent: Double?,
    positiveTrendIsFavorable: Boolean,
    icon: ImageVector,
    modifier: Modifier = Modifier
) {
    val colors = MaterialTheme.colorScheme
    Surface(
        modifier = modifier.height(146.dp),
        shape = RoundedCornerShape(18.dp),
        color = colors.surface,
        border = BorderStroke(1.dp, BGrowthBorder)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(34.dp)
                        .background(colors.surfaceVariant, RoundedCornerShape(10.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp),
                        tint = colors.primary
                    )
                }
                Spacer(modifier = Modifier.weight(1f))
                trendPercent?.let { trend ->
                    val favorable = if (positiveTrendIsFavorable) trend >= 0 else trend <= 0
                    Text(
                        text = formatTrend(trend),
                        color = if (favorable) colors.secondary else BGrowthError,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
            Spacer(modifier = Modifier.height(14.dp))
            Text(
                text = title,
                color = BGrowthSecondaryText,
                fontSize = 12.sp,
                lineHeight = 17.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.height(3.dp))
            Text(
                text = value,
                color = colors.onSurface,
                fontSize = 21.sp,
                lineHeight = 27.sp,
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
private fun NetProfitCard(value: Double) {
    val colors = MaterialTheme.colorScheme
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        color = colors.primary
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 18.dp, vertical = 17.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(42.dp)
                    .background(Color.White.copy(alpha = 0.14f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Filled.Info,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(21.dp)
                )
            }
            Spacer(modifier = Modifier.width(13.dp))
            Column {
                Text(
                    text = "Net Profit",
                    color = Color.White.copy(alpha = 0.78f),
                    fontSize = 13.sp
                )
                Text(
                    text = formatCurrency(value),
                    color = Color.White,
                    fontSize = 24.sp,
                    lineHeight = 29.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
private fun LowStockCard(count: Int, onClick: () -> Unit) {
    val colors = MaterialTheme.colorScheme
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(18.dp))
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(18.dp),
        color = Color(0xFFFFF7E7),
        border = BorderStroke(1.dp, Color(0xFFF1DFC0))
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(42.dp)
                    .background(Color(0xFFFFE9BE), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "!",
                    color = Color(0xFF9A6512),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            Spacer(modifier = Modifier.width(13.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Low Stock Alert",
                    color = colors.onSurface,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = "$count ${if (count == 1) "item is" else "items are"} running low. Tap to restock.",
                    color = BGrowthSecondaryText,
                    fontSize = 12.sp,
                    lineHeight = 18.sp
                )
            }
            Text(
                text = "›",
                color = Color(0xFF9A6512),
                fontSize = 26.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@Composable
private fun QuickActionsGrid(
    onRecordSaleClick: () -> Unit,
    onAddExpenseClick: () -> Unit,
    onAddProductClick: () -> Unit,
    onViewReportsClick: () -> Unit
) {
    val actions = listOf(
        QuickAction("Record Sale", Icons.Filled.ShoppingCart, onRecordSaleClick),
        QuickAction("Add Expense", Icons.AutoMirrored.Filled.List, onAddExpenseClick),
        QuickAction("Add Product", Icons.Filled.Add, onAddProductClick),
        QuickAction("View Reports", Icons.Filled.Info, onViewReportsClick)
    )

    BoxWithConstraints(modifier = Modifier.fillMaxWidth()) {
        if (maxWidth < 350.dp) {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                actions.chunked(2).forEach { rowActions ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        rowActions.forEach { action ->
                            QuickActionItem(action = action, modifier = Modifier.weight(1f))
                        }
                    }
                }
            }
        } else {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                actions.forEach { action ->
                    QuickActionItem(action = action, modifier = Modifier.weight(1f))
                }
            }
        }
    }
}

@Composable
private fun QuickActionItem(action: QuickAction, modifier: Modifier = Modifier) {
    val colors = MaterialTheme.colorScheme
    Surface(
        modifier = modifier
            .height(102.dp)
            .clip(RoundedCornerShape(16.dp))
            .clickable(onClick = action.onClick),
        shape = RoundedCornerShape(16.dp),
        color = colors.surface,
        border = BorderStroke(1.dp, BGrowthBorder)
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 5.dp, vertical = 13.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .size(38.dp)
                    .background(colors.surfaceVariant, RoundedCornerShape(11.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = action.icon,
                    contentDescription = null,
                    modifier = Modifier.size(20.dp),
                    tint = colors.primary
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = action.label,
                color = colors.onSurface,
                fontSize = 11.sp,
                lineHeight = 14.sp,
                fontWeight = FontWeight.SemiBold,
                textAlign = TextAlign.Center,
                maxLines = 2
            )
        }
    }
}

@Composable
private fun NextStepsCard() {
    val colors = MaterialTheme.colorScheme
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        color = colors.surfaceVariant,
        border = BorderStroke(1.dp, BGrowthBorder)
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            Text(
                text = "What happens next ?",
                color = colors.onSurface,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(15.dp))
            listOf(
                "Add Products",
                "Record sales",
                "Track profit automatically"
            ).forEachIndexed { index, label ->
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(28.dp)
                            .background(colors.primary, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = (index + 1).toString(),
                            color = colors.onPrimary,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = label,
                        color = BGrowthSecondaryText,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
                if (index < 2) Spacer(modifier = Modifier.height(12.dp))
            }
        }
    }
}

@Composable
private fun BottomNavigationBar(onMainAddClick: () -> Unit) {
    val colors = MaterialTheme.colorScheme
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(76.dp)
            .shadow(elevation = 10.dp),
        color = colors.surface,
        border = BorderStroke(1.dp, BGrowthBorder)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            BottomNavigationItem(
                label = "Dashboard",
                icon = Icons.Filled.Home,
                selected = true,
                modifier = Modifier.weight(1f)
            )
            BottomNavigationItem(
                label = "Sales",
                icon = Icons.AutoMirrored.Filled.List,
                selected = false,
                modifier = Modifier.weight(1f)
            )
            Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.Center) {
                Surface(
                    modifier = Modifier
                        .size(54.dp)
                        .clip(CircleShape)
                        .clickable(onClick = onMainAddClick),
                    shape = CircleShape,
                    color = colors.primary,
                    shadowElevation = 5.dp
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = Icons.Filled.Add,
                            contentDescription = "Add",
                            modifier = Modifier.size(28.dp),
                            tint = colors.onPrimary
                        )
                    }
                }
            }
            BottomNavigationItem(
                label = "Products",
                icon = Icons.Filled.ShoppingCart,
                selected = false,
                modifier = Modifier.weight(1f)
            )
            BottomNavigationItem(
                label = "Business",
                icon = Icons.Filled.Home,
                selected = false,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
private fun BottomNavigationItem(
    label: String,
    icon: ImageVector,
    selected: Boolean,
    modifier: Modifier = Modifier
) {
    val colors = MaterialTheme.colorScheme
    val tint = if (selected) colors.primary else BGrowthMutedText

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            modifier = Modifier.size(22.dp),
            tint = tint
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = label,
            color = tint,
            fontSize = 9.sp,
            lineHeight = 11.sp,
            fontWeight = if (selected) FontWeight.Bold else FontWeight.Medium,
            maxLines = 1
        )
    }
}

@Composable
private fun SectionTitle(text: String) {
    Text(
        text = text,
        color = MaterialTheme.colorScheme.onBackground,
        fontSize = 18.sp,
        lineHeight = 24.sp,
        fontWeight = FontWeight.Bold
    )
}

@Composable
private fun ErrorMessageCard(message: String) {
    Text(
        text = message,
        modifier = Modifier
            .fillMaxWidth()
            .background(BGrowthError.copy(alpha = 0.09f), RoundedCornerShape(12.dp))
            .border(1.dp, BGrowthError.copy(alpha = 0.25f), RoundedCornerShape(12.dp))
            .padding(horizontal = 14.dp, vertical = 11.dp),
        color = BGrowthError,
        fontSize = 13.sp,
        lineHeight = 18.sp
    )
}

private data class QuickAction(
    val label: String,
    val icon: ImageVector,
    val onClick: () -> Unit
)

private fun formatCurrency(value: Double): String =
    NumberFormat.getCurrencyInstance(Locale.US).format(value)

private fun formatTrend(value: Double): String {
    val amount = if (value % 1.0 == 0.0) value.toInt().toString() else "%.1f".format(value)
    return "${if (value > 0) "+" else ""}$amount%"
}

@Preview(
    name = "Dashboard - Empty",
    showBackground = true,
    widthDp = 393,
    heightDp = 852
)
@Composable
private fun EmptyDashboardPreview() {
    BGrothTheme(darkTheme = false) {
        DashboardPreview(
            uiState = DashboardUiState(
                userName = "Ahmad",
                hasBusinessData = false
            )
        )
    }
}

@Preview(
    name = "Dashboard - Populated",
    showBackground = true,
    widthDp = 393,
    heightDp = 852
)
@Composable
private fun PopulatedDashboardPreview() {
    BGrothTheme(darkTheme = false) {
        DashboardPreview(
            uiState = DashboardUiState(
                userName = "Ahmad",
                hasBusinessData = true,
                todaySales = 450.0,
                todayExpenses = 180.0,
                netProfit = 330.0,
                salesTrendPercent = 12.0,
                expensesTrendPercent = -5.0,
                lowStockCount = 2
            )
        )
    }
}

@Composable
private fun DashboardPreview(uiState: DashboardUiState) {
    DashboardScreen(
        uiState = uiState,
        onMenuClick = {},
        onAddProductClick = {},
        onRecordSaleClick = {},
        onAddExpenseClick = {},
        onViewReportsClick = {},
        onLowStockClick = {},
        onMainAddClick = {}
    )
}
