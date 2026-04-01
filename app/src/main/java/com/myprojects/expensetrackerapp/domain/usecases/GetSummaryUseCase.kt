package com.myprojects.expensetrackerapp.domain.usecases

import com.myprojects.expensetrackerapp.domain.repository.ExpenseRepository

class GetSummaryUseCase constructor(
    private val repository: ExpenseRepository,
) {
    fun totalIncome() = repository.getTotalIncome()
    fun totalExpense() = repository.getTotalExpense()
}