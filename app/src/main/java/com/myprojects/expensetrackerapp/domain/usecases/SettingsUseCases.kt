package com.myprojects.expensetrackerapp.domain.usecases

import com.myprojects.expensetrackerapp.domain.model.Settings
import com.myprojects.expensetrackerapp.domain.repository.SettingsRepository
import javax.inject.Inject

class GetSettingsUseCase constructor(
    private val repository: SettingsRepository
) {
    operator fun invoke() = repository.getSettings()
}

class SaveSettingsUseCase constructor(
    private val repository: SettingsRepository
) {
    suspend operator fun invoke(settings: Settings) = repository.saveSettings(settings)
}

class GetThemeUseCase constructor(
    private val repository: SettingsRepository
) {
    operator fun invoke() = repository.isDarkTheme()
}

data class SettingsUseCases(
    val getSettings: GetSettingsUseCase,
    val saveSettings: SaveSettingsUseCase,
    val getTheme: GetThemeUseCase
)