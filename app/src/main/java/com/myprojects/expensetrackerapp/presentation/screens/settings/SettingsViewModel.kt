package com.myprojects.expensetrackerapp.presentation.screens.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.myprojects.expensetrackerapp.domain.model.Settings
import com.myprojects.expensetrackerapp.domain.usecases.SettingsUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class SettingsUiState(
    val settings: Settings = Settings(),
    val isLoading: Boolean = false,
    val isSaved: Boolean = false
)

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val settingsUseCases: SettingsUseCases
) : ViewModel() {

    private val _uiState = MutableStateFlow(SettingsUiState())
    val uiState: StateFlow<SettingsUiState> = _uiState.asStateFlow()

    init {
        loadSettings()
    }

    private fun loadSettings() {
        viewModelScope.launch {
            settingsUseCases.getSettings().collect { settings ->
                _uiState.update { it.copy(settings = settings) }
            }
        }
    }

    fun onMonthlyBudgetChange(value: Double) {
        _uiState.update { it.copy(settings = it.settings.copy(monthlyBudget = value)) }
        saveSettings()
    }

    fun onBudgetAlertsChange(enabled: Boolean) {
        _uiState.update { it.copy(settings = it.settings.copy(budgetAlertsEnabled = enabled)) }
        saveSettings()
    }

    fun onDarkModeChange(enabled: Boolean) {
        _uiState.update { it.copy(settings = it.settings.copy(isDarkMode = enabled)) }
        saveSettings()
    }

    fun onMonthStartDayChange(day: Int) {
        _uiState.update { it.copy(settings = it.settings.copy(monthStartDay = day)) }
        saveSettings()
    }

    fun onCurrencyChange(currency: String, symbol: String) {
        _uiState.update {
            it.copy(
                settings = it.settings.copy(
                    currency = currency,
                    currencySymbol = symbol
                )
            )
        }
        saveSettings()
    }

    private fun saveSettings() {
        viewModelScope.launch {
            settingsUseCases.saveSettings(_uiState.value.settings)
        }
    }
}