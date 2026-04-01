package com.myprojects.expensetrackerapp.data.repository

import com.myprojects.expensetrackerapp.data.dao.ExpenseDao
import com.myprojects.expensetrackerapp.data.entity.ExpenseEntity
import com.myprojects.expensetrackerapp.domain.repository.ExpenseRepository
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ExpenseRepositoryImpl constructor(
    private val dao: ExpenseDao
) : ExpenseRepository {
    override fun getAllExpenses() = dao.getAllExpenses().map { list -> list.map { it.toDomain() } }

    override suspend fun addExpense(expense: ExpenseEntity) = dao.insertExpanse(expense)

    override suspend fun updateExpense(expense: ExpenseEntity) = dao.updateExpense(expense)

    override suspend fun deleteExpense(expense: ExpenseEntity) = dao.deleteExpense(expense)

    override fun getExpensesByDateRange(startDate: Long, endDate: Long) =
        dao.getExpenseByDateRange(startDate, endDate).map { list -> list.map { it.toDomain() } }

    override fun getTotalIncome() = dao.getTotalIncome()

    override fun getTotalExpense() = dao.getTotalExpense()

    override fun getExpenseById(id: String) = dao.getExpensesById(id)?.toDomain()

    override fun getCategoryTotal() = dao.getCategoryWiseExpenses()
}