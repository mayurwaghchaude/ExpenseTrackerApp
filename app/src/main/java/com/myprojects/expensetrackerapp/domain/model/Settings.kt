package com.myprojects.expensetrackerapp.domain.model

import com.myprojects.expensetrackerapp.data.entity.SettingsEntity

data class Settings(
    val currency: String = "INR",
    val currencySymbol: String = "₹",
    val monthlyBudget: Double = 0.0,
    val budgetAlertsEnabled: Boolean = true,
    val isDarkMode: Boolean = false,
    val monthStartDay: Int = 1
){
    fun toEntity() = SettingsEntity(
        id = 1,
        currency = currency,
        currencySymbol = currencySymbol,
        monthlyBudget = monthlyBudget,
        budgetAlertsEnabled = budgetAlertsEnabled,
        isDarkMode = isDarkMode,
        monthStartDay = monthStartDay
    )
}