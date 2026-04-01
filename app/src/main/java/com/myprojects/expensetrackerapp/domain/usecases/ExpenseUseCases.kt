package com.myprojects.expensetrackerapp.domain.usecases

data class ExpenseUseCases(
    val getExpenses: GetExpensesUseCase,
    val addExpense: AddExpenseUseCase,
    val deleteExpense: DeleteExpenseUseCase,
    val updateExpense: UpdateExpenseUseCase,
    val getCategoryTotals: GetCategoryTotalsUseCase,
    val getSummary: GetSummaryUseCase,
    val getExpensesByDateRange: GetExpensesByDateRangeUseCase
)

