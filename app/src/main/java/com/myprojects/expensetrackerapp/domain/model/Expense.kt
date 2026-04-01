package com.myprojects.expensetrackerapp.domain.model

import com.myprojects.expensetrackerapp.data.entity.ExpenseEntity
import com.myprojects.expensetrackerapp.domain.enums.ExpenseType
import java.util.UUID

data class Expense(
    val id: String? = null,
    val title: String,
    val amount: Double,
    val category: String,
    val type: ExpenseType,
    val date: Long,
    val note: String = "",
    val createdAt: Long? = null
) {
    fun toEntity() = ExpenseEntity(
        id = id ?: UUID.randomUUID().toString(),
        title = title,
        amount = amount,
        category = category,
        type = type.name,
        date = date,
        note = note,
        createdAt = createdAt ?: System.currentTimeMillis()
    )
}



