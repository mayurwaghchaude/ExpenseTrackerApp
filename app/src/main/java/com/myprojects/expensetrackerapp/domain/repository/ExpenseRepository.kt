package com.myprojects.expensetrackerapp.domain.repository

import com.myprojects.expensetrackerapp.data.entity.CategoryTotal
import com.myprojects.expensetrackerapp.data.entity.ExpenseEntity
import com.myprojects.expensetrackerapp.domain.model.Expense
import kotlinx.coroutines.flow.Flow

interface ExpenseRepository {

    fun getAllExpenses(): Flow<List<Expense>>

    suspend fun addExpense(expense: ExpenseEntity)

    suspend fun updateExpense(expense: ExpenseEntity)

    suspend fun deleteExpense(expense: ExpenseEntity)

    fun getTotalIncome(): Flow<Double>

    fun getTotalExpense(): Flow<Double>

    fun getExpensesByDateRange(startDate: Long, endDate: Long): Flow<List<Expense>>

    fun getExpenseById(id: String): Expense?

    fun getCategoryTotal(): Flow<List<CategoryTotal>>
}