package com.example.bgrowth.ui.onboarding

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.bgrowth.ui.theme.BGrothTheme
import kotlinx.coroutines.launch

private val OnboardingPrimary = Color(0xFF0F5D46)
private val OnboardingBackground = Color(0xFFF7F8F4)
private val OnboardingSecondaryText = Color(0xFF5E6B63)

@Composable
fun OnboardingPagerScreen(
    onFinished: () -> Unit,
    onSkipClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val pagerState = rememberPagerState(
        pageCount = { onboardingPages.size }
    )
    val coroutineScope = rememberCoroutineScope()
    val currentPage = pagerState.currentPage

    OnboardingLayout(
        currentPage = currentPage,
        pageCount = onboardingPages.size,
        buttonText = onboardingPages[currentPage].buttonText,
        onPrimaryClick = {
            val visiblePage = pagerState.currentPage
            if (visiblePage < onboardingPages.lastIndex) {
                coroutineScope.launch {
                    pagerState.animateScrollToPage(visiblePage + 1)
                }
            } else {
                onFinished()
            }
        },
        onSkipClick = onSkipClick,
        modifier = modifier
    ) { compactHeight ->
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.weight(1f),
            userScrollEnabled = true
        ) { pageIndex ->
            OnboardingPageContent(
                page = onboardingPages[pageIndex],
                compactHeight = compactHeight,
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}

@Composable
fun OnboardingScreen(
    page: OnboardingPage,
    currentPage: Int,
    pageCount: Int,
    onPrimaryClick: () -> Unit,
    onSkipClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    OnboardingLayout(
        currentPage = currentPage,
        pageCount = pageCount,
        buttonText = page.buttonText,
        onPrimaryClick = onPrimaryClick,
        onSkipClick = onSkipClick,
        modifier = modifier
    ) { compactHeight ->
        OnboardingPageContent(
            page = page,
            compactHeight = compactHeight,
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
private fun OnboardingLayout(
    currentPage: Int,
    pageCount: Int,
    buttonText: String,
    onPrimaryClick: () -> Unit,
    onSkipClick: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.(compactHeight: Boolean) -> Unit
) {
    BoxWithConstraints(
        modifier = modifier
            .fillMaxSize()
            .background(OnboardingBackground)
            .safeDrawingPadding()
    ) {
        val compactHeight = maxHeight < 650.dp

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            content(compactHeight)

            OnboardingIndicator(
                currentPage = currentPage,
                pageCount = pageCount
            )

            Spacer(modifier = Modifier.height(if (compactHeight) 16.dp else 26.dp))

            Button(
                onClick = onPrimaryClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = OnboardingPrimary,
                    contentColor = Color.White
                ),
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 0.dp)
            ) {
                Text(
                    text = buttonText,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }

            Spacer(modifier = Modifier.height(4.dp))

            TextButton(onClick = onSkipClick) {
                Text(
                    text = "Skip",
                    color = OnboardingSecondaryText,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Medium
                )
            }

            Spacer(modifier = Modifier.height(if (compactHeight) 4.dp else 10.dp))
        }
    }
}

@Composable
private fun OnboardingPageContent(
    page: OnboardingPage,
    compactHeight: Boolean,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(if (compactHeight) 14.dp else 32.dp))

        Text(
            text = page.title,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 4.dp),
            color = OnboardingPrimary,
            fontSize = if (compactHeight) 24.sp else 27.sp,
            fontWeight = FontWeight.Bold,
            lineHeight = if (compactHeight) 30.sp else 34.sp,
            letterSpacing = (-0.25).sp,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(if (compactHeight) 8.dp else 12.dp))

        Text(
            text = page.description,
            modifier = Modifier.widthIn(max = 380.dp),
            color = OnboardingSecondaryText,
            fontSize = 16.sp,
            fontWeight = FontWeight.Normal,
            lineHeight = 24.sp,
            textAlign = TextAlign.Center
        )

        Image(
            painter = painterResource(page.imageRes),
            contentDescription = page.title,
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(
                    horizontal = if (compactHeight) 12.dp else 4.dp,
                    vertical = if (compactHeight) 8.dp else 18.dp
                ),
            contentScale = ContentScale.Fit
        )
    }
}

@Preview(
    name = "Onboarding Page 1",
    showBackground = true,
    widthDp = 390,
    heightDp = 844
)
@Composable
private fun OnboardingPage1Preview() {
    BGrothTheme {
        OnboardingScreen(
            page = onboardingPages[0],
            currentPage = 0,
            pageCount = onboardingPages.size,
            onPrimaryClick = {},
            onSkipClick = {}
        )
    }
}

@Preview(
    name = "Onboarding Page 2",
    showBackground = true,
    widthDp = 390,
    heightDp = 844
)
@Composable
private fun OnboardingPage2Preview() {
    BGrothTheme {
        OnboardingScreen(
            page = onboardingPages[1],
            currentPage = 1,
            pageCount = onboardingPages.size,
            onPrimaryClick = {},
            onSkipClick = {}
        )
    }
}

@Preview(
    name = "Onboarding Page 3",
    showBackground = true,
    widthDp = 390,
    heightDp = 844
)
@Composable
private fun OnboardingPage3Preview() {
    BGrothTheme {
        OnboardingScreen(
            page = onboardingPages[2],
            currentPage = 2,
            pageCount = onboardingPages.size,
            onPrimaryClick = {},
            onSkipClick = {}
        )
    }
}
