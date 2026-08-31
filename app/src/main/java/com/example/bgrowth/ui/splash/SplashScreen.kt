package com.example.bgrowth.ui.splash

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.material3.Text
import com.example.bgrowth.R

private val Secondary = Color(0xFF1F8A61)
private val Accent = Color(0xFF43C389)
private val Background = Color(0xFFF7F8F4)
private val SecondaryText = Color(0xFF5E6B63)

@Composable
fun SplashScreen(modifier: Modifier = Modifier) {
    BoxWithConstraints(
        modifier = modifier
            .fillMaxSize()
            .background(Background)
    ) {
        WaveDecoration(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .fillMaxHeight(0.24f)
        )

        val logoWidth = (maxWidth * 0.92f).coerceAtMost(520.dp)

        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(R.drawable.logo),
                contentDescription = "BGrowth logo",
                modifier = Modifier
                    .width(logoWidth)
                    .aspectRatio(1.5f),
                contentScale = ContentScale.Fit
            )

            Spacer(modifier = Modifier.height(2.dp))


        }
    }
}

@Composable
private fun WaveDecoration(modifier: Modifier = Modifier) {
    Canvas(modifier = modifier) {
        val w = size.width
        val h = size.height

        fun wave(
            startY: Float,
            firstControlY: Float,
            middleY: Float,
            secondControlY: Float,
            endY: Float
        ) = Path().apply {
            moveTo(-w * 0.05f, h * startY)
            cubicTo(
                w * 0.15f, h * firstControlY,
                w * 0.34f, h * middleY,
                w * 0.52f, h * middleY
            )
            cubicTo(
                w * 0.70f, h * middleY,
                w * 0.86f, h * secondControlY,
                w * 1.05f, h * endY
            )
        }

        drawPath(
            path = wave(0.28f, 0.02f, 0.34f, 0.68f, 0.38f),
            color = Secondary.copy(alpha = 0.14f),
            style = Stroke(width = 1.25.dp.toPx(), cap = StrokeCap.Round)
        )
        drawPath(
            path = wave(0.46f, 0.18f, 0.51f, 0.82f, 0.55f),
            color = Accent.copy(alpha = 0.16f),
            style = Stroke(width = 1.5.dp.toPx(), cap = StrokeCap.Round)
        )
        drawPath(
            path = wave(0.64f, 0.38f, 0.68f, 0.92f, 0.72f),
            color = Secondary.copy(alpha = 0.10f),
            style = Stroke(width = 1.dp.toPx(), cap = StrokeCap.Round)
        )
        drawPath(
            path = wave(0.82f, 0.60f, 0.84f, 1.02f, 0.88f),
            color = Accent.copy(alpha = 0.11f),
            style = Stroke(width = 1.15.dp.toPx(), cap = StrokeCap.Round)
        )
    }
}

@Preview(showBackground = true, widthDp = 360, heightDp = 800)
@Composable
private fun SplashScreenPreview() {
    SplashScreen()
}
