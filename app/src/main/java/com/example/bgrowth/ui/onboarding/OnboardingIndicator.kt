package com.example.bgrowth.ui.onboarding

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp

private val IndicatorGreen = Color(0xFF0F5D46)

@Composable
fun OnboardingIndicator(
    currentPage: Int,
    pageCount: Int,
    modifier: Modifier = Modifier
) {
    if (pageCount <= 0) return

    val selectedPage = currentPage.coerceIn(0, pageCount - 1)

    Row(
        modifier = modifier.semantics {
            contentDescription = "Page ${selectedPage + 1} of $pageCount"
        },
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        repeat(pageCount) { index ->
            Box(
                modifier = Modifier
                    .width(if (index == selectedPage) 24.dp else 8.dp)
                    .height(8.dp)
                    .clip(RoundedCornerShape(50))
                    .background(
                        if (index == selectedPage) {
                            IndicatorGreen
                        } else {
                            IndicatorGreen.copy(alpha = 0.18f)
                        }
                    )
            )
        }
    }
}
