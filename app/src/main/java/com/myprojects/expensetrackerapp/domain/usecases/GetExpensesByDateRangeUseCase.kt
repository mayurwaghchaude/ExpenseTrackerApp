package com.myprojects.expensetrackerapp.domain.usecases

import com.myprojects.expensetrackerapp.domain.repository.ExpenseRepository

class GetExpensesByDateRangeUseCase constructor(
    private val repository: ExpenseRepository
) {
    operator fun invoke(startDate: Long, endDate: Long) =
        repository.getExpensesByDateRange(startDate, endDate)
}