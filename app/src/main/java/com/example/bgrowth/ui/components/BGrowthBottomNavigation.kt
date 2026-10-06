package com.example.bgrowth.ui.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.bgrowth.R
import com.example.bgrowth.ui.theme.BGrowthMutedText
import com.example.bgrowth.ui.theme.BGrowthPrimary
import com.example.bgrowth.ui.theme.BGrowthSurface

enum class BottomNavItem {
    DASHBOARD,
    SALES,
    PRODUCTS,
    BUSINESS
}

@Composable
fun BGrowthBottomNavigation(
    selectedItem: BottomNavItem,
    onDashboardClick: () -> Unit,
    onSalesClick: () -> Unit,
    onAddClick: () -> Unit,
    onProductsClick: () -> Unit,
    onBusinessClick: () -> Unit,
    modifier: Modifier = Modifier
) {

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(108.dp)
    ) {

        Surface(
            color = BGrowthSurface,
            modifier = Modifier
                .fillMaxWidth()
                .height(88.dp)
                .align(Alignment.BottomCenter)
        ) {

            Box(
                modifier = Modifier.fillMaxWidth()
            ) {

                BottomNavigationTopLine(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(32.dp)
                        .align(Alignment.TopCenter)
                        .offset(y = 6.dp)
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            start = 12.dp,
                            end = 12.dp,
                            top = 18.dp,
                            bottom = 12.dp
                        ),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    BottomNavigationItem(
                        label = "Dashboard",
                        icon = R.drawable.ic_nav_dashboard,
                        selected = selectedItem == BottomNavItem.DASHBOARD,
                        onClick = onDashboardClick
                    )

                    BottomNavigationItem(
                        label = "Sales",
                        icon = R.drawable.ic_nav_sales,
                        selected = selectedItem == BottomNavItem.SALES,
                        onClick = onSalesClick
                    )

                    Spacer(
                        modifier = Modifier.size(64.dp)
                    )

                    BottomNavigationItem(
                        label = "Products",
                        icon = R.drawable.ic_nav_products,
                        selected = selectedItem == BottomNavItem.PRODUCTS,
                        onClick = onProductsClick
                    )

                    BottomNavigationItem(
                        label = "Business",
                        icon = R.drawable.ic_nav_business,
                        selected = selectedItem == BottomNavItem.BUSINESS,
                        onClick = onBusinessClick
                    )
                }
            }
        }

        Surface(
            modifier = Modifier
                .size(58.dp)
                .align(Alignment.TopCenter)
                .offset(y = (-8).dp)
                .clickable(
                    onClick = onAddClick
                ),
            shape = CircleShape,
            color = BGrowthPrimary,
            shadowElevation = 5.dp
        ) {

            Box(
                contentAlignment = Alignment.Center
            ) {

                Icon(
                    painter = painterResource(
                        id = R.drawable.ic_nav_add
                    ),
                    contentDescription = "Add",
                    tint = Color.White,
                    modifier = Modifier.size(28.dp)
                )
            }
        }
    }
}

@Composable
private fun BottomNavigationItem(
    label: String,
    @DrawableRes icon: Int,
    selected: Boolean,
    onClick: () -> Unit
) {

    val itemColor =
        if (selected) {
            BGrowthPrimary
        } else {
            BGrowthMutedText
        }

    Column(
        modifier = Modifier
            .clickable(
                onClick = onClick
            )
            .padding(
                horizontal = 4.dp,
                vertical = 2.dp
            ),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Icon(
            painter = painterResource(
                id = icon
            ),
            contentDescription = label,
            tint = itemColor,
            modifier = Modifier.size(25.dp)
        )

        Spacer(
            modifier = Modifier.height(3.dp)
        )

        Text(
            text = label,
            color = itemColor,
            fontSize = 10.sp,
            lineHeight = 12.sp,
            fontWeight =
                if (selected) {
                    FontWeight.Medium
                } else {
                    FontWeight.Normal
                },
            maxLines = 1,
            overflow = TextOverflow.Clip
        )
    }
}

@Composable
private fun BottomNavigationTopLine(
    modifier: Modifier = Modifier
) {

    Canvas(
        modifier = modifier
    ) {

        val centerX = size.width / 2f

        val notchWidth = 82.dp.toPx()
        val notchDepth = 27.dp.toPx()

        val leftNotchStart =
            centerX - notchWidth / 2f

        val rightNotchEnd =
            centerX + notchWidth / 2f

        val topY = 1.dp.toPx()

        val path = Path()

        path.moveTo(
            0f,
            topY
        )

        path.lineTo(
            leftNotchStart,
            topY
        )

        path.cubicTo(
            leftNotchStart,
            topY,
            leftNotchStart,
            notchDepth,
            centerX,
            notchDepth
        )

        path.cubicTo(
            rightNotchEnd,
            notchDepth,
            rightNotchEnd,
            topY,
            rightNotchEnd,
            topY
        )

        path.lineTo(
            size.width,
            topY
        )

        drawPath(
            path = path,
            color = BGrowthPrimary,
            style = Stroke(
                width = 1.3.dp.toPx()
            )
        )
    }
}