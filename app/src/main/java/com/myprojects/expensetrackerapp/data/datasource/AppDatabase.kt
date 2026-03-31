package com.myprojects.expensetrackerapp.data.datasource

import androidx.room.Database
import androidx.room.RoomDatabase
import com.myprojects.expensetrackerapp.data.dao.ExpenseDao
import com.myprojects.expensetrackerapp.data.entity.ExpenseEntity

@Database(
    entities = [ExpenseEntity::class],
    version = 1,
    exportSchema = true
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun expenseDao(): ExpenseDao

    companion object {
        const val DATABASE_NAME = "expense_db"
    }
}