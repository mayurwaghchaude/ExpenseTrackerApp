package com.myprojects.expensetrackerapp.domain.usecases

import com.myprojects.expensetrackerapp.domain.repository.ExpenseRepository

class GetExpensesUseCase constructor(
    private val repository: ExpenseRepository
) {
    operator fun invoke() = repository.getAllExpenses()
}