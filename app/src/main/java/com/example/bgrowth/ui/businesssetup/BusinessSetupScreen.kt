package com.example.bgrowth.ui.businesssetup

import android.graphics.BitmapFactory
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.PathParser
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.bgrowth.ui.theme.BGrothTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

private val BusinessSetupPrimary = Color(0xFF0F5D46)
private val BusinessSetupSecondary = Color(0xFF1F8A61)
private val BusinessSetupBackground = Color(0xFFF7F8F4)
private val BusinessSetupSurface = Color(0xFFFFFFFF)
private val BusinessSetupSoftSurface = Color(0xFFEEF3ED)
private val BusinessSetupBorder = Color(0xFFD9E3DA)
private val BusinessSetupSecondaryText = Color(0xFF5E6B63)
private val BusinessSetupMutedText = Color(0xFF8A958E)
private val BusinessSetupError = Color(0xFFD9534F)

private val BusinessTypeOptions = listOf(
    "Retail",
    "Grocery",
    "Clothing",
    "Restaurant",
    "Services",
    "Other"
)

private val CurrencyOptions = listOf("NIS ₪", "USD $", "JOD JD")

@Composable
fun BusinessSetupScreen(
    onBackClick: () -> Unit,
    onBusinessSetupSuccess: () -> Unit,
    modifier: Modifier = Modifier,
    onLogoClick: () -> Unit = {},
    viewModel: BusinessSetupViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(uiState.isBusinessSetupSuccessful) {
        if (uiState.isBusinessSetupSuccessful) {
            onBusinessSetupSuccess()
            viewModel.consumeBusinessSetupSuccess()
        }
    }

    BusinessSetupScreen(
        uiState = uiState,
        onBusinessNameChange = viewModel::onBusinessNameChange,
        onBusinessTypeSelected = viewModel::onBusinessTypeSelected,
        onCurrencySelected = viewModel::onCurrencySelected,
        onLogoClick = onLogoClick,
        onBackClick = onBackClick,
        onSaveAndContinueClick = viewModel::saveAndContinue,
        modifier = modifier
    )
}

@Composable
fun BusinessSetupScreen(
    uiState: BusinessSetupUiState,
    onBusinessNameChange: (String) -> Unit,
    onBusinessTypeSelected: (String) -> Unit,
    onCurrencySelected: (String) -> Unit,
    onLogoClick: () -> Unit,
    onBackClick: () -> Unit,
    onSaveAndContinueClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    BoxWithConstraints(
        modifier = modifier
            .fillMaxSize()
            .background(BusinessSetupBackground)
            .safeDrawingPadding()
            .imePadding()
    ) {
        val compactHeight = maxHeight < 720.dp

        Column(modifier = Modifier.fillMaxSize()) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
                    .padding(horizontal = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onBackClick) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = BusinessSetupPrimary
                    )
                }
            }

            Column(
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(if (compactHeight) 10.dp else 18.dp))

                Text(
                    text = "Set Up Your Business",
                    modifier = Modifier.fillMaxWidth(),
                    color = BusinessSetupPrimary,
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    lineHeight = 35.sp,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Tell us about your business to get started.",
                    modifier = Modifier.fillMaxWidth(),
                    color = BusinessSetupSecondaryText,
                    fontSize = 15.sp,
                    lineHeight = 22.sp,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(if (compactHeight) 18.dp else 24.dp))

                BusinessNameField(
                    value = uiState.businessName,
                    onValueChange = onBusinessNameChange,
                    error = uiState.businessNameError,
                    enabled = !uiState.isLoading
                )

                Spacer(modifier = Modifier.height(12.dp))

                BusinessSetupDropdown(
                    value = uiState.selectedBusinessType,
                    onValueSelected = onBusinessTypeSelected,
                    label = "Business Type",
                    placeholder = "Select Business type",
                    options = BusinessTypeOptions,
                    error = uiState.businessTypeError,
                    enabled = !uiState.isLoading
                )

                Spacer(modifier = Modifier.height(12.dp))

                BusinessSetupDropdown(
                    value = uiState.selectedCurrency,
                    onValueSelected = onCurrencySelected,
                    label = "Currency",
                    placeholder = "Select Currency",
                    options = CurrencyOptions,
                    error = uiState.currencyError,
                    enabled = !uiState.isLoading
                )

                Spacer(modifier = Modifier.height(if (compactHeight) 16.dp else 20.dp))

                LogoUploadArea(
                    selectedLogoUri = uiState.selectedLogoUri,
                    onClick = onLogoClick,
                    enabled = !uiState.isLoading
                )

                Spacer(modifier = Modifier.height(if (compactHeight) 20.dp else 28.dp))

                Button(
                    onClick = onSaveAndContinueClick,
                    enabled = !uiState.isLoading,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = BusinessSetupPrimary,
                        contentColor = Color.White,
                        disabledContainerColor = BusinessSetupPrimary.copy(alpha = 0.65f),
                        disabledContentColor = Color.White
                    ),
                    elevation = ButtonDefaults.buttonElevation(defaultElevation = 0.dp)
                ) {
                    if (uiState.isLoading) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(22.dp),
                            color = Color.White,
                            strokeWidth = 2.dp
                        )
                    } else {
                        Text(
                            text = "Save & Continue",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }

                uiState.errorMessage?.let { error ->
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = error,
                        modifier = Modifier.fillMaxWidth(),
                        color = BusinessSetupError,
                        fontSize = 13.sp,
                        lineHeight = 18.sp,
                        textAlign = TextAlign.Center
                    )
                }

                Spacer(modifier = Modifier.height(if (compactHeight) 16.dp else 28.dp))
            }
        }
    }
}

@Composable
private fun BusinessNameField(
    value: String,
    onValueChange: (String) -> Unit,
    error: String?,
    enabled: Boolean
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = Modifier.fillMaxWidth(),
        enabled = enabled,
        label = { Text(text = "Business Name") },
        placeholder = { Text(text = "Enter Business name") },
        supportingText = fieldSupportingText(error),
        isError = error != null,
        singleLine = true,
        shape = RoundedCornerShape(14.dp),
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Text,
            imeAction = ImeAction.Done
        ),
        keyboardActions = KeyboardActions.Default,
        colors = businessSetupFieldColors()
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun BusinessSetupDropdown(
    value: String,
    onValueSelected: (String) -> Unit,
    label: String,
    placeholder: String,
    options: List<String>,
    error: String?,
    enabled: Boolean
) {
    var expanded by rememberSaveable { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = {
            if (enabled) expanded = !expanded
        }
    ) {
        OutlinedTextField(
            value = value,
            onValueChange = {},
            modifier = Modifier
                .menuAnchor(
                    type = MenuAnchorType.PrimaryNotEditable,
                    enabled = enabled
                )
                .fillMaxWidth(),
            enabled = enabled,
            readOnly = true,
            label = { Text(text = label) },
            placeholder = { Text(text = placeholder) },
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
            },
            supportingText = fieldSupportingText(error),
            isError = error != null,
            singleLine = true,
            shape = RoundedCornerShape(14.dp),
            colors = businessSetupFieldColors()
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.background(BusinessSetupSurface)
        ) {
            options.forEach { option ->
                DropdownMenuItem(
                    text = {
                        Text(
                            text = option,
                            color = BusinessSetupSecondaryText,
                            fontSize = 14.sp
                        )
                    },
                    onClick = {
                        onValueSelected(option)
                        expanded = false
                    }
                )
            }
        }
    }
}

@Composable
private fun LogoUploadArea(
    selectedLogoUri: String?,
    onClick: () -> Unit,
    enabled: Boolean
) {
    val shape = RoundedCornerShape(16.dp)
    val logoBitmap = rememberLogoBitmap(selectedLogoUri)
    val borderColor = if (enabled) BusinessSetupSecondary else BusinessSetupBorder

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(132.dp)
            .clip(shape)
            .background(BusinessSetupSoftSurface)
            .drawBehind {
                drawRoundRect(
                    color = borderColor,
                    cornerRadius = androidx.compose.ui.geometry.CornerRadius(
                        x = 16.dp.toPx(),
                        y = 16.dp.toPx()
                    ),
                    style = Stroke(
                        width = 1.5.dp.toPx(),
                        pathEffect = PathEffect.dashPathEffect(
                            intervals = floatArrayOf(9.dp.toPx(), 7.dp.toPx())
                        )
                    )
                )
            }
            .clickable(enabled = enabled, onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        if (logoBitmap != null) {
            Image(
                bitmap = logoBitmap,
                contentDescription = "Selected business logo",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        } else {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Icon(
                    imageVector = CameraIcon,
                    contentDescription = null,
                    modifier = Modifier.size(32.dp),
                    tint = BusinessSetupPrimary
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = if (selectedLogoUri == null) {
                        "Add Logo (Optional)"
                    } else {
                        "Logo selected"
                    },
                    color = BusinessSetupPrimary,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}

@Composable
private fun rememberLogoBitmap(uriString: String?): ImageBitmap? {
    val context = LocalContext.current
    var imageBitmap by remember(uriString) { mutableStateOf<ImageBitmap?>(null) }

    LaunchedEffect(uriString) {
        imageBitmap = if (uriString.isNullOrBlank()) {
            null
        } else {
            withContext(Dispatchers.IO) {
                runCatching {
                    context.contentResolver.openInputStream(Uri.parse(uriString))
                        ?.use { inputStream ->
                            BitmapFactory.decodeStream(inputStream)?.asImageBitmap()
                        }
                }.getOrNull()
            }
        }
    }

    return imageBitmap
}

private fun fieldSupportingText(error: String?): (@Composable () -> Unit)? =
    error?.let {
        {
            Text(
                text = it,
                fontSize = 12.sp,
                lineHeight = 16.sp
            )
        }
    }

@Composable
private fun businessSetupFieldColors() = OutlinedTextFieldDefaults.colors(
    focusedTextColor = BusinessSetupPrimary,
    unfocusedTextColor = BusinessSetupPrimary,
    disabledTextColor = BusinessSetupPrimary,
    focusedBorderColor = BusinessSetupPrimary,
    unfocusedBorderColor = BusinessSetupBorder,
    disabledBorderColor = BusinessSetupBorder,
    errorBorderColor = BusinessSetupError,
    focusedLabelColor = BusinessSetupPrimary,
    unfocusedLabelColor = BusinessSetupSecondaryText,
    disabledLabelColor = BusinessSetupMutedText,
    errorLabelColor = BusinessSetupError,
    cursorColor = BusinessSetupPrimary,
    errorCursorColor = BusinessSetupError,
    focusedContainerColor = BusinessSetupSurface,
    unfocusedContainerColor = BusinessSetupSurface,
    disabledContainerColor = BusinessSetupSurface,
    errorContainerColor = BusinessSetupSurface,
    errorSupportingTextColor = BusinessSetupError,
    focusedPlaceholderColor = BusinessSetupMutedText,
    unfocusedPlaceholderColor = BusinessSetupMutedText
)

private val CameraIcon: ImageVector by lazy {
    ImageVector.Builder(
        name = "Camera",
        defaultWidth = 24.dp,
        defaultHeight = 24.dp,
        viewportWidth = 24f,
        viewportHeight = 24f
    ).apply {
        addPath(
            pathData = PathParser().parsePathString(
                "M9,2 L7.17,4 L4,4 C2.9,4 2,4.9 2,6 L2,18 " +
                    "C2,19.1 2.9,20 4,20 L20,20 C21.1,20 22,19.1 22,18 " +
                    "L22,6 C22,4.9 21.1,4 20,4 L16.83,4 L15,2 Z " +
                    "M12,17 C9.24,17 7,14.76 7,12 C7,9.24 9.24,7 12,7 " +
                    "C14.76,7 17,9.24 17,12 C17,14.76 14.76,17 12,17 Z " +
                    "M12,9 C10.34,9 9,10.34 9,12 C9,13.66 10.34,15 12,15 " +
                    "C13.66,15 15,13.66 15,12 C15,10.34 13.66,9 12,9 Z"
            ).toNodes(),
            fill = SolidColor(Color.Black)
        )
    }.build()
}

@Preview(
    name = "Business Setup - Default",
    showBackground = true,
    widthDp = 393,
    heightDp = 852
)
@Composable
private fun BusinessSetupDefaultPreview() {
    BGrothTheme {
        BusinessSetupPreview(uiState = BusinessSetupUiState())
    }
}

@Preview(
    name = "Business Setup - Filled",
    showBackground = true,
    widthDp = 393,
    heightDp = 852
)
@Composable
private fun BusinessSetupFilledPreview() {
    BGrothTheme {
        BusinessSetupPreview(
            uiState = BusinessSetupUiState(
                businessName = "Green Market",
                selectedBusinessType = "Retail",
                selectedCurrency = "NIS ₪",
                selectedLogoUri = "content://business-logo-preview"
            )
        )
    }
}

@Preview(
    name = "Business Setup - Error",
    showBackground = true,
    widthDp = 393,
    heightDp = 852
)
@Composable
private fun BusinessSetupErrorPreview() {
    BGrothTheme {
        BusinessSetupPreview(
            uiState = BusinessSetupUiState(
                selectedCurrency = "",
                businessNameError = "Business name is required.",
                businessTypeError = "Business type is required.",
                currencyError = "Currency is required."
            )
        )
    }
}

@Preview(
    name = "Business Setup - Loading",
    showBackground = true,
    widthDp = 393,
    heightDp = 852
)
@Composable
private fun BusinessSetupLoadingPreview() {
    BGrothTheme {
        BusinessSetupPreview(
            uiState = BusinessSetupUiState(
                businessName = "Green Market",
                selectedBusinessType = "Grocery",
                selectedCurrency = "USD $",
                isLoading = true
            )
        )
    }
}

@Composable
private fun BusinessSetupPreview(uiState: BusinessSetupUiState) {
    BusinessSetupScreen(
        uiState = uiState,
        onBusinessNameChange = {},
        onBusinessTypeSelected = {},
        onCurrencySelected = {},
        onLogoClick = {},
        onBackClick = {},
        onSaveAndContinueClick = {}
    )
}
