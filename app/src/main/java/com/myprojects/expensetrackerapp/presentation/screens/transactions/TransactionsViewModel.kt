package com.myprojects.expensetrackerapp.presentation.screens.transactions

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.myprojects.expensetrackerapp.domain.enums.ExpenseType
import com.myprojects.expensetrackerapp.domain.model.Expense
import com.myprojects.expensetrackerapp.domain.usecases.ExpenseUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

enum class TransactionFilter { ALL, INCOME, EXPENSE }

data class TransactionUiState(
    val allExpenses: List<Expense> = emptyList(),
    val filteredExpenses: List<Expense> = emptyList(),
    val searchQuery: String = "",
    val selectedFilter: TransactionFilter = TransactionFilter.ALL,
    val isLoading: Boolean = false
)

@HiltViewModel
class TransactionsViewModel @Inject constructor(
    private val useCases: ExpenseUseCases
) : ViewModel() {

    private val _uiState = MutableStateFlow(TransactionUiState())
    val uiState: StateFlow<TransactionUiState> = _uiState.asStateFlow()

    init {
        observeExpenses()
    }

    private fun observeExpenses() {
        viewModelScope.launch {
            useCases.getExpenses().collect { expenses ->
                _uiState.update {
                    it.copy(allExpenses = expenses)
                }
                applyFilters()
            }
        }
    }

    fun onSearchQueryChange(query: String) {
        _uiState.update { it.copy(searchQuery = query) }
        applyFilters()
    }

    fun onFilterChange(filter: TransactionFilter) {
        _uiState.update { it.copy(selectedFilter = filter) }
        applyFilters()
    }

    fun deleteExpense(expense: Expense) {
        viewModelScope.launch {
            useCases.deleteExpense(expense)
        }
    }

    private fun applyFilters() {
        val state = _uiState.value
        val filtered = state.allExpenses
            .filter { expense ->
                when (state.selectedFilter) {
                    TransactionFilter.ALL -> true
                    TransactionFilter.INCOME -> expense.type == ExpenseType.INCOME
                    TransactionFilter.EXPENSE -> expense.type == ExpenseType.EXPENSE
                }
            }
            .filter { expense ->
                if (state.searchQuery.isBlank()) true
                else expense.title.contains(state.searchQuery, ignoreCase = true)
                        || expense.category.contains(state.searchQuery, ignoreCase = true)
            }
        _uiState.update { it.copy(filteredExpenses = filtered) }
    }
}