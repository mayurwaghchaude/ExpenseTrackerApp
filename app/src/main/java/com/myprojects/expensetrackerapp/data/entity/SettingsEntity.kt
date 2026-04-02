package com.myprojects.expensetrackerapp.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.myprojects.expensetrackerapp.domain.model.Settings

@Entity(tableName = "settings")
data class SettingsEntity(
    @PrimaryKey
    val id: Int = 1,             // single row table
    val currency: String = "INR",
    val currencySymbol: String = "₹",
    val monthlyBudget: Double = 0.0,
    val budgetAlertsEnabled: Boolean = true,
    val isDarkMode: Boolean = false,
    val monthStartDay: Int = 1   // 1 = 1st of month
){
    fun toDomain() = Settings(
        currency = currency,
        currencySymbol = currencySymbol,
        monthlyBudget = monthlyBudget,
        budgetAlertsEnabled = budgetAlertsEnabled,
        isDarkMode = isDarkMode,
        monthStartDay = monthStartDay
    )
}
