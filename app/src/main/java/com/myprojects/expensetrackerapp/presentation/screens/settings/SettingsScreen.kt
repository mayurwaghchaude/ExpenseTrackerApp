package com.myprojects.expensetrackerapp.presentation.screens.settings

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.myprojects.expensetrackerapp.R
import com.myprojects.expensetrackerapp.presentation.components.BottomNavBar

@Composable
fun SettingsScreen(
    navController: NavController,
    viewModel: SettingsViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    var showBudgetDialog by remember { mutableStateOf(false) }
    var showClearDataDialog by remember { mutableStateOf(false) }

    if (showBudgetDialog) {
        BudgetInputDialog(
            currentBudget = uiState.settings.monthlyBudget,
            onConfirm = { budget ->
                viewModel.onMonthlyBudgetChange(budget)
                showBudgetDialog = false
            },
            onDismiss = { showBudgetDialog = false }
        )
    }

    if (showClearDataDialog) {
        AlertDialog(
            onDismissRequest = { showClearDataDialog = false },
            title = { Text(stringResource(R.string.clear_data_title)) },
            text = { Text(stringResource(R.string.clear_data_confirmation)) },
            confirmButton = {
                TextButton(onClick = { showClearDataDialog = false }) {
                    Text(stringResource(R.string.clear), color = MaterialTheme.colorScheme.error)
                }
            },
            dismissButton = {
                TextButton(onClick = { showClearDataDialog = false }) {
                    Text(stringResource(R.string.cancel))
                }
            }
        )
    }

    Scaffold(
        bottomBar = { BottomNavBar(navController = navController) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            Text(
                text = stringResource(R.string.settings_title),
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold
            )

            // Preferences section
            SettingsSection(title = stringResource(R.string.preferences_section)) {
                SettingsToggleRow(
                    icon = Icons.Rounded.DarkMode,
                    label = stringResource(R.string.dark_mode),
                    checked = uiState.settings.isDarkMode,
                    onCheckedChange = viewModel::onDarkModeChange
                )
                HorizontalDivider(thickness = 0.5.dp)
                SettingsNavigationRow(
                    icon = Icons.Rounded.AttachMoney,
                    label = stringResource(R.string.currency),
                    value = "${uiState.settings.currencySymbol} ${uiState.settings.currency}",
                    onClick = {}
                )
                HorizontalDivider(thickness = 0.5.dp)
                SettingsNavigationRow(
                    icon = Icons.Rounded.CalendarMonth,
                    label = stringResource(R.string.start_of_month),
                    value = "${uiState.settings.monthStartDay}${ordinalSuffix(uiState.settings.monthStartDay)}",
                    onClick = {}
                )
            }

            // Budget section
            SettingsSection(title = stringResource(R.string.budget_section)) {
                SettingsNavigationRow(
                    icon = Icons.Rounded.Wallet,
                    label = stringResource(R.string.monthly_budget),
                    value = "₹${uiState.settings.monthlyBudget.toLong()}",
                    onClick = { showBudgetDialog = true }
                )
                HorizontalDivider(thickness = 0.5.dp)
                SettingsToggleRow(
                    icon = Icons.Rounded.Notifications,
                    label = stringResource(R.string.budget_alerts),
                    checked = uiState.settings.budgetAlertsEnabled,
                    onCheckedChange = viewModel::onBudgetAlertsChange
                )
            }

            // Data section
            SettingsSection(title = stringResource(R.string.data_section)) {
                SettingsNavigationRow(
                    icon = Icons.Rounded.Download,
                    label = stringResource(R.string.export_data),
                    value = "",
                    onClick = {}
                )
                HorizontalDivider(thickness = 0.5.dp)
                SettingsNavigationRow(
                    icon = Icons.Rounded.Delete,
                    label = stringResource(R.string.clear_all_data),
                    value = "",
                    onClick = { showClearDataDialog = true },
                    labelColor = MaterialTheme.colorScheme.error,
                    iconTint = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}

@Composable
fun SettingsSection(
    title: String,
    content: @Composable ColumnScope.() -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(0.dp)) {
        Text(
            text = title.uppercase(),
            fontSize = 11.sp,
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f),
            letterSpacing = 0.5.sp,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        Surface(
            shape = MaterialTheme.shapes.large,
            color = MaterialTheme.colorScheme.surfaceVariant,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column { content() }
        }
    }
}

@Composable
fun SettingsToggleRow(
    icon: ImageVector,
    label: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            modifier = Modifier.size(18.dp),
            tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
        )
        Spacer(modifier = Modifier.width(12.dp))
        Text(
            text = label,
            fontSize = 14.sp,
            modifier = Modifier.weight(1f)
        )
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(
                checkedThumbColor = Color.White,
                checkedTrackColor = MaterialTheme.colorScheme.primary
            )
        )
    }
}

@Composable
fun SettingsNavigationRow(
    icon: ImageVector,
    label: String,
    value: String,
    onClick: () -> Unit,
    labelColor: Color = MaterialTheme.colorScheme.onSurface,
    iconTint: Color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
) {
    Surface(
        onClick = onClick,
        color = Color.Transparent
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier.size(18.dp),
                tint = iconTint
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = label,
                fontSize = 14.sp,
                color = labelColor,
                modifier = Modifier.weight(1f)
            )
            if (value.isNotBlank()) {
                Text(
                    text = value,
                    fontSize = 13.sp,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                )
                Spacer(modifier = Modifier.width(4.dp))
            }
            Icon(
                imageVector = Icons.Rounded.ChevronRight,
                contentDescription = null,
                modifier = Modifier.size(16.dp),
                tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f)
            )
        }
    }
}

@Composable
fun BudgetInputDialog(
    currentBudget: Double,
    onConfirm: (Double) -> Unit,
    onDismiss: () -> Unit
) {
    var input by remember {
        mutableStateOf(
            if (currentBudget > 0) currentBudget.toLong().toString() else ""
        )
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(stringResource(R.string.monthly_budget)) },
        text = {
            OutlinedTextField(
                value = input,
                onValueChange = { input = it },
                label = { Text(stringResource(R.string.amount_label)) },
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFF1D9E75)
                )
            )
        },
        confirmButton = {
            TextButton(onClick = {
                input.toDoubleOrNull()?.let { onConfirm(it) }
            }) {
                Text(stringResource(R.string.save), color = Color(0xFF1D9E75))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text(stringResource(R.string.cancel)) }
        }
    )
}

private fun ordinalSuffix(day: Int): String = when {
    day in 11..13 -> "th"
    day % 10 == 1 -> "st"
    day % 10 == 2 -> "nd"
    day % 10 == 3 -> "rd"
    else -> "th"
}
