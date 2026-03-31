package com.myprojects.expensetrackerapp.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.myprojects.expensetrackerapp.domain.enums.ExpenseType
import com.myprojects.expensetrackerapp.domain.model.Expense
import java.util.UUID

@Entity(tableName = "expenses")
data class ExpenseEntity(
    @PrimaryKey val id: String = UUID.randomUUID().toString(),
    val title: String,
    val amount: Double,
    val category: String,
    val note: String = "",
    val type: String,          // "EXPENSE" or "INCOME"
    val date: Long, //timestamp
    val createdAt: Long = System.currentTimeMillis()
) {
    // --- Mappers ---

    fun toDomain() = Expense(
        id = id,
        title = title,
        amount = amount,
        category = category,
        type = ExpenseType.valueOf(type),
        date = date,
        note = note,
        createdAt = createdAt
    )
}
