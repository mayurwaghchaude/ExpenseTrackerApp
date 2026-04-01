package com.myprojects.expensetrackerapp.presentation.screens.analytics

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.myprojects.expensetrackerapp.data.entity.CategoryTotal
import com.myprojects.expensetrackerapp.domain.usecases.ExpenseUseCases
import com.myprojects.expensetrackerapp.domain.usecases.GetSummaryUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class AnalyticsUiState(
    val totalExpense: Double = 0.0,
    val totalIncome: Double = 0.0,
    val categoryTotals: List<CategoryTotal> = emptyList(),
    val isLoading: Boolean = false
) {
    val balance: Double get() = totalIncome - totalExpense
}

@HiltViewModel
class AnalyticsViewModel @Inject constructor(
    private val expenseUseCases: ExpenseUseCases
) : ViewModel() {

    private val _uiState = MutableStateFlow(AnalyticsUiState())
    val uiState: StateFlow<AnalyticsUiState> = _uiState.asStateFlow()

    init {
        observeData()
    }

    private fun observeData() {
        viewModelScope.launch {
            expenseUseCases.getCategoryTotals().collect { totals ->
                _uiState.update { it.copy(categoryTotals = totals) }
            }
        }
        viewModelScope.launch {
            expenseUseCases.getSummary.totalIncome().collect { income ->
                _uiState.update { it.copy(totalIncome = income) }
            }
        }
        viewModelScope.launch {
            expenseUseCases.getSummary.totalExpense().collect { expense ->
                _uiState.update { it.copy(totalExpense = expense) }
            }
        }
    }
}