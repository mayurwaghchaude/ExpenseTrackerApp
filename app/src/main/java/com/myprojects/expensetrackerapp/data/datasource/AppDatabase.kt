package com.myprojects.expensetrackerapp.data.datasource

import androidx.room.Database
import androidx.room.RoomDatabase
import com.myprojects.expensetrackerapp.data.dao.ExpenseDao
import com.myprojects.expensetrackerapp.data.dao.SettingsDao
import com.myprojects.expensetrackerapp.data.entity.ExpenseEntity
import com.myprojects.expensetrackerapp.data.entity.SettingsEntity

@Database(
    entities = [ExpenseEntity::class, SettingsEntity::class],
    version = 1,
    exportSchema = true
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun expenseDao(): ExpenseDao
    abstract fun settingsDao(): SettingsDao

    companion object {
        const val DATABASE_NAME = "expense_db"
    }
}