package com.myprojects.expensetrackerapp.domain.usecases

import com.myprojects.expensetrackerapp.domain.model.Expense
import com.myprojects.expensetrackerapp.domain.repository.ExpenseRepository

class DeleteExpenseUseCase constructor(
    private val repository: ExpenseRepository
) {
    suspend operator fun invoke(expense: Expense) = repository.deleteExpense(expense.toEntity())
}