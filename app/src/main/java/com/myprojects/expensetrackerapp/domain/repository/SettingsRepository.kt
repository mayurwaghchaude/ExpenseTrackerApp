package com.myprojects.expensetrackerapp.domain.repository

import com.myprojects.expensetrackerapp.domain.model.Settings
import kotlinx.coroutines.flow.Flow

interface SettingsRepository {
    fun getSettings(): Flow<Settings>
    suspend fun saveSettings(settings: Settings)
    fun isDarkTheme(): Flow<Boolean>
}