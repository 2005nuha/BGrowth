package com.example.bgrowth.ui.dashboard

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.bgrowth.R
import com.example.bgrowth.ui.theme.BGrowthBorder
import com.example.bgrowth.ui.theme.BGrowthError
import com.example.bgrowth.ui.theme.BGrowthMutedText
import com.example.bgrowth.ui.theme.BGrowthSecondaryText
import com.example.bgrowth.ui.theme.BGrowthSoftSurface
import com.example.bgrowth.ui.theme.BGrothTheme
import kotlinx.coroutines.launch

/**
 * Dashboard-level shell that owns the drawer state while leaving [DashboardScreen] unchanged.
 */
@Composable
fun DashboardDrawer(
    userName: String,
    secondaryAccountText: String?,
    onProfileClick: () -> Unit,
    onNotificationsClick: () -> Unit,
    onBusinessSettingsClick: () -> Unit,
    onLanguageClick: () -> Unit,
    onDarkModeClick: () -> Unit,
    onPrivacySecurityClick: () -> Unit,
    onLogoutClick: () -> Unit,
    modifier: Modifier = Modifier,
    isDarkModeEnabled: Boolean = false,
    content: @Composable (onMenuClick: () -> Unit) -> Unit
) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val coroutineScope = rememberCoroutineScope()

    fun closeDrawerAndRun(action: () -> Unit) {
        coroutineScope.launch {
            drawerState.close()
            action()
        }
    }

    BackHandler(enabled = drawerState.isOpen) {
        coroutineScope.launch { drawerState.close() }
    }

    BoxWithConstraints(modifier = modifier.fillMaxSize()) {
        val drawerWidth = maxWidth * DRAWER_WIDTH_FRACTION

        ModalNavigationDrawer(
            drawerState = drawerState,
            gesturesEnabled = true,
            scrimColor = Color.Black.copy(alpha = 0.34f),
            drawerContent = {
                ModalDrawerSheet(
                    modifier = Modifier.width(drawerWidth),
                    drawerContainerColor = MaterialTheme.colorScheme.surface
                ) {
                    DashboardDrawerContent(
                        userName = userName,
                        secondaryAccountText = secondaryAccountText,
                        isDarkModeEnabled = isDarkModeEnabled,
                        onProfileClick = { closeDrawerAndRun(onProfileClick) },
                        onNotificationsClick = { closeDrawerAndRun(onNotificationsClick) },
                        onBusinessSettingsClick = { closeDrawerAndRun(onBusinessSettingsClick) },
                        onLanguageClick = { closeDrawerAndRun(onLanguageClick) },
                        onDarkModeClick = { closeDrawerAndRun(onDarkModeClick) },
                        onPrivacySecurityClick = { closeDrawerAndRun(onPrivacySecurityClick) },
                        onLogoutClick = { closeDrawerAndRun(onLogoutClick) }
                    )
                }
            }
        ) {
            content {
                coroutineScope.launch { drawerState.open() }
            }
        }
    }
}

@Composable
fun DashboardDrawerContent(
    userName: String,
    secondaryAccountText: String?,
    onProfileClick: () -> Unit,
    onNotificationsClick: () -> Unit,
    onBusinessSettingsClick: () -> Unit,
    onLanguageClick: () -> Unit,
    onDarkModeClick: () -> Unit,
    onPrivacySecurityClick: () -> Unit,
    onLogoutClick: () -> Unit,
    modifier: Modifier = Modifier,
    isDarkModeEnabled: Boolean = false
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface)
    ) {
        DrawerProfileHeader(
            userName = userName,
            secondaryAccountText = secondaryAccountText
        )

        HorizontalDivider(color = BGrowthBorder)
        Spacer(modifier = Modifier.height(10.dp))

        DrawerMenuItem(
            label = "Personal Profile",
            icon = Icons.Filled.Person,
            onClick = onProfileClick
        )
        DrawerMenuItem(
            label = "Notifications",
            icon = Icons.Filled.Notifications,
            onClick = onNotificationsClick
        )
        DrawerMenuItem(
            label = "Business Settings",
            icon = Icons.Filled.Settings,
            onClick = onBusinessSettingsClick
        )
        DrawerMenuItem(
            label = "Language",
            iconResource = R.drawable.ic_language,
            onClick = onLanguageClick
        )
        DrawerMenuItem(
            label = "Dark Mode",
            iconResource = R.drawable.ic_dark_mode,
            onClick = onDarkModeClick,
            trailingContent = {
                Switch(
                    checked = isDarkModeEnabled,
                    onCheckedChange = { onDarkModeClick() },
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = MaterialTheme.colorScheme.onPrimary,
                        checkedTrackColor = MaterialTheme.colorScheme.primary,
                        uncheckedThumbColor = BGrowthMutedText,
                        uncheckedTrackColor = BGrowthSoftSurface,
                        uncheckedBorderColor = BGrowthBorder
                    )
                )
            }
        )
        DrawerMenuItem(
            label = "Privacy & Security",
            icon = Icons.Filled.Lock,
            onClick = onPrivacySecurityClick
        )

        Spacer(modifier = Modifier.weight(1f))
        HorizontalDivider(color = BGrowthBorder)
        Spacer(modifier = Modifier.height(8.dp))
        DrawerMenuItem(
            label = "Logout",
            icon = Icons.AutoMirrored.Filled.ExitToApp,
            contentColor = BGrowthError,
            onClick = onLogoutClick
        )
        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
private fun DrawerProfileHeader(
    userName: String,
    secondaryAccountText: String?
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 22.dp, vertical = 28.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(58.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.primary),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = userName.trim().firstOrNull()?.uppercase() ?: "B",
                color = MaterialTheme.colorScheme.onPrimary,
                fontSize = 23.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.width(14.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = userName.ifBlank { "BGrowth User" },
                color = MaterialTheme.colorScheme.onSurface,
                fontSize = 17.sp,
                lineHeight = 22.sp,
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            secondaryAccountText
                ?.takeIf { it.isNotBlank() }
                ?.let { secondaryText ->
                    Spacer(modifier = Modifier.height(3.dp))
                    Text(
                        text = secondaryText,
                        color = BGrowthSecondaryText,
                        fontSize = 13.sp,
                        lineHeight = 18.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
        }
    }
}

@Composable
private fun DrawerMenuItem(
    label: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    icon: ImageVector? = null,
    iconResource: Int? = null,
    contentColor: Color = MaterialTheme.colorScheme.onSurface,
    trailingContent: (@Composable () -> Unit)? = null
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(58.dp)
            .clickable(onClick = onClick)
            .padding(horizontal = 22.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(38.dp)
                .background(
                    color = if (contentColor == BGrowthError) {
                        BGrowthError.copy(alpha = 0.10f)
                    } else {
                        BGrowthSoftSurface
                    },
                    shape = RoundedCornerShape(11.dp)
                ),
            contentAlignment = Alignment.Center
        ) {
            when {
                icon != null -> Icon(
                    imageVector = icon,
                    contentDescription = null,
                    modifier = Modifier.size(20.dp),
                    tint = contentColor
                )

                iconResource != null -> Icon(
                    painter = painterResource(iconResource),
                    contentDescription = null,
                    modifier = Modifier.size(20.dp),
                    tint = contentColor
                )
            }
        }

        Spacer(modifier = Modifier.width(14.dp))
        Text(
            text = label,
            modifier = Modifier.weight(1f),
            color = contentColor,
            fontSize = 15.sp,
            lineHeight = 20.sp,
            fontWeight = FontWeight.Medium
        )
        trailingContent?.invoke()
    }
}

@Preview(
    name = "Dashboard Drawer",
    showBackground = true,
    widthDp = 393,
    heightDp = 852
)
@Composable
private fun DashboardDrawerPreview() {
    BGrothTheme(darkTheme = false) {
        DashboardDrawerContent(
            userName = "Ahmad",
            secondaryAccountText = "ahmad@example.com",
            onProfileClick = {},
            onNotificationsClick = {},
            onBusinessSettingsClick = {},
            onLanguageClick = {},
            onDarkModeClick = {},
            onPrivacySecurityClick = {},
            onLogoutClick = {},
            modifier = Modifier
                .width(306.dp)
                .fillMaxHeight()
        )
    }
}

private const val DRAWER_WIDTH_FRACTION = 0.78f
