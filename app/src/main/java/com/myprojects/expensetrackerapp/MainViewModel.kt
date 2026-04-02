package com.myprojects.expensetrackerapp

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.myprojects.expensetrackerapp.domain.usecases.SettingsUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    settingsUseCases: SettingsUseCases
) : ViewModel() {

    val isDarkTheme = settingsUseCases.getTheme()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = false
        )
}