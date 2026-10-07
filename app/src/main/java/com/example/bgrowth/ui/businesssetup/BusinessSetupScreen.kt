package com.example.bgrowth.ui.businesssetup

import androidx.compose.foundation.background
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
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.bgrowth.ui.theme.BGrothTheme

private val BusinessSetupPrimary = Color(0xFF0F5D46)
private val BusinessSetupBackground = Color(0xFFF7F8F4)
private val BusinessSetupSurface = Color(0xFFFFFFFF)
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

private val CurrencyOptions = listOf(
    "NIS ₪",
    "USD $",
    "JOD JD"
)

@Composable
fun BusinessSetupScreen(
    onBackClick: () -> Unit,
    onBusinessSetupSuccess: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: BusinessSetupViewModel = viewModel()
) {

    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(
        uiState.isBusinessSetupSuccessful
    ) {

        if (uiState.isBusinessSetupSuccessful) {

            onBusinessSetupSuccess()

            viewModel.consumeBusinessSetupSuccess()
        }
    }

    BusinessSetupScreen(
        uiState = uiState,
        onBusinessNameChange =
            viewModel::onBusinessNameChange,
        onBusinessTypeSelected =
            viewModel::onBusinessTypeSelected,
        onCurrencySelected =
            viewModel::onCurrencySelected,
        onBackClick = onBackClick,
        onSaveAndContinueClick =
            viewModel::saveAndContinue,
        modifier = modifier
    )
}

@Composable
fun BusinessSetupScreen(
    uiState: BusinessSetupUiState,
    onBusinessNameChange: (String) -> Unit,
    onBusinessTypeSelected: (String) -> Unit,
    onCurrencySelected: (String) -> Unit,
    onBackClick: () -> Unit,
    onSaveAndContinueClick: () -> Unit,
    modifier: Modifier = Modifier
) {

    BoxWithConstraints(
        modifier = modifier
            .fillMaxSize()
            .background(
                BusinessSetupBackground
            )
            .safeDrawingPadding()
            .imePadding()
    ) {

        val compactHeight =
            maxHeight < 720.dp

        Column(
            modifier = Modifier.fillMaxSize()
        ) {

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
                    .padding(horizontal = 8.dp),
                verticalAlignment =
                    Alignment.CenterVertically
            ) {

                IconButton(
                    onClick = onBackClick,
                    enabled = !uiState.isLoading
                ) {

                    Icon(
                        imageVector =
                            Icons.AutoMirrored
                                .Filled
                                .ArrowBack,
                        contentDescription = "Back",
                        tint = BusinessSetupPrimary
                    )
                }
            }

            Column(
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(
                        rememberScrollState()
                    )
                    .padding(
                        horizontal = 24.dp
                    ),
                horizontalAlignment =
                    Alignment.CenterHorizontally
            ) {

                Spacer(
                    modifier = Modifier.height(
                        if (compactHeight) {
                            10.dp
                        } else {
                            18.dp
                        }
                    )
                )

                Text(
                    text = "Set Up Your Business",
                    modifier =
                        Modifier.fillMaxWidth(),
                    color =
                        BusinessSetupPrimary,
                    fontSize = 28.sp,
                    fontWeight =
                        FontWeight.Bold,
                    lineHeight = 35.sp,
                    textAlign =
                        TextAlign.Center
                )

                Spacer(
                    modifier =
                        Modifier.height(8.dp)
                )

                Text(
                    text =
                        "Tell us about your business to get started.",
                    modifier =
                        Modifier.fillMaxWidth(),
                    color =
                        BusinessSetupSecondaryText,
                    fontSize = 15.sp,
                    lineHeight = 22.sp,
                    textAlign =
                        TextAlign.Center
                )

                Spacer(
                    modifier = Modifier.height(
                        if (compactHeight) {
                            18.dp
                        } else {
                            24.dp
                        }
                    )
                )

                BusinessNameField(
                    value =
                        uiState.businessName,
                    onValueChange =
                        onBusinessNameChange,
                    error =
                        uiState.businessNameError,
                    enabled =
                        !uiState.isLoading
                )

                Spacer(
                    modifier =
                        Modifier.height(12.dp)
                )

                BusinessSetupDropdown(
                    value =
                        uiState.selectedBusinessType,
                    onValueSelected =
                        onBusinessTypeSelected,
                    label = "Business Type",
                    placeholder =
                        "Select Business type",
                    options =
                        BusinessTypeOptions,
                    error =
                        uiState.businessTypeError,
                    enabled =
                        !uiState.isLoading
                )

                Spacer(
                    modifier =
                        Modifier.height(12.dp)
                )

                BusinessSetupDropdown(
                    value =
                        uiState.selectedCurrency,
                    onValueSelected =
                        onCurrencySelected,
                    label = "Currency",
                    placeholder =
                        "Select Currency",
                    options =
                        CurrencyOptions,
                    error =
                        uiState.currencyError,
                    enabled =
                        !uiState.isLoading
                )

                Spacer(
                    modifier = Modifier.height(
                        if (compactHeight) {
                            24.dp
                        } else {
                            32.dp
                        }
                    )
                )

                Button(
                    onClick =
                        onSaveAndContinueClick,
                    enabled =
                        !uiState.isLoading,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    shape =
                        RoundedCornerShape(
                            16.dp
                        ),
                    colors =
                        ButtonDefaults
                            .buttonColors(
                                containerColor =
                                    BusinessSetupPrimary,
                                contentColor =
                                    Color.White,
                                disabledContainerColor =
                                    BusinessSetupPrimary
                                        .copy(
                                            alpha = 0.65f
                                        ),
                                disabledContentColor =
                                    Color.White
                            ),
                    elevation =
                        ButtonDefaults
                            .buttonElevation(
                                defaultElevation =
                                    0.dp
                            )
                ) {

                    if (uiState.isLoading) {

                        CircularProgressIndicator(
                            modifier =
                                Modifier.size(
                                    22.dp
                                ),
                            color =
                                Color.White,
                            strokeWidth =
                                2.dp
                        )

                    } else {

                        Text(
                            text =
                                "Save & Continue",
                            fontSize =
                                16.sp,
                            fontWeight =
                                FontWeight.SemiBold
                        )
                    }
                }

                uiState.errorMessage
                    ?.let { error ->

                        Spacer(
                            modifier =
                                Modifier.height(
                                    10.dp
                                )
                        )

                        Text(
                            text = error,
                            modifier =
                                Modifier
                                    .fillMaxWidth(),
                            color =
                                BusinessSetupError,
                            fontSize =
                                13.sp,
                            lineHeight =
                                18.sp,
                            textAlign =
                                TextAlign.Center
                        )
                    }

                Spacer(
                    modifier = Modifier.height(
                        if (compactHeight) {
                            16.dp
                        } else {
                            28.dp
                        }
                    )
                )
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
        modifier =
            Modifier.fillMaxWidth(),
        enabled = enabled,
        label = {
            Text(
                text = "Business Name"
            )
        },
        placeholder = {
            Text(
                text = "Enter Business name"
            )
        },
        supportingText =
            fieldSupportingText(error),
        isError = error != null,
        singleLine = true,
        shape =
            RoundedCornerShape(14.dp),
        keyboardOptions =
            KeyboardOptions(
                keyboardType =
                    KeyboardType.Text,
                imeAction =
                    ImeAction.Done
            ),
        keyboardActions =
            KeyboardActions.Default,
        colors =
            businessSetupFieldColors()
    )
}

@OptIn(
    ExperimentalMaterial3Api::class
)
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

    var expanded by rememberSaveable {
        mutableStateOf(false)
    }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = {

            if (enabled) {
                expanded = !expanded
            }
        }
    ) {

        OutlinedTextField(
            value = value,
            onValueChange = {},
            modifier = Modifier
                .menuAnchor(
                    type =
                        MenuAnchorType
                            .PrimaryNotEditable,
                    enabled = enabled
                )
                .fillMaxWidth(),
            enabled = enabled,
            readOnly = true,
            label = {
                Text(
                    text = label
                )
            },
            placeholder = {
                Text(
                    text = placeholder
                )
            },
            trailingIcon = {

                ExposedDropdownMenuDefaults
                    .TrailingIcon(
                        expanded = expanded
                    )
            },
            supportingText =
                fieldSupportingText(error),
            isError = error != null,
            singleLine = true,
            shape =
                RoundedCornerShape(
                    14.dp
                ),
            colors =
                businessSetupFieldColors()
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = {
                expanded = false
            },
            modifier =
                Modifier.background(
                    BusinessSetupSurface
                )
        ) {

            options.forEach { option ->

                DropdownMenuItem(
                    text = {

                        Text(
                            text = option,
                            color =
                                BusinessSetupSecondaryText,
                            fontSize =
                                14.sp
                        )
                    },
                    onClick = {

                        onValueSelected(
                            option
                        )

                        expanded = false
                    }
                )
            }
        }
    }
}

private fun fieldSupportingText(
    error: String?
): (@Composable () -> Unit)? {

    return error?.let {

        {
            Text(
                text = it,
                fontSize = 12.sp,
                lineHeight = 16.sp
            )
        }
    }
}

@Composable
private fun businessSetupFieldColors() =
    OutlinedTextFieldDefaults.colors(

        focusedTextColor =
            BusinessSetupPrimary,

        unfocusedTextColor =
            BusinessSetupPrimary,

        disabledTextColor =
            BusinessSetupPrimary,

        focusedBorderColor =
            BusinessSetupPrimary,

        unfocusedBorderColor =
            BusinessSetupBorder,

        disabledBorderColor =
            BusinessSetupBorder,

        errorBorderColor =
            BusinessSetupError,

        focusedLabelColor =
            BusinessSetupPrimary,

        unfocusedLabelColor =
            BusinessSetupSecondaryText,

        disabledLabelColor =
            BusinessSetupMutedText,

        errorLabelColor =
            BusinessSetupError,

        cursorColor =
            BusinessSetupPrimary,

        errorCursorColor =
            BusinessSetupError,

        focusedContainerColor =
            BusinessSetupSurface,

        unfocusedContainerColor =
            BusinessSetupSurface,

        disabledContainerColor =
            BusinessSetupSurface,

        errorContainerColor =
            BusinessSetupSurface,

        errorSupportingTextColor =
            BusinessSetupError,

        focusedPlaceholderColor =
            BusinessSetupMutedText,

        unfocusedPlaceholderColor =
            BusinessSetupMutedText
    )

@Preview(
    name = "Business Setup - Default",
    showBackground = true,
    widthDp = 393,
    heightDp = 852
)
@Composable
private fun BusinessSetupDefaultPreview() {

    BGrothTheme {

        BusinessSetupPreview(
            uiState =
                BusinessSetupUiState()
        )
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
            uiState =
                BusinessSetupUiState(
                    businessName =
                        "Green Market",
                    selectedBusinessType =
                        "Retail",
                    selectedCurrency =
                        "NIS ₪"
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
            uiState =
                BusinessSetupUiState(
                    selectedCurrency = "",
                    businessNameError =
                        "Business name is required.",
                    businessTypeError =
                        "Business type is required.",
                    currencyError =
                        "Currency is required."
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
            uiState =
                BusinessSetupUiState(
                    businessName =
                        "Green Market",
                    selectedBusinessType =
                        "Grocery",
                    selectedCurrency =
                        "USD $",
                    isLoading = true
                )
        )
    }
}

@Composable
private fun BusinessSetupPreview(
    uiState: BusinessSetupUiState
) {

    BusinessSetupScreen(
        uiState = uiState,
        onBusinessNameChange = {},
        onBusinessTypeSelected = {},
        onCurrencySelected = {},
        onBackClick = {},
        onSaveAndContinueClick = {}
    )
}