package com.myprojects.expensetrackerapp.domain.model

import com.myprojects.expensetrackerapp.data.entity.ExpenseEntity
import com.myprojects.expensetrackerapp.domain.enums.ExpenseType

data class Expense(
    val id: String,
    val title: String,
    val amount: Double,
    val category: String,
    val type: ExpenseType,
    val date: Long,
    val note: String = "",
    val createdAt: Long
) {
    fun toEntity() = ExpenseEntity(
        id = id,
        title = title,
        amount = amount,
        category = category,
        type = type.name,
        date = date,
        note = note,
        createdAt = createdAt
    )
}



