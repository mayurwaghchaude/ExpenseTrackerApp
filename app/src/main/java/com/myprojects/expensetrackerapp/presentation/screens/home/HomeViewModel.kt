package com.myprojects.expensetrackerapp.presentation.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.myprojects.expensetrackerapp.domain.model.Expense
import com.myprojects.expensetrackerapp.domain.usecases.ExpenseUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class HomeUIState(
    val totalIncome: Double = 0.0,
    val totalExpense: Double = 0.0,
    val expenses: List<Expense> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
) {
    val balance: Double get() = totalIncome - totalExpense;
}


@HiltViewModel
class HomeViewModel @Inject constructor(
    private val expenseUseCases: ExpenseUseCases
) : ViewModel() {
    private val _uiState = MutableStateFlow(HomeUIState())
    val uiState = _uiState.asStateFlow()

    private fun observeExpenses() {
        viewModelScope.launch {
            expenseUseCases.getExpenses().collect { expenses ->
                _uiState.update { it.copy(expenses = expenses) }
            }
        }
    }

    private fun observeSummary() {
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

    fun deleteExpense(expense: Expense) {
        viewModelScope.launch {
            try {
                expenseUseCases.deleteExpense(expense)
            } catch (e: Exception) {
                _uiState.update { it.copy(error = e.message) }
            }
        }
    }

    fun clearError() {
        _uiState.update { it.copy(error = null) }
    }

}