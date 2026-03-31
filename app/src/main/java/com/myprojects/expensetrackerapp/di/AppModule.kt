package com.myprojects.expensetrackerapp.di

import android.app.Application
import androidx.room.Room
import com.myprojects.expensetrackerapp.data.dao.ExpenseDao
import com.myprojects.expensetrackerapp.data.datasource.AppDatabase
import com.myprojects.expensetrackerapp.data.repository.ExpenseRepositoryImpl
import com.myprojects.expensetrackerapp.domain.repository.ExpenseRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideDatabase(app: Application): AppDatabase {
        return Room.databaseBuilder(
            app,
            AppDatabase::class.java,
            AppDatabase.DATABASE_NAME
        ).build()
    }


    @Provides
    fun provideExpenseDao(db: AppDatabase): ExpenseDao {
        return db.expenseDao()
    }

    @Provides
    @Singleton
    fun provideExpenseRepository(db: AppDatabase): ExpenseRepository {
        return ExpenseRepositoryImpl(db.expenseDao())
    }
}