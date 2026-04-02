package com.myprojects.expensetrackerapp.data.repository

import com.myprojects.expensetrackerapp.data.dao.SettingsDao
import com.myprojects.expensetrackerapp.domain.model.Settings
import com.myprojects.expensetrackerapp.domain.repository.SettingsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class SettingsRepositoryImpl constructor(
    private val dao: SettingsDao
) : SettingsRepository {

    override fun getSettings(): Flow<Settings> =
        dao.getSettings().map { it?.toDomain() ?: Settings() }

    override suspend fun saveSettings(settings: Settings) =
        dao.saveSettings(settings.toEntity())

    override fun isDarkTheme(): Flow<Boolean> =
        dao.getSettings().map { it?.isDarkMode ?: false }
}