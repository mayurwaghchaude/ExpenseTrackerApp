package com.myprojects.expensetrackerapp.presentation.screens.addExpense

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.myprojects.expensetrackerapp.domain.enums.ExpenseCategory
import com.myprojects.expensetrackerapp.domain.enums.ExpenseType
import com.myprojects.expensetrackerapp.domain.model.Expense
import com.myprojects.expensetrackerapp.domain.usecases.ExpenseUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class AddExpenseUiState(
    val title: String = "",
    val amount: String = "",
    val selectedCategory: ExpenseCategory = ExpenseCategory.FOOD,
    val selectedType: ExpenseType = ExpenseType.EXPENSE,
    val date: Long = System.currentTimeMillis(),
    val note: String = "",
    val isLoading: Boolean = false,
    val isSaved: Boolean = false,
    val error: String? = null
)

@HiltViewModel
class AddExpenseViewModel @Inject constructor(
    private val expenseUseCases: ExpenseUseCases
) : ViewModel() {

    private val _uiState = MutableStateFlow(AddExpenseUiState())
    val uiState: StateFlow<AddExpenseUiState> = _uiState.asStateFlow()

    fun loadExpense(expenseId: String) {
        viewModelScope.launch {
            val expense = expenseUseCases.getExpenses()
                .first()
                .find { it.id == expenseId } ?: return@launch

            _uiState.update {
                it.copy(
                    title = expense.title,
                    amount = expense.amount.toString(),
                    selectedCategory = ExpenseCategory.valueOf(expense.category),
                    selectedType = expense.type,
                    date = expense.date,
                    note = expense.note
                )
            }
        }
    }

    fun onDescriptionChange(value: String) =
        _uiState.update { it.copy(title = value) }

    fun onAmountChange(value: String) =
        _uiState.update { it.copy(amount = value) }

    fun onCategoryChange(value: ExpenseCategory) =
        _uiState.update { it.copy(selectedCategory = value) }

    fun onTypeChange(value: ExpenseType) =
        _uiState.update { it.copy(selectedType = value) }

    fun onDateChange(value: Long) =
        _uiState.update { it.copy(date = value) }

    fun onNoteChange(value: String) =
        _uiState.update { it.copy(note = value) }

    fun saveExpense() {
        val state = _uiState.value
        val amount = state.amount.toDoubleOrNull()

        if (state.title.isBlank()) {
            _uiState.update { it.copy(error = "Description cannot be empty") }
            return
        }
        if (amount == null || amount <= 0) {
            _uiState.update { it.copy(error = "Enter a valid amount") }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            try {
                expenseUseCases.addExpense(
                    Expense(
                        title = state.title,
                        amount = amount,
                        category = state.selectedCategory.name,
                        type = state.selectedType,
                        date = state.date,
                        note = state.note
                    )
                )
                _uiState.update { it.copy(isSaved = true, isLoading = false) }
            } catch (e: Exception) {
                _uiState.update { it.copy(error = e.message, isLoading = false) }
            }
        }
    }

    fun clearError() = _uiState.update { it.copy(error = null) }
}