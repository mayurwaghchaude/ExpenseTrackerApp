package com.myprojects.expensetrackerapp.domain.usecases

import com.myprojects.expensetrackerapp.domain.model.Expense
import com.myprojects.expensetrackerapp.domain.repository.ExpenseRepository

class UpdateExpenseUseCase constructor(
    private val repository: ExpenseRepository
) {
    suspend operator fun invoke(expense: Expense) = repository.updateExpense(expense.toEntity())
}
