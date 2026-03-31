package com.myprojects.expensetrackerapp.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.myprojects.expensetrackerapp.data.entity.CategoryTotal
import com.myprojects.expensetrackerapp.data.entity.ExpenseEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ExpenseDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertExpanse(expense: ExpenseEntity)

    @Update
    suspend fun updateExpense(expense: ExpenseEntity)

    @Delete
    suspend fun deleteExpense(expense: ExpenseEntity)

    @Query("SELECT * FROM expenses ORDER BY date DESC")
    fun getAllExpenses(): Flow<List<ExpenseEntity>>

    @Query("SELECT * FROM expenses WHERE id= :id")
    fun getExpensesById(id: String): ExpenseEntity?


    // Total income
    @Query("SELECT COALESCE(SUM(amount), 0.0) FROM expenses WHERE type = 'INCOME'")
    fun getTotalIncome(): Flow<Double>

    // Total expenses
    @Query("SELECT COALESCE(SUM(amount), 0.0) FROM expenses WHERE type = 'EXPENSE'")
    fun getTotalExpense(): Flow<Double>


    @Query(
        """SELECT * FROM expenses
            WHERE type = 'EXPENSE' AND 
            date BETWEEN :start AND :end
            ORDER BY date DESC"""
    )
    fun getExpenseByDateRange(start: Long, end: Long): Flow<List<ExpenseEntity>>

    @Query(
        """
        SELECT category, SUM(amount) as total
         FROM expenses  WHERE type = 'EXPENSE' 
         GROUP BY category
    """
    )
    fun getCategoryWiseExpenses(): Flow<List<CategoryTotal>>

}