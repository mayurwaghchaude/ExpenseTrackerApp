package com.myprojects.expensetrackerapp.domain.usecases

import com.myprojects.expensetrackerapp.domain.repository.ExpenseRepository

class GetCategoryTotalsUseCase constructor(
    private val repository: ExpenseRepository
) {
    operator fun invoke() = repository.getCategoryTotal()
}
